package reversi_app;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


/**
 * Run GUI Reversi
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("ReversiGame.fxml"));
			Scene scene = new Scene(root,680,480);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Reversi");
			primaryStage.setScene(scene);

			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("error: " + e.getMessage());
		}
	}
	public static void main(String[] args) {
		launch(args);

	}
}