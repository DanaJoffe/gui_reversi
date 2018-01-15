package game_UI;

import game_components.Board;
import game_components.Point;

/**
 * prints messages and reversi board to reversi's players.
 */
public interface Printer {

  /**
   * prints a given board.
   * @param board 
   */
  public void printBoard(Board board);
  
  /**
   * prints a given message at given location
   * @param message
   * @param loc
   */
  public void printMessage(String message, Point loc);
  
  /**
   * prints a given message
   * @param message
   */
  public void printMessage(String message);
}
