package io.rik72.aftertaste;

public class Main {

    public static void main(String[] args) throws Exception {
        CLIArgs.get().handle(args);
        App.main();
    }
}
