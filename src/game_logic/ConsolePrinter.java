package game_logic;

public class ConsolePrinter implements Printer {
  
  public ConsolePrinter() {}

  @Override
  public void printBoard(Board board) {
    System.out.print(board);
  }


  @Override
  public void printMessage(String message, Point loc) {
    System.out.print(message);
  }
 
  @Override
  public void printMessage(String message) {
    printMessage(message, new Point(1,1));
  }
}
