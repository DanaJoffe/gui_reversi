package game_logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import reversi_app.ClickableBoard;
import reversi_app.GameInfoListener;

public class GUIGameFlow extends GameFlow implements ClickListener {

	private Color currentPlayer_;
	private Map<Color, Integer> scores_;	
	private ClickableBoard visibleBoard_;
	private List<GameInfoListener> gameWatchers_;
	
	public GUIGameFlow(Board board, GameLogic logic, Map<Color, Player> players, Printer printer) {
		super(board, logic, players, printer);
		this.gameWatchers_= new ArrayList<GameInfoListener>();
		this.currentPlayer_ = Color.BLACK;
		this.scores_ = new HashMap<Color, Integer>();
		scores_.put(Color.BLACK, 2);
		scores_.put(Color.WHITE, 2);
	}
	
	public void addGameWatcher(GameInfoListener watcher) {
		this.gameWatchers_.add(watcher);
		notifyWhoIsCurrentPlayer();
		notifyScores();
	}
	public void removeGameWatcher(GameInfoListener watcher) {
		this.gameWatchers_.remove(watcher);
	}
	private void notifyWhoIsCurrentPlayer() {
		List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		if (!gameWatchers.isEmpty()) {
			for (GameInfoListener watcher: gameWatchers) {
				watcher.currentPlayerChanged(this.currentPlayer_);
			}	 
		}
	}
	private void notifyScores() {
		List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		if (!gameWatchers.isEmpty()) {
			for (GameInfoListener watcher: gameWatchers) {
				watcher.scoresChanged(this.scores_.get(Color.BLACK), this.scores_.get(Color.WHITE));
			}	 
		}
	}
	private void notifyNewMessage(String msg) {
		 List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		 if (!gameWatchers.isEmpty()) {
			 for (GameInfoListener watcher: gameWatchers) {
				 watcher.newMessages(msg);
			 }	 
		 }
	}
	
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
	
	private void updateScores(int flippedDisks, Color whoPlayed) {
		Color otherPlayer = Color.values()[(whoPlayed.ordinal() + 1) % Color.values().length];
		scores_.put(whoPlayed, scores_.get(whoPlayed) + flippedDisks + 1);
		scores_.put(otherPlayer , scores_.get(otherPlayer ) - flippedDisks);
		notifyScores();
	}
	
	private void checkStatus() {
	    if (!playerHasMove(currentPlayer_) && !playerHasMove(getNextPlayer(currentPlayer_))) {
    	  endGame();
	    } else if (num_disks_played_ == (board_.getRows() * board_.getCols())) {
	    	 endGame();
	    }
	}
	
	public void setVisibleBoard(ClickableBoard visibleBoard) {
		this.visibleBoard_ = visibleBoard;
		this.visibleBoard_ .addClickListener(this);
	}
	
	
	@Override
	public void playGame() {
	    this.initializeBoard();
	}
	
	private void passTurn() {
		this.currentPlayer_ = Color.values()[(this.currentPlayer_.ordinal() + 1) % Color.values().length];
   	    notifyWhoIsCurrentPlayer();
	}
	
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
	            //invalid input
	            return null;
	        }
	    } 
	    if (playerHasMove(getNextPlayer(currentPlayer_))) {
	       passTurn();
	    } else {
	       notifyNewMessage("skipped player");
	    }
	    return cellsFlliped;
	}

	private boolean playerHasMove(Color player) {
		return !logic_.getPossibleMoves(board_, player).isEmpty();
	}
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
		this.removeAllWatchers();
	}
	
	private void removeAllWatchers() {
		List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		if (!gameWatchers.isEmpty()) {
			for (GameInfoListener watcher: gameWatchers) {
				this.removeGameWatcher(watcher);
			}	 
		}
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
