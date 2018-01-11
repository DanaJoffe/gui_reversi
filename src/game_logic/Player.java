package game_logic;

import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Player {
  
  private String name_;
  private Color color_;
  
  Player(String name, Color color) {
    name_ = name;
    color_ = color;
  }

  String getName() {
    return this.name_;
  }

  Color getColor() {
    return this.color_;
  }

  void insertDisk(Cell cell) {
    cell.insertDisk(this.color_);
  }

  void flipDisks(List<Cell> cells_to_flip) {
    for (Cell c : cells_to_flip) {
      c.getDisk().flipDisk();
    }
  }
  
  void hasNoMoves() {
    String input;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
      input = br.readLine();
    } catch (IOException e) {
      System.out.println(e.toString());
    }
    return;
  }

  Point decideOnAMove(Board board, List<Cell> possibleMoves,
      GameLogic logic){
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
