package io.rik72.aftertaste;

import io.rik72.aftertaste.config.UserPreferences;

public class CLIArgs {

    private String[] args;

    public void handle(String[] args) {
        this.args = args;
        handleArgs();
    }

    private void handleArgs() {
        if (args.length > 0) {
            if ("--resetPrefs".equals(args[0])) {
                UserPreferences.clear();
            }
        }
    }

	///////////////////////////////////////////////////////////////////////////
    private CLIArgs() {}
	private static CLIArgs instance = new CLIArgs();
	public static CLIArgs get() {
		return instance;
	}
}
