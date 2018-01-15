package game_logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * controls flow of game
 */
public class GameFlow {
  
  protected Board board_;
  protected GameLogic logic_;
  protected Map<Color,Player> players_;
  protected Printer printer_;
  protected int num_disks_played_;
  
  /**
   * constructs game flow with board, game logic, players and printer
   * @param board
   * @param logic
   * @param players
   * @param printer
   */
  public GameFlow(Board board, GameLogic logic,
      Map<Color, Player> players, Printer printer) {
    board_ = board;
    logic_ = logic;
    players_ = players;
    printer_ = printer;
    num_disks_played_ = 0;
  }

  /**
   * play the game
   */
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

  /**
   * initialize board with 4 disks in center: 2 of each color
   */
  protected void initializeBoard() {
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

  /**
   * play one round of game, one turn for each player
   * @return true if game should continue, false if game is over
   */
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
              move = players_.get(c).decideOnAMove(moves);
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

  /**
   * end of game, declare winner/tie
   */
  protected void endGame() {
    Player winner = this.determineWinner();
    if (winner == null) {
      printer_.printMessage(Message.declareTie());
    } else {
      printer_.printMessage(Message.declareWinner(winner.getName()));
    }
  }

  /**
   * player with more disks of his/her color is the winner
   * @return the winner Player or NULL if players tied
   */
  protected Player determineWinner() {
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
