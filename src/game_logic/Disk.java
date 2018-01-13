package game_logic;

import javafx.scene.shape.Circle;

//import javafx.scene.shape;

public class Disk {
  //enum
  private Color color_;
//  private javafx.scene.shape.Circle circle;
  
  public Disk(Color color) {
    color_ = color;
//    circle = new Circle();
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
  
//  public void drawDisk(Printer printer) {
//	  printer.printDisk(this.circle);
//  }
//  public Circle getCircle() {
//	  return this.circle;
//  }
//  public void setCircle(Circle c) {
//	  this.circle=c;
//  }

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