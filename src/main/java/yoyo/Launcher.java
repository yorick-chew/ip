package yoyo;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 */
public class Launcher {
    /**
     * Entry point for the Yoyo chatbot.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}