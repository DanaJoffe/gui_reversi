package game_logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * supplies game logic according to game rules.
 */
public class GameLogic {

  GameLogic() {}

  /**
   * return a map that connects the player's color to his score.
   * the score of player X is the amount of disks he has on the board.
   * @param board the current board
   * @return map of player's scores
   */
  public Map<Color, Integer> getScores( Board board) {
    Map<Color, Integer> num_of_disks = new TreeMap<Color, Integer>();
    for (Color c : Color.values()) {
      num_of_disks.put(c, 0);
    }
    //count disks of each color
    for (int i = 0; i < board.getRows(); i++) {
      for (int j = 0; j < board.getCols(); j++) {
        if (board.getCell(i,j).getDisk() != null) {
          for (Color c : Color.values()) {
            if (board.getCell(i,j).getDisk().getColor() == c) {
              num_of_disks.put(c, num_of_disks.get(c) + 1);
            }
          }
        }
      }
    }
    return num_of_disks;
  }

  /**
   * calculates which cells are supposed to be have their disk flipped in all directions,
   * after a disk of color <color> has been inserted at (row, col) in <board>.
   * @param board the current board
   * @param row the row of the location of the new disk on the board
   * @param col the column of the location of the new disk on the board
   * @param color the new disk's color
   * @return list of cells to be flipped
   */
  public List<Cell> getCellsToFlip( Board board, int row, int col, Color color) {
    List<Cell> cells_to_flip = new ArrayList<Cell>();
    //find cells to flip in all directions
    for (Direction d : Direction.values()) {
      List<Cell> cells_d = new ArrayList<Cell>(this.cellsToFlipInDirection(board, row, col,
          color, d));
      cells_to_flip.addAll(cells_d);
      cells_d.clear();
    }
    return cells_to_flip;
  }
  
  /**
   * calculates which cells are supposed to be have their disk flipped in all directions,
   * after a disk of color <color> has been inserted at (row, col) in <board>.
   * @param board the current board
   * @param p the point of the location of the new disk on the board
   * @param color the new disk's color
   * @return list of cells to be flipped
   */
  public List<Cell> getCellsToFlip( Board board, Point p, Color color) {
    return this.getCellsToFlip(board, p.getRow(), p.getCol(), color);
  }

  /**
  * calculates which cells are supposed to be have their disk flipped in a specific direction
  * after a disk of color <color> has been inserted at (row, col) in <board>.
   * @param board the current board
   * @param row the row of the location of the new disk on the board
   * @param col the column of the location of the new disk on the board
   * @param color the new disk's color
   * @param dir the direction to check
   * @return List of cells that their disk should be flipped in given direction
   */
  private List<Cell> cellsToFlipInDirection( Board board,
      int row, int col, Color color, Direction dir) {
    List<Cell> cells_to_flip = new ArrayList<Cell>();
    Cell next = board.getNeighboringCell(row, col, dir);
    //no disks to flip in this direction
    if (next == null || !next.hasDisk() ||
        next.getDisk().getColor() == color) {
      return cells_to_flip;
    //might be disks to flip in this direction
    } else {
      while (next.getDisk().getColor() != color) {
        cells_to_flip.add(next);
        int curr_row = next.getLocation().getRow();
        int curr_col = next.getLocation().getCol();
        next = board.getNeighboringCell(curr_row, curr_col, dir);
        //flip in this direction
        if (next != null && next.hasDisk()
            && next.getDisk().getColor() == color) {
            break;
        //don't flip in this direction
        } else if (next == null || !next.hasDisk()) {
          cells_to_flip.clear();
          break;
        }
      }
      return cells_to_flip;
    }
  }
  
  /**
   * calculate player's (of color) possible moves in board
   * @param board the current board
   * @param color the current player's color 
   * @return List of cells of possible moves
   */
  List<Cell> getPossibleMoves(Board board,
      Color color) {
    List<Cell> possible_moves = new ArrayList<Cell>();
    boolean flip_dir = false;
    //loop through all cells in board
    for (int i = 0; i < board.getRows(); i++) {
      for (int j = 0; j < board.getCols(); j++) {
        //check in all directions if move will flip cells
        for (Direction d : Direction.values()) {
          flip_dir = flipInDirection(board, i, j, d, color);
          if (flip_dir) {
            possible_moves.add(board.getCell(i,j));
            break;
          }
        }
      }
    }
    return possible_moves;
  }

  /**
   * check if insertion in (row,col) will flip neighboring disks
   * @param board the current board
   * @param row the row of the location of the new disk on the board
   * @param col the column of the location of the new disk on the board
   * @param dir the direction to check
   * @param color the new disk's color
   * @return true if flip in this direction, otherwise false
   */
  boolean flipInDirection(Board board,
      int row, int col, Direction dir, Color color) {
    //cell is taken
    if (board.getCell(row, col).hasDisk()) {
      return false;
    }
    //neighbor is empty or its neighbor is empty/same color
    Cell next = board.getNeighboringCell(row, col, dir);
    if (next == null || !next.hasDisk() ||
        next.getDisk().getColor() == color) {
      return false;
    //need to continue checking in this direction
    } else {
      while (next != null && next.hasDisk()
          && next.getDisk().getColor() != color) {
        int curr_row = next.getLocation().getRow();
        int curr_col = next.getLocation().getCol();
        next = board.getNeighboringCell(curr_row, curr_col, dir);
        if (next != null && next.hasDisk()
            && next.getDisk().getColor() == color) {
            return true;
        }
      }
      return false;
    }
  }
}