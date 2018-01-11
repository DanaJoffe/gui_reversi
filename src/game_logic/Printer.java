package game_logic;

public interface Printer {

  public void printBoard(Board board);
  
  public void printMessage(String message, Point loc);
  
  public void printMessage(String message);
}
