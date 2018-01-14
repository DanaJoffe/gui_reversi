package reversi_app;
import game_logic.*;
import game_logic.Player;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.sun.javafx.css.converters.ColorConverter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ReversiGameController implements Initializable, GameInfoListener {

	
	@FXML
	private Text currentPlayer;
	@FXML
	private Text firstPlayerScore;
	@FXML
	private Text secondPlayerScore;
	@FXML
	private Text firstPlayerColor;
	@FXML
	private Text secondPlayerColor;
	@FXML
	private Text gameMessages;
	
	@FXML
	 private HBox root;
	 
	 private Board board;
	 private GameSetUp gameSetUp_;
	 private GUIGameFlow flow_;
	 private Map<game_logic.Color ,Color> playersColor_;
	 private Map<game_logic.Color ,String> playersName_;
	 

	 private void initializeGame() {
		 List<String> colors = readColorsFromFile(); 
		 setPlayersColors(colors.get(0), colors.get(1));
		 
		 this.playersName_ =  new HashMap<game_logic.Color, String>();
		 this.playersColor_ = new HashMap<game_logic.Color, Color>();
		 //first player
		 playersName_.put(game_logic.Color.BLACK, colors.get(0));
		 playersColor_.put(game_logic.Color.BLACK, Color.valueOf(colors.get(0).toUpperCase()));
		 //second player
		 playersName_.put(game_logic.Color.WHITE, colors.get(1));
		 playersColor_.put(game_logic.Color.WHITE, Color.valueOf(colors.get(1).toUpperCase()));
		 
		 int size = readBoardSizeFromFile();
		 this.gameSetUp_= new GameSetUp(size, size);
		 this.flow_= new GUIGameFlow(gameSetUp_.getBoard(), gameSetUp_.getLogic(),
				 gameSetUp_.getPlayers(), gameSetUp_.getPrinter());
		 this.board = gameSetUp_.getBoard();

		 this.flow_.initializeBoard();
		 this.flow_.addGameWatcher(this);
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
		 
		 ReversiBoardController reversiBoard = new ReversiBoardController(this.board, playersColor_);
		 this.flow_.setVisibleBoard(reversiBoard);

		 root.getChildren().add(0, reversiBoard);
	  
		 root.widthProperty().addListener((observable, oldValue, newValue) -> {
			 double boardNewWidth = newValue.doubleValue() - 210;
			 reversiBoard.setPrefWidth(boardNewWidth);
			 reversiBoard.draw();
		 });

		 root.heightProperty().addListener((observable, oldValue, newValue) -> {
			 double boardNewHeight = newValue.doubleValue() - 25;

			 reversiBoard.setPrefHeight(boardNewHeight);
			 reversiBoard.draw();
		 });
		 
		 
		 root.setOnMouseReleased(reversiBoard.getOnMouseReleased());
		 root.setOnMousePressed(reversiBoard.getOnMousePressed());
		 root.setOnMouseMoved(reversiBoard.getOnMouseMoved()); 
		 root.setOnMouseExited(reversiBoard.getOnMouseExited());
		 

	 }
	 
	 public int readBoardSizeFromFile() {
		 return 8;
	 }
	 public List<String> readColorsFromFile() {
		 List<String> colors = new ArrayList<String>();
		 colors.add("SEAGREEN");
		 colors.add("TOMATO");
		 return colors;
	 }
}
