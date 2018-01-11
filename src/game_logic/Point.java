package game_logic;

public class Point {
	
  static final int DEF_ROW = -1;
  static final int DEF_COL = -1;
	private int row_;
	private int column_;
	
	public Point(int row, int column) {
	  row_ = row;
	  column_ = column;	  
	}
	
	public Point() {
	  this(DEF_ROW, DEF_COL);	  
	}

	public int getCol() {
	  return column_;
	}

	public int getRow() {
	  return row_;
	}

	@Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + column_;
    result = prime * result + row_;
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
    Point other = (Point) obj;  
    if (column_ != other.column_)
      return false;
    if (row_ != other.row_)
      return false;
    return true;
  }
	
	@Override
  public String toString() {
	  int row = row_+1;
	  int col = column_+1;
    return "(" + row + "," + col + ")";
  }
}
