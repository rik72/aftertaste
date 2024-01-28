package io.rik72.aftertaste.ui.views;

import java.io.File;
import java.util.Map.Entry;

import io.rik72.aftertaste.ui.Defaults;
import io.rik72.aftertaste.ui.ux.TerminalUX;
import io.rik72.bottlerack.view.AbstractView;
import io.rik72.brew.engine.finder.Finder;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.game.BrewController;
import io.rik72.brew.game.ui.Terminal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TerminalView extends AbstractView {

	public static int TEXT_COLS = 80;

	// private TextArea textArea;
	private TextFlow textFlow;
	private ScrollPane textPane;
	private TextField promptField;
	private HBox promptPane;
	private BorderPane allPane;
	private boolean scrollToBottom = false;
	private Future enterListener;
	private FileChooser fileChooser = new FileChooser();

	public TerminalView(Stage stage) {
		super(stage);
	}

	@Override
	protected Parent create() {

		// Elements ===========================================================
		textFlow = new TextFlow();
		textFlow.setMaxWidth(Defaults.WINDOW_WIDTH - 20);
		textFlow.setPadding(new Insets(0, 20, 0, 20));

		Label promptLabel = new Label("Your action:");
		promptLabel.setFont(Defaults.FONT_NORMAL);
		promptLabel.setMinWidth(90);
		promptLabel.setMaxWidth(90);
		promptField = new TextField();
		promptField.setPrefColumnCount(50);
		promptField.setFont(Defaults.FONT_NORMAL);
		promptField.setDisable(true);
		promptField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode kc = event.getCode();
				String text = promptField.getText();
				if (kc == KeyCode.ENTER) {
					if (text.length() > 0) {
						try {
							Terminal.get().hilightln("> " + text);
							promptField.clear();
							Results results = Terminal.get().executeInput(text);
							if (results != null)
								Terminal.get().consumeResults(results);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		// Layout ===========================================================
		// scrollable text pane
		textPane = new ScrollPane(textFlow);
		textPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        VBox.setVgrow(textPane, Priority.ALWAYS);     
        // textPane.setMinWidth(Region.USE_PREF_SIZE);
		textFlow.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (scrollToBottom) {
					textPane.setVvalue(textPane.getVmax());
					scrollToBottom = false;
				}
            }
        });
		
		// user input pane
		promptPane = new HBox(promptLabel, promptField);
		promptPane.setAlignment(Pos.CENTER_RIGHT);
		promptPane.setPadding(new Insets(10));

		// main pane
		allPane = new BorderPane();
		// - top will be used for location images
		// - bottom will be used for user input
		allPane.setCenter(textPane);

		// set GUI view on terminal
        Terminal.get().setUX(new TerminalUX(this));

		return allPane;
	}

	public void onOpen() {
		// Build and show scene
		stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode kc = event.getCode();
				if (kc == KeyCode.ENTER) {
					if (enterListener != null) {
						Future then = enterListener;
						enterListener = null;
						then.onSuccess();
					}
				}
			}
		});

		Entry<LoadPath, StoryDescriptor> storyData = Finder.get().getFirstStory();
		try {
			BrewController.load(storyData.getKey());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Terminal.get().intro();
	}

	public void onClose() {
		stage.getScene().setOnKeyPressed(null);
	}

	public void openConfirmBox(String question, Future then) {
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setResizable(false); 
		dialog.initOwner(stage);

		VBox dialogVbox = new VBox();
		dialogVbox.setPadding(new Insets(20));
		HBox dialogButtonsBox = new HBox();
		dialogButtonsBox.setSpacing(20);

		Region spacerv = new Region();
        VBox.setVgrow(spacerv, Priority.ALWAYS);     
        spacerv.setMinWidth(Region.USE_PREF_SIZE);
		
		Button yesButton = new Button("Yes");
		yesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();
				then.onSuccess();
			}
		});
		Region spacerl = new Region();
        HBox.setHgrow(spacerl, Priority.ALWAYS);     
        spacerl.setMinWidth(Region.USE_PREF_SIZE);
		Region spacerr = new Region();
        HBox.setHgrow(spacerr, Priority.ALWAYS);     
        spacerr.setMinWidth(Region.USE_PREF_SIZE);
		Button noButton = new Button("No");
		noButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();
				then.onFailure();
			}
		});

		dialogButtonsBox.getChildren().addAll(spacerl, yesButton, noButton, spacerr);
		dialogVbox.getChildren().addAll(new Text(question), spacerv, dialogButtonsBox);

		Scene dialogScene = new Scene(dialogVbox, 240, 120);
		dialog.setScene(dialogScene);

        dialogScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					Node node = dialogScene.focusOwnerProperty().get();
					if (node instanceof Button)
						((Button)node).fire();
				}
            }
        });		
		dialog.show();		
	}

	public TextFlow getTextFlow() {
		return textFlow;
	}

	public ScrollPane getTextPane() {
		return textPane;
	}

	public void setScrollToBottom() {
		this.scrollToBottom = true;
	}

	public void setEnterListener(Future enterListener) {
		this.enterListener = enterListener;
	}

	public Stage getStage() {
		return stage;
	}
	
	public TextField getPromptField() {
		return promptField;
	}

	public void showPromptPane() {
		allPane.setBottom(promptPane);
		allPane.layout();
	}

	public void setTopImage(Node imageNode) {
		allPane.setTop(imageNode);
		allPane.layout();
	}

	public void removeTopImage() {
		allPane.setTop(null);
		allPane.layout();
	}

	public File chooseOpenFile() {
		fileChooser.setTitle("Load game");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Savegame", "*.save")
		);
		return fileChooser.showOpenDialog(stage);
	}

	public File chooseSaveFile() {
		fileChooser.setTitle("Save game");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Savegame", "*.save")
		);
		return fileChooser.showSaveDialog(stage);
	}
}
