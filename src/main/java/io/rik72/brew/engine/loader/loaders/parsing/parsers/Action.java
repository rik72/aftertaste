package io.rik72.brew.engine.loader.loaders.parsing.parsers;

import java.util.ArrayList;
import java.util.List;

import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ActionRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ConsequenceRaw;

public class Action extends Parser {

	public OneAction oneAction;
	public TwoAction twoAction;
	public List<Consequence> consequences;

	public Action(OneAction oneAction, TwoAction twoAction, List<Consequence> consequences) {
		this.oneAction = oneAction;
		this.twoAction = twoAction;
		this.consequences = consequences;
	}

	public static Action parse(ActionRaw raw) {

		OneAction oneAction = OneAction.parse(raw.action);
		TwoAction twoAction = oneAction == null ? TwoAction.parse(raw.action) : null;

		if (oneAction == null && twoAction == null)
			throw new IllegalParseException("action", raw.action,
				"( " + OneAction.oneActionHR + " | " + TwoAction.twoActionHR + " )");
		
		List<Consequence> consequences = new ArrayList<>();

		if (raw.consequences != null) {
			for (ConsequenceRaw cRaw : raw.consequences) {
				consequences.add(Consequence.parse(cRaw));
			}
		}

		return new Action(oneAction, twoAction, consequences);
	}
}