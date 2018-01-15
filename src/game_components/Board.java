package game_components;

import java.util.Arrays;

/**
 * board of game
 * holds matrix (rows x columns) of cells
 */
public class Board {
  
  static final int DEFAULT_ROWS = 8;
  static final int DEFAULT_COlS = 8;
  private int rows_;
  private int columns_;
  private Cell[][] board_;

  /**
   * constructs Board of Cells of size rows x columns 
   * @param rows
   * @param columns
   */
  public Board(int rows, int columns) {
    rows_ = rows;
    columns_ = columns;
    this.board_ = new Cell[this.rows_][];
    for (int i = 0; i < this.rows_; i++) {
      this.board_[i] = new Cell[this.columns_];
    }
    for (int i = 0; i < rows_; i++) {
      for (int j = 0; j < columns_; j++) {
        this.board_[i][j] = new Cell(i, j);
      }
    }
  }
  
  /**
   * constructs Board of Cells of size default rows x default columns  
   */
  public Board() {
    this(DEFAULT_ROWS, DEFAULT_COlS);
  }

  /**
   * copy constructor for board
   * @param oldBoard the board to copy
   */
  public Board(Board oldBoard) {
    rows_ = oldBoard.getRows();
    columns_ = oldBoard.getCols();
      this.board_ = new Cell[this.rows_][];
    for (int i = 0; i < this.rows_; i++) {
      this.board_[i] = new Cell[this.columns_];
    }
    for (int i = 0; i < rows_; i++) {
      for (int j = 0; j < columns_; j++) {
        this.board_[i][j] = oldBoard.getCell(i, j);
      }
    }
  }

  /**
   * access number of rows in board
   * @return rows
   */
  public int getRows() {
    return this.rows_;
  }

  /**
   *  access number of columns in board
   * @return columns
   */
  public int getCols() {
    return this.columns_;
  }

  /**
   * access cell in certain location
   * @param row of cell
   * @param col of cell
   * @return cell
   */
  public Cell getCell(int row, int col) {
    if (row >= 0 && col >= 0
        && row < this.rows_ && col < this.columns_) {
      return this.board_[row][col];
    } else {
      return null;
    }
  }

  /**
   * access cell in certain location
   * @param p the point location of cell
   * @return cell
   */
  public Cell getCell(Point p) {
    return this.getCell(p.getRow(), p.getCol());
  }

  /**
   * get neighbor of certain cell in certain direction
   * @param row of cell
   * @param col of cell
   * @param dir the direction of the neighbor
   * @return neighboring cell
   */
  public Cell getNeighboringCell(int row, int col, Direction dir) {

    switch(dir) {
      case N:
          return this.getCell(row - 1, col);
      case NE:
          return this.getCell(row - 1, col + 1);
      case E:
          return this.getCell(row, col + 1);
      case SE:
          return this.getCell(row + 1,col + 1);
      case S:
          return this.getCell(row + 1,col);
      case SW:
        return this.getCell(row + 1, col - 1);
      case W:
          return this.getCell(row,col - 1);
      case NW:
          return this.getCell(row - 1,col - 1);
      default:
        return null;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.deepHashCode(board_);
    result = prime * result + columns_;
    result = prime * result + rows_;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Board other = (Board) obj;
    if (!Arrays.deepEquals(board_, other.board_))
      return false;
    if (columns_ != other.columns_)
      return false;
    if (rows_ != other.rows_)
      return false;
    return true;
  }

  @Override
  public String toString() {
    //first row - #s of columns
    StringBuilder sb = new StringBuilder();
    sb.append(" | ");
    for (int i = 0; i < columns_; i++) {
      sb.append(i+1 + " | ");
    }
    sb.append("\n");
    for (int k = 0; k < (columns_ * 4 + 2); k++) {
      sb.append("-");
    }
    sb.append("\n");
    //following rows - # of rows & board itself
    for (int i = 0; i < rows_; i++) {
      sb.append(i+1 + "| ");
      for (int j = 0; j < columns_; j++) {
        if (board_[i][j].hasDisk()) {
          sb.append(board_[i][j].getDisk());
        } else {
          sb.append(" ");
        }
          sb.append(" | ");
        }
        sb.append("\n");
      for (int k = 0; k < (columns_ * 4 + 2); k++) {
        sb.append("-");
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}
