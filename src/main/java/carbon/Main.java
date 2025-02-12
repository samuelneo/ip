package carbon;

import java.io.IOException;
import java.util.Objects;

import carbon.gui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Carbon using FXML.
 */
public class Main extends Application {
    private final Carbon carbon = new Carbon();

    /**
     * Starts the GUI.
     *
     * @param stage Stage to use.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setCarbon(carbon); // inject the Carbon instance
            stage.getIcons().add(new Image(Objects.requireNonNull(
                    Main.class.getResourceAsStream("/images/carbon.png"))));
            stage.setTitle("Carbon");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
