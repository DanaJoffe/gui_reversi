package game_logic;

import java.util.List;

public class Message {

  public static String getPlayerName(Color color) {
    return "Player " + color + " please enter your name: ";
  }

  public static String currentBoard() {
    return "Current board:\n";
  }

  public static String startTurn(String player) {
    return player + ": It's your turn.\n";
  }

  public static String startTurn(Color c) {
      return c + ": It's your turn.\n";
    }

  public static String finishTurn(String player, Point p) {
    return player + " played " + p + '\n' + '\n';
  }

  public static String finishTurn(Color c, Point p) {
    return c + " played " + p + '\n'+ '\n';
  }

  public static String possibleMoves(List<Cell> moves) {
    StringBuilder sb = new StringBuilder();
    sb.append("Your possible moves: ");
    sb.append(moves.get(0).getLocation());
    for (int i = 1; i < moves.size(); i++) {
      sb.append("," + moves.get(i).getLocation());
    }
    sb.append("\n");
    sb.append("Please enter your move as follows: row column ");
    return sb.toString();
  }

  public static String noPossibleMoves() {
    StringBuilder sb = new StringBuilder();
    sb.append("No possible moves.\n");
    sb.append("Play passes back to the other player.\n");
    sb.append("Press any key and then Enter to continue\n");
    return sb.toString();
  }

  public static String invalidInput() {
    return "Input is invalid. Please try again.\n";
  }

  public static String declareWinner(String winner) {
    return "Game over. The winner is... " + winner + "!\n";
  }

  public static String declareTie() {
    return "Game over. It's a tie!\n";
  }
}
