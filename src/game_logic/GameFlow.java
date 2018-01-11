package game_logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameFlow {

  private Board board_;
  private GameLogic logic_;
  private Map<Color,Player> players_;
  private Printer printer_;
  private int num_disks_played_;
  
  public GameFlow(Board board, GameLogic logic,
      Map<Color, Player> players, Printer printer) {
    board_ = board;
    logic_ = logic;
    players_ = players;
    printer_ = printer;
    num_disks_played_ = 0;
  }

  public void playGame() {
    this.initializeBoard();
    printer_.printMessage("\n");
    printer_.printMessage(Message.currentBoard());
    printer_.printBoard(board_);
    //loop through rounds till game over
    boolean continue_game = true;
    while (continue_game) {
      continue_game = this.playOneRound();
    }
    this.endGame();
  }

  private void initializeBoard() {
    int b_r = this.board_.getRows();
    int b_c = this.board_.getCols();
    //place 2 disks of each color in center of board
    for (Color c : Color.values()) {
      this.players_.get(c).insertDisk(this.board_.getCell(b_r/2 - 1,
             b_c/2 - 1 + (1- c.ordinal())));
      this.num_disks_played_++;
      this.players_.get(c).insertDisk(this.board_.getCell(b_r/2,
             b_c/2 - (1-c.ordinal())));
      this.num_disks_played_++;
    }
  }

  private boolean playOneRound() {
    for (Color c : Color.values()) {
    Point move = new Point();
      //game is over if board is full
      if (num_disks_played_ == (board_.getRows() * board_.getCols())) {
        return false;
      }
      printer_.printMessage(Message.startTurn(c));
      List<Cell> moves = new ArrayList<Cell>(logic_.getPossibleMoves(board_, c));
      boolean invalid_move = true;
        //player places a disk in one of possible moves
      if (!moves.isEmpty()) {
          while(invalid_move) {
              printer_.printMessage(Message.possibleMoves(moves));
              move = players_.get(c).decideOnAMove(board_, moves, logic_);
              printer_.printMessage("\n");
              if (board_.getCell(move) != null && 
                  moves.contains((board_.getCell(move)))) {
                  invalid_move = false;
                  players_.get(c).insertDisk(board_.getCell(move));
                  this.num_disks_played_++;
                  players_.get(c).flipDisks(logic_.getCellsToFlip(board_,
                      move, c));
              } else {
                  printer_.printMessage(Message.invalidInput());
              }
          }
        //player has no possible moves
        } else {
          printer_.printMessage(Message.noPossibleMoves());
          //if next player has no moves as well, game is over
          if (logic_.getPossibleMoves(board_,
              Color.values()[(c.ordinal() + 1) % Color.values().length]).isEmpty()) {
            return false;
          //else, play passes on to next player
          } else {
            players_.get(c).hasNoMoves();
          }
        }
      printer_.printMessage(Message.currentBoard());
      printer_.printBoard(board_);
      if (move.getRow()>-1){
        printer_.printMessage(Message.finishTurn(players_.get(c).getColor(), move));
      }

    }
    //game continues
    return true;
  }

  private void endGame() {
    Player winner = this.determineWinner();
    if (winner == null) {
      printer_.printMessage(Message.declareTie());
    } else {
      printer_.printMessage(Message.declareWinner(winner.getName()));
    }
  }

  private Player determineWinner() {
    Map<Color, Integer> num_of_disks = this.logic_.getScores(this.board_);
    //return winner or null if tie
    if (num_of_disks.get(Color.BLACK) > num_of_disks.get(Color.WHITE)) {
      return players_.get(Color.BLACK);
    } else if (num_of_disks.get(Color.BLACK) < num_of_disks.get(Color.WHITE)) {
      return players_.get(Color.WHITE);
    } else {
      return null;
    }
  }
}
