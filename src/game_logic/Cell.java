package game_logic;

public class Cell {
  
  private Point location_;
  private Disk disk_;
  private boolean isEmpty_;
  
  public Cell(int row, int column) {
    location_ = new Point(row, column);
    disk_ = null;
    isEmpty_ = true;
  }

  public Cell(Cell oldCell) {
    location_ = new Point(oldCell.getLocation().getRow(), 
                      oldCell.getLocation().getCol());
    if (oldCell.hasDisk()) {
      insertDisk(oldCell.getDisk().getColor());
    } else {
      disk_ = null;
      isEmpty_ = true;
    }
  }

  public Point getLocation() {
    return this.location_;
  }

  boolean hasDisk() {
    return !this.isEmpty_;
  }

  public void insertDisk(Color color) {
    this.disk_ = new Disk(color);
    this.isEmpty_ = false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((disk_ == null) ? 0 : disk_.hashCode());
    result = prime * result + (isEmpty_ ? 1231 : 1237);
    result = prime * result + ((location_ == null) ? 0 : location_.hashCode());
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
    Cell other = (Cell) obj;
    if (disk_ == null) {
      if (other.disk_ != null)
        return false;
    } else if (!disk_.equals(other.disk_))
      return false;
    if (isEmpty_ != other.isEmpty_)
      return false;
    if (location_ == null) {
      if (other.location_ != null)
        return false;
    } else if (!location_.equals(other.location_))
      return false;
    return true;
  }

  public Disk getDisk() {
    return this.disk_;
  }
}
