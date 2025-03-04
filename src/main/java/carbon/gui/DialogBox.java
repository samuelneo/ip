package carbon.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    private void changeDialogStyle(String commandType) {
        switch (commandType) {
        case "todo":
        case "deadline":
        case "event":
            dialog.getStyleClass().add("add-label");
            break;
        case "mark":
            dialog.getStyleClass().add("marked-label");
            break;
        case "delete":
            dialog.getStyleClass().add("delete-label");
            break;
        case "error":
            dialog.getStyleClass().add("error-label");
            break;
        default:
            // Do nothing
        }
    }

    /**
     * Returns a DialogBox, formatted from the right, containing the given parameters.
     *
     * @param text Text to contain.
     * @param img Image to contain.
     * @return DialogBox object.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Returns a DialogBox, formatted from the left, containing the given parameters.
     *
     * @param text Text to contain.
     * @param img Image to contain.
     * @param command Command type.
     * @return DialogBox object.
     */
    public static DialogBox getCarbonDialog(String text, Image img, String command) {
        var db = new DialogBox(text, img);
        db.flip();
        db.changeDialogStyle(command);
        return db;
    }
}
