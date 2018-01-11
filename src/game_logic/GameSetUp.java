package game_logic;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class GameSetUp {

  static final int DEFAULT_ROWS = 8;
  static final int DEFAULT_COLS = 8;
  private Board board_;
  private GameLogic logic_;
  private Printer printer_;
  private Map<Color,Player> players_;

  GameSetUp(int board_rows, int board_cols) {
    this.board_ = new Board(board_rows, board_cols);
    this.logic_ = new GameLogic();
    this.printer_ = this.consoleInterface();
    this.players_ = this.players();
  }

  GameSetUp() {
    this(DEFAULT_ROWS, DEFAULT_COLS);
  }
  
  Printer consoleInterface() {
    return new ConsolePrinter();
  }

  private Map<Color,Player> players() {
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

  void playGame() {
    GameFlow game = new GameFlow(board_, logic_, players_, printer_);
    game.playGame();
  }
}
