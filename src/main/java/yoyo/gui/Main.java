package yoyo.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import yoyo.Yoyo;
import yoyo.exception.MissingMemoryException;

/**
 * A GUI for Yoyo using FXML.
 */
public class Main extends Application {
    /**
     * {@inheritDoc}
     * Instantiates Yoyo and loads the Yoyo's GUI.
     */
    @Override
    public void start(Stage stage) {
        try {
            Yoyo yoyo = new Yoyo();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setYoyo(yoyo);  // inject the Yoyo instance
            stage.show();
        } catch (IOException | MissingMemoryException e) {
            e.printStackTrace();
        }
    }
}
