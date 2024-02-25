package io.rik72.brew.engine.processing.execution.base;

import java.util.ArrayList;
import java.util.List;

import io.rik72.brew.engine.db.entities.TextGroup;
import io.rik72.brew.engine.utils.TextUtils;

public class Results {
	private boolean success;
	private boolean refresh;
	private String feedback;
	private boolean emphasis;
	private boolean restart;
	private List<String> texts = new ArrayList<>();
	private TextGroup transition;
	private TextGroup finale;

	public Results(boolean success, boolean refresh, String feedback, TextGroup transition, TextGroup finale, boolean emphasis, boolean restart) {
		this.success = success;
		this.refresh = refresh;
		this.feedback = feedback;
		this.transition = transition;
		this.finale = finale;
		this.emphasis = emphasis;
		this.restart = restart;
	}

	public Results(boolean success, boolean refresh, String feedback, TextGroup transition, TextGroup finale, boolean emphasis) {
		this(success, refresh, feedback, transition, finale, emphasis, false);
	}

	public Results(boolean success, boolean refresh, String feedback, TextGroup transition, TextGroup finale) {
		this(success, refresh, feedback, transition, finale, false, false);
	}

	public Results(boolean success, boolean refresh, String feedback) {
		this(success, refresh, feedback, null, null, false, false);
	}

	public Results(boolean success) {
		this(success, false, null, null, null, false, false);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public boolean isEmphasis() {
		return emphasis;
	}

	public void setEmphasis(boolean emphasis) {
		this.emphasis = emphasis;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public TextGroup getTransition() {
		return transition;
	}

	public void setTransition(TextGroup transition) {
		this.transition = transition;
	}

	public TextGroup getFinale() {
		return finale;
	}

	public void setFinale(TextGroup finale) {
		this.finale = finale;
	}

	public List<String> getTexts() {
		return texts;
	}

	@Override
	public String toString() {
		return "{ Results :: " +
			success + " : " + 
			refresh + " : " + 
			emphasis + " : " + 
			restart + " : " + 
			TextUtils.quote(feedback) + " : " + 
			texts +
		" }";
	}
}
