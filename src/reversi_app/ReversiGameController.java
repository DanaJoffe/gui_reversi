package reversi_app;
import game_logic.*;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReversiGameController implements Initializable, GameInfoListener {

	
  @FXML private MenuBar menu_bar;
	@FXML private Text currentPlayer;
	@FXML private Text firstPlayerScore;
	@FXML private Text secondPlayerScore;
	@FXML private Text firstPlayerColor;
	@FXML private Text secondPlayerColor;
	@FXML private Text gameMessages;
	
	@FXML private HBox center;
	@FXML private VBox root;
	 
	 private GUIgameSetUp gameSetUp_;
	 private Map<game_logic.Color ,Color> playersColor_;
	 private Map<game_logic.Color ,String> playersName_;

	 @FXML
	 private void openSettings(ActionEvent event) throws IOException {
	   Stage stage;
	   Parent new_root;
	   stage = new Stage();
	   new_root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
	   stage.setScene(new Scene(new_root));
	   stage.initModality(Modality.APPLICATION_MODAL);
	   stage.initOwner(menu_bar.getScene().getWindow());
	   stage.showAndWait(); 
	 }
	 
	 @FXML
	 private void exitGame(ActionEvent event) {
	   Stage stage = (Stage)menu_bar.getScene().getWindow();
	   stage.close();
	 }
	 
	 @FXML
	 private void startNewGame(ActionEvent event) throws IOException {
	   Stage stage;
	   Parent new_root;
	   stage = (Stage)menu_bar.getScene().getWindow();
	   new_root = FXMLLoader.load(getClass().getResource("ReversiGame.fxml"));
	   Scene scene = new Scene(new_root,680,480);
	   stage.setScene(scene);
	   stage.show();
	 }

	 private void initializeGame() {
		 List<String> colors = SettingsController.colorsSettings(); 
		 setPlayersColors(colors.get(0), colors.get(1));
		 
		 this.playersName_ =  new HashMap<game_logic.Color, String>();
		 this.playersColor_ = new HashMap<game_logic.Color, Color>();
		 //first player
		 playersName_.put(game_logic.Color.BLACK, colors.get(0));
		 playersColor_.put(game_logic.Color.BLACK, Color.valueOf(colors.get(0).toUpperCase()));
		 //second player
		 playersName_.put(game_logic.Color.WHITE, colors.get(1));
		 playersColor_.put(game_logic.Color.WHITE, Color.valueOf(colors.get(1).toUpperCase()));
		 
		 int size = SettingsController.boardSizeSettings();
		 List<GameInfoListener> watchers = new ArrayList<GameInfoListener>();
		 watchers.add(this);
		 this.gameSetUp_= new GUIgameSetUp(size, size, watchers);		 
//=======
//		 int size = SettingsController.boardSizeSettings(); 
//		 this.gameSetUp_= new GameSetUp(size, size);
//		 this.flow_= new GUIGameFlow(gameSetUp_.getBoard(), gameSetUp_.getLogic(),
//				 gameSetUp_.getPlayers(), gameSetUp_.getPrinter());
//		 this.board = gameSetUp_.getBoard();
//
//		 this.flow_.initializeBoard();
//		 this.flow_.addGameWatcher(this);
//>>>>>>> 523a7a6fddc990f584f57e389f9c37573a680ca6
	 }
	 
	 private void setPlayersColors(String first, String second) {
		 firstPlayerColor.setText(first);
		 secondPlayerColor.setText(second);
	 }
	 
	 public void currentPlayerChanged(game_logic.Color player) {
		 currentPlayer.setText(this.playersName_.get(player));
	 }
	 public void scoresChanged(Integer first, Integer second) {
		 firstPlayerScore.setText(first.toString());
		 secondPlayerScore.setText(second.toString());
	 }
	 public void newMessages(String msg) {
		 msg = msg.replaceAll("WHITE", this.playersName_.get(game_logic.Color.WHITE));
		 msg = msg.replaceAll("BLACK", this.playersName_.get(game_logic.Color.BLACK));
		 gameMessages.setText(msg);
		 gameMessages.setFill(Color.RED);
	 }
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {	 
		 initializeGame();
		 ReversiBoardController reversiBoard = new ReversiBoardController(this.gameSetUp_.getBoard(), playersColor_);

		 center.getChildren().add(0, reversiBoard);
		 root.widthProperty().addListener((observable, oldValue, newValue) -> {
			 double boardNewWidth = newValue.doubleValue() - 210;
			 reversiBoard.setPrefWidth(boardNewWidth);
			 reversiBoard.draw();
		 });

		 root.heightProperty().addListener((observable, oldValue, newValue) -> {
			 double boardNewHeight = newValue.doubleValue() - 55;
			 reversiBoard.setPrefHeight(boardNewHeight);
			 reversiBoard.draw();
		 });
		 
		 root.setOnMouseReleased(reversiBoard.getOnMouseReleased());
		 root.setOnMousePressed(reversiBoard.getOnMousePressed());
		 root.setOnMouseMoved(reversiBoard.getOnMouseMoved()); 
		 root.setOnMouseExited(reversiBoard.getOnMouseExited());
		 
		 gameSetUp_.setVisibleBoard(reversiBoard);
		 gameSetUp_.playGame();
	 }

}
