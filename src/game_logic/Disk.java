package game_logic;

public class Disk {
  
  private Color color_;
  
  public Disk(Color color) {
    color_ = color;
  }

  public Color getColor() {
    return this.color_;
  }

  public void flipDisk() {
    switch (this.color_) {
      case BLACK:
        this.color_ = Color.WHITE;
        break;
      case WHITE:
        this.color_ = Color.BLACK;
        break;
      default:
        break;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((color_ == null) ? 0 : color_.hashCode());
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
    Disk other = (Disk) obj;
    if (color_ != other.color_)
      return false;
    return true;
  }

  //don't really need this
  @Override
  public String toString() {
    switch(color_) {
    case BLACK:
      return "X";
    case WHITE:
      return "O";
    default:
      return "";
    }
  }
}