package carbon.gui;

import java.util.Objects;

import carbon.Carbon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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

    private Carbon carbon;

    private final Image userImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/user.png")));
    private final Image carbonImage = new Image(Objects.requireNonNull(
            this.getClass().getResourceAsStream("/images/carbon.png")));

    /**
     * Initialises the window.
     */
    @FXML
    public void initialize() {
        // Allows the user to scroll without first clicking on the scrollbar
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> scrollPane.vvalueProperty().unbind());
    }

    /**
     * Injects the Carbon instance.
     */
    public void setCarbon(Carbon c) {
        carbon = c;
        dialogContainer.getChildren().addAll(
                DialogBox.getCarbonDialog(carbon.getResponse("start"), carbonImage, "start")
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Carbon's reply and then appends them
     * to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = carbon.getResponse(input);
        String command = carbon.getCommand();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getCarbonDialog(response, carbonImage, command)
        );
        userInput.clear();

        if (command.equals("bye")) {
            Platform.exit();
        }

        // Scroll to bottom
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }
}
