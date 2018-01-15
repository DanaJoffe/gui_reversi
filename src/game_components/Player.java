package game_components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * player in game, has name and color
 */
public class Player {
  
  private String name_;
  private Color color_;
 
  /**
   * player in game, has name and color
   * @param name
   * @param color
   */
  public Player(String name, Color color) {
    name_ = name;
    color_ = color;
  }

  /**
   * access player's name
   * @return name
   */
  public String getName() {
    return this.name_;
  }

  /**
   * access player's color
   * @return color
   */
  public Color getColor() {
    return this.color_;
  }

  /**
   * insert disk with player's color in cell
   * @param cell to insert disk in
   */
  public void insertDisk(Cell cell) {
    cell.insertDisk(this.color_);
  }

  /**
   * flip disks in given cells
   * @param cells_to_flip
   */
  public void flipDisks(List<Cell> cells_to_flip) {
    for (Cell c : cells_to_flip) {
      c.getDisk().flipDisk();
    }
  }
  
  /**
   * informs the player that he has no moves to execute.
   */
  public void hasNoMoves() {
    String input = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
      input = br.readLine();
    } catch (IOException e) {
      System.out.println(e.toString());
    }
    return;
  }

  /**
   * asks the player to decide on a move to execute.
   * @param possibleMoves list of options
   * @return chosen location
   */
  public Point decideOnAMove(List<Cell> possibleMoves){
    String input;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
      input = br.readLine();
    } catch (IOException e) {
      System.out.println(e.toString());
      return null;
    }
    String[] p = input.split(" ");
    int r = Integer.parseInt(p[0]);
    int c = Integer.parseInt(p[1]);
    return new Point(r - 1,c - 1);
  }
}