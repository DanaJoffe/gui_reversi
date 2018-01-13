package reversi_app;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		System.out.println("start REVERSI\n");
		try {
			HBox root = (HBox)FXMLLoader.load(getClass().getResource("ReversiGame.fxml"));
			Scene scene = new Scene(root,680,480);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Maze game");
			primaryStage.setScene(scene);

			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("error: " + e.getMessage());
		}
		System.out.println("start-ends\n");
	}
	public static void main(String[] args) {
		launch(args);

	}
}