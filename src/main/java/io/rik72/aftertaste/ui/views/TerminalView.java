package io.rik72.aftertaste.ui.views;

import java.io.File;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.ui.skin.CurrentSkin;
import io.rik72.aftertaste.ui.ux.TerminalUX;
import io.rik72.aftertaste.ui.views.menu.TopMenu;
import io.rik72.bottlerack.view.AbstractView;
import io.rik72.brew.engine.processing.execution.base.Future;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.vati.locale.Translations;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TerminalView extends AbstractView {

	public static int TEXT_COLS = 80;

	// private TextArea textArea;
	private TextFlow textFlow;
	private ScrollPane textPane;
	private TextField promptField;
	private HBox promptPane;
	private BorderPane rootPane;
	private BorderPane mainPane;
	private boolean scrollToBottom = false;
	private Future enterListener;
	private DirectoryChooser directoryChooser = new DirectoryChooser();
	private FileChooser fileChooser = new FileChooser();
	private boolean menuShown = false;
	private Label promptLabel;

	public TerminalView(Stage stage) {
		super(stage);
	}

	@Override
	protected Parent create() {

		// Elements ===========================================================
		textFlow = new TextFlow();
		textFlow.setMinWidth(App.WINDOW_WIDTH - 42);
		textFlow.setMaxWidth(App.WINDOW_WIDTH - 42);
		textFlow.setPrefHeight(App.WINDOW_HEIGHT);
		textFlow.setPadding(new Insets(0, 20, 0, 10));
		textFlow.setStyle("-fx-background-color: -aft-color-text-flow-bg;");

		promptLabel = new Label();
		adjustPromptLabel();
		promptLabel.setPadding(new Insets(0, 0, 0, 10));
		promptField = new TextField();
		promptField.setPrefColumnCount(200);
		promptField.setFont(CurrentSkin.getGUIFont());
		promptField.setDisable(true);
					
		promptField.setOnKeyPressed(event -> {
			KeyCode kc = event.getCode();
			String text = promptField.getText();
			if (kc == KeyCode.ENTER) {
				if (text.length() > 0) {
					try {
						Terminal.get().hilightln("> " + text);
						promptField.clear();
						Results results = Terminal.get().executeInput(text);
						Terminal.get().consumeResults(results);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

	    // final EventHandler<KeyEvent> promptKeyPressHandler = new EventHandler<>() {
		// 	@Override
		// 	public void handle(KeyEvent event) {
		// 		KeyCode kc = event.getCode();
		// 		String text = promptField.getText();
		// 		if (kc == KeyCode.ENTER) {
		// 			if (text.length() > 0) {
		// 				try {
		// 					Terminal.get().hilightln("> " + text);
		// 					promptField.clear();
		// 					Results results = Terminal.get().executeInput(text);
		// 					Terminal.get().consumeResults(results);
		// 				} catch (Exception e) {
		// 					e.printStackTrace();
		// 				}
		// 			}
		// 		}
		// 	}
		// };

		// promptField.setEventDispatcher(new EventDispatcher() {

		// 	@Override
		// 	public Event dispatchEvent(Event event, EventDispatchChain tail) {
		// 		// // First, dispatch event for the capturing phase
		// 		// event = dispatchCapturingEvent(event);
		// 		// if (event.isConsumed()) {
		// 		// 	return null;
		// 		// }

		// 		event = tail.dispatchEvent(event);

		// 		// once we've reached this point the capturing phase has completed

		// 		if (event != null) {
		// 			// Not consumed from down the chain so we now handle the
		// 			// bubbling phase of the process

		// 			if (event instanceof KeyEvent)
		// 				promptKeyPressHandler.handle((KeyEvent)event);

		// 			if (event.isConsumed()) {
		// 				// One of the EventHandlers invoked in dispatchBubblingEvent
		// 				// consumed the event. Return null to indicate processing is complete
		// 				return null;
		// 			}

		// 			if (!event.getEventType().getName().contains("MOUSE"))
		// 				System.out.println(event);
		// 		}

		// 		// return the event, or null if tail.dispatchEvent returned null
		// 		return event;
		// 	}	
		// });

		// Layout ===========================================================
		// scrollable text pane
		textPane = new ScrollPane(textFlow);
		textPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		BorderPane.setMargin(textPane, new Insets(0, 0, 0, 20));
        VBox.setVgrow(textPane, Priority.ALWAYS);     
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
		promptPane.setPadding(new Insets(0, 20, 5, 20));

		// main pane
		mainPane = new BorderPane();
		// - top will be used for location images
		// - bottom will be used for user input
		mainPane.setCenter(textPane);
		mainPane.setStyle("-fx-background-color: -aft-color-window-bg;");

		// root pane
		rootPane = new BorderPane();
		// - top will be used for menus
		// - bottom will be used for status bar (if any)
		rootPane.setCenter(mainPane);
		rootPane.setStyle("-fx-background-color: -aft-color-window-bg;");

		// set GUI view on terminal
        Terminal.get().setUX(new TerminalUX(this));

		return rootPane;
	}

	public void adjustPromptLabel() {
		promptLabel.setText(Translations.get("your_action"));
		promptLabel.setMinWidth(Region.USE_PREF_SIZE);
	}

	public void onOpen() {
		// Build and show scene
		stage.getScene().setOnKeyPressed(event -> {
			KeyCode kc = event.getCode();
			if (kc == KeyCode.ENTER) {
				if (enterListener != null) {
					Future then = enterListener;
					enterListener = null;
					then.onSuccess();
				}
			}
			else if (kc == KeyCode.ESCAPE) {
				if (!menuShown)
					showMenus();
				else
					hideMenus();
			}
		});
	}

	public void showMenus() {
		menuShown = true;
		((BorderPane)stage.getScene().getRoot()).setTop(new TopMenu());
	}

	public void hideMenus() {
		menuShown = false;
		((BorderPane)stage.getScene().getRoot()).setTop(null);
	}

	public void onClose() {
		stage.getScene().setOnKeyPressed(null);
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
		mainPane.setBottom(promptPane);
		mainPane.layout();
	}

	public void hidePromptPane() {
		mainPane.setBottom(null);
		mainPane.layout();
	}

	public void setTopImage(Node imageNode) {
		mainPane.setTop(imageNode);
		mainPane.layout();
	}

	public void removeTopImage() {
		mainPane.setTop(null);
		mainPane.layout();
	}

	public File chooseOpenFile(String dialogTitle, String description, String extension) {
		fileChooser.setTitle(dialogTitle);
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter(description, extension)
		);
		return fileChooser.showOpenDialog(stage);
	}

	public File chooseSaveFile(String dialogTitle) {
		fileChooser.setTitle(dialogTitle);
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Savegame", "*.save")
		);
		return fileChooser.showSaveDialog(stage);
	}

	public File chooseDirectory(String dialogTitle) {
		directoryChooser.setTitle(dialogTitle);
		return directoryChooser.showDialog(stage);
	}
}
