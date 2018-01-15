package game_logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import game_UI.ConsolePrinter;
import game_UI.Message;
import game_UI.Printer;
import game_components.Board;
import game_components.Color;
import game_components.Player;

/**
 * set up game
 */
public class GameSetUp {

  static final int DEFAULT_ROWS = 8;
  static final int DEFAULT_COLS = 8;
  protected Board board_;
  protected GameLogic logic_;
  protected Printer printer_;
  protected Map<Color,Player> players_;

  /**
   * game set up constructor
   * set board, logic, printer and players
   * @param board_rows number of rows in game
   * @param board_cols number of columns in game
   */
  public GameSetUp(int board_rows, int board_cols) {
    this.board_ = new Board(board_rows, board_cols);
    this.logic_ = new GameLogic();
    this.printer_ = this.consoleInterface();
    this.players_ = this.players();
  }
   
  /**
   * construct game set up with default board size
   */
  GameSetUp() {
    this(DEFAULT_ROWS, DEFAULT_COLS);
  }
  
  /**
   * get printer for console
   * @return console printer
   */
  private Printer consoleInterface() {
    return new ConsolePrinter();
  }

  /**
   * get players
   * @return map of players with their colors
   */
  protected Map<Color,Player> players() {
    Map<Color,Player> players = new TreeMap<Color,Player>();
    for (Color c : Color.values()) {
      this.printer_.printMessage(Message.getPlayerName(c));
      String name;
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      try {
        name = br.readLine();
      } catch (IOException e) {
        this.printer_.printMessage(e.toString());
        return null;
      }
      players.put(c, new Player(name, c));
    }
    return players;
  }

  /**
   * play game
   */
  void playGame() {
    GameFlow game = new GameFlow(board_, logic_, players_, printer_);
    game.playGame();
  }
}
