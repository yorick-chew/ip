package yoyo.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import yoyo.Yoyo;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Yoyo yoyo;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/UserImg.png"));
    private Image yoyoImage = new Image(this.getClass().getResourceAsStream("/images/YoyoImg.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Yoyo instance. */
    public void setYoyo(Yoyo yoyo) {
        this.yoyo = yoyo;
        String greetingMessage = yoyo.getGreeting();
        dialogContainer.getChildren().addAll(DialogBox.getYoyoDialog(greetingMessage, yoyoImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Yoyo's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = yoyo.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getYoyoDialog(response, yoyoImage)
        );
        userInput.clear();
        if (yoyo.isTerminated()) {
            Platform.exit();
        }
    }
}

