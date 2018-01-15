package game_logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import game_UI.Message;
import game_UI.Printer;
import game_components.Board;
import game_components.Cell;
import game_components.Color;
import game_components.Player;
import game_components.Point;
import reversi_app.ClickableBoard;
import reversi_app.GameInfoListener;

/**
 * game flow for GUI. 
 * GUIGameFlow extends GameFlow, is a game-info notifier and a click listener.
 */
public class GUIGameFlow extends GameFlow implements GameInfoNotifier, ClickListener {

	private Color currentPlayer_;
	private Map<Color, Integer> scores_;	
	private ClickableBoard visibleBoard_;
	private List<GameInfoListener> gameWatchers_;
	
	/**
	 * construct game flow for GUI
	 * open containers for game watchers and game scores.
	 * @param board 
	 * @param logic
	 * @param players
	 * @param printer
	 */
	public GUIGameFlow(Board board, GameLogic logic, Map<Color, Player> players, Printer printer) {
		super(board, logic, players, printer);
		this.gameWatchers_= new ArrayList<GameInfoListener>();
		this.scores_ = new HashMap<Color, Integer>();
	}
	
	@Override
	public void addGameWatcher(GameInfoListener watcher) {
		this.gameWatchers_.add(watcher);
	}
	
  @Override
	public void removeGameWatcher(GameInfoListener watcher) {
		this.gameWatchers_.remove(watcher);
	}
  
  /**
   * remove all game listeners
   */
  private void removeAllGameWatchers() {
    List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
    if (!gameWatchers.isEmpty()) {
      for (GameInfoListener watcher: gameWatchers) {
        this.removeGameWatcher(watcher);
      }  
    }
  }
	
	/**
	 * notify game listeners who current player is
	 */
	private void notifyWhoIsCurrentPlayer() {
		List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		if (!gameWatchers.isEmpty()) {
			for (GameInfoListener watcher: gameWatchers) {
				watcher.currentPlayerChanged(this.currentPlayer_);
			}	 
		}
	}
	
	/**
	 *  notify game listeners of updated scores
	 */
	private void notifyScores() {
		List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		if (!gameWatchers.isEmpty()) {
			for (GameInfoListener watcher: gameWatchers) {
				watcher.scoresChanged(this.scores_.get(Color.BLACK), this.scores_.get(Color.WHITE));
			}	 
		}
	}
	
	/**
	 * notify game listeners of new message that is relevant to the game flow
	 * @param msg
	 */
	private void notifyNewMessage(String msg) {
		 List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		 if (!gameWatchers.isEmpty()) {
			 for (GameInfoListener watcher: gameWatchers) {
				 watcher.newMessages(msg);
			 }	 
		 }
	}
	
	 /**
   * set visible board
   * and add this GUIGameFlow as click listener for visible board
   * @param visibleBoard
   */
  public void setVisibleBoard(ClickableBoard visibleBoard) {
    this.visibleBoard_ = visibleBoard;
    this.visibleBoard_ .addClickListener(this);
  }
	
	/**
	 * play current player's turn at clicked cell
	 */
	@Override
	public void clickEvent(int row, int col) {
		notifyNewMessage("");
		Color playsNow = currentPlayer_;
		List<Cell> cellsToUpdate = playOneTurn(row, col);
		if (cellsToUpdate != null) {
			updateScores(cellsToUpdate.size(), playsNow);
			cellsToUpdate.add(this.board_.getCell(row, col));
			this.visibleBoard_.drawDisks(new HashSet<Cell>(cellsToUpdate));
		}
		checkStatus();		
	}
	
	/**
	 * update player's scores
	 * @param flippedDisks the number of disks that were flipped
	 * @param whoPlayed the current player's color
	 */
	private void updateScores(int flippedDisks, Color whoPlayed) {
		Color otherPlayer = Color.values()[(whoPlayed.ordinal() + 1) % Color.values().length];
		scores_.put(whoPlayed, scores_.get(whoPlayed) + flippedDisks + 1);
		scores_.put(otherPlayer , scores_.get(otherPlayer ) - flippedDisks);
		notifyScores();
	}
	
	/**
	 * check status of game.
	 * game is over when neither player has possible moves or if board is full.
	 */
	private void checkStatus() {
	    if (!playerHasMove(currentPlayer_) && !playerHasMove(getNextPlayer(currentPlayer_))) {
    	  endGame();
	    } else if (num_disks_played_ == (board_.getRows() * board_.getCols())) {
	    	 endGame();
	    }
	}
	
	@Override
	public void playGame() {
	    this.initializeBoard();
	    scores_.put(Color.BLACK, 2);
	    scores_.put(Color.WHITE, 2);	
	    this.currentPlayer_ = Color.BLACK;
	    notifyScores();
	    notifyWhoIsCurrentPlayer();
	}
	
	/**
	 * pass turn to other player.
	 */
	private void passTurn() {
		this.currentPlayer_ = this.getNextPlayer(this.currentPlayer_);
		notifyWhoIsCurrentPlayer();
	}
	
	/**
	 * play current player's turn.
	 * @param row the row of clicked cell
	 * @param col the column of clicked cell
	 * @return list of cells that were flipped
	 */
	private List<Cell> playOneTurn(int row, int col) {
		Point move;
		List<Cell> cellsFlliped = null;
	  List<Cell> moves = new ArrayList<Cell>(logic_.getPossibleMoves(board_, this.currentPlayer_));
	  //player places a disk in one of possible moves
	  if (!moves.isEmpty()) {
	    move = new Point(row, col);
	    if (board_.getCell(move) != null && 
	   	  moves.contains((board_.getCell(move)))) {
	    	players_.get(this.currentPlayer_).insertDisk(board_.getCell(move));
	      this.num_disks_played_++;
	      cellsFlliped = logic_.getCellsToFlip(board_, move, this.currentPlayer_);
	      players_.get(this.currentPlayer_).flipDisks(cellsFlliped);    
	     } else {
	       //invalid move - don't place new disk
	       notifyNewMessage("invalid move");
	       return null;
	     }
	  }
	  //if other player has possible moves - pass turn to other player
	  if (playerHasMove(getNextPlayer(currentPlayer_))) {
	    passTurn();
	  //else, skip other player's turn
	  } else {
	     notifyNewMessage("skipped player");
    }
    return cellsFlliped;
	}

	/**
	 * check if player has possible moves.
	 * @param player the player's color
	 * @return true if player has possible moves, else return false
	 */
	private boolean playerHasMove(Color player) {
		return !logic_.getPossibleMoves(board_, player).isEmpty();
	}
	
	/**
	 * get next player
	 * @param curPlayer the current player's color
	 * @return next player
	 */
	private Color getNextPlayer(Color curPlayer) {
		return Color.values()[(curPlayer.ordinal() + 1) % Color.values().length];
	}
	
	@Override
	protected void endGame() {
		this.visibleBoard_.removeClickListener(this);
	  Player winner = this.determineWinner();
	  if (winner == null) {
	    notifyNewMessage(Message.declareTie());
	  } else {
	  	notifyNewMessage(Message.declareWinner(winner.getColor().toString()));
	  }
		this.removeAllGameWatchers();
	}
	
	@Override
	protected Player determineWinner() {
	  //return winner or null if tie
	  if (this.scores_.get(Color.BLACK) > this.scores_.get(Color.WHITE)) {
	    return players_.get(Color.BLACK);
	  } else if (this.scores_.get(Color.BLACK) < this.scores_.get(Color.WHITE)) {
	    return players_.get(Color.WHITE);
	  } else {
	    return null;
	  }
	}
}
