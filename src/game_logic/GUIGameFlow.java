package game_logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import reversi_app.GameInfoListener;
import reversi_app.ReversiBoardController;

public class GUIGameFlow extends GameFlow implements ClickListener {

	private Color currentPlayer_;
	private Map<Color, Integer> scores_;	
	private ReversiBoardController visibleBoard_;
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
	public void notifyWhoIsCurrentPlayer() {
		List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		 if (!gameWatchers.isEmpty()) {
			 for (GameInfoListener watcher: gameWatchers) {
				 watcher.currentPlayerChanged(this.currentPlayer_);
			 }	 
		 }
	}
	public void notifyScores() {
		List<GameInfoListener> gameWatchers = new ArrayList<GameInfoListener>(this.gameWatchers_);
		 if (!gameWatchers.isEmpty()) {
			 for (GameInfoListener watcher: gameWatchers) {
				 watcher.scoresChanged(this.scores_.get(Color.BLACK), this.scores_.get(Color.WHITE));
			 }	 
		 }
	}
	public void notifyNewMessage(String msg) {
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
	
	public  void checkStatus() {
	    if (!playerHasMove(currentPlayer_) && !playerHasMove(getNextPlayer(currentPlayer_))) {
    	  System.out.println("nobody has move");
    	  endGame();
	    } else if (num_disks_played_ == (board_.getRows() * board_.getCols())) {
	    	 System.out.println("board is full");
	    	 endGame();
	    }
	}
	
	public void setVisibleBoard(ReversiBoardController visibleBoard) {
		this.visibleBoard_ = visibleBoard;
		this.visibleBoard_ .addClickListener(this);
	}
	
	
	@Override
	public void playGame() {
	    this.initializeBoard();
	    
	    //need to make currentPlayer updated
	    
	    
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
	
	public void initializeBoard() {
		super.initializeBoard();
	}
	
	private void passTurn() {
		this.currentPlayer_ = Color.values()[(this.currentPlayer_.ordinal() + 1) % Color.values().length];
   	    System.out.println("move passed to " + this.currentPlayer_);
   	    notifyWhoIsCurrentPlayer();
	}
	
	private List<Cell> playOneTurn(int row, int col) {
		Point move;
		List<Cell> cellsFlliped = null;
//	      printer_.printMessage(Message.startTurn(this.currentPlayer_))
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
	        	  //update visible Game with an error
	              return null;
	          }
	          
	      } 
		  if (playerHasMove(getNextPlayer(currentPlayer_))) {
	         passTurn();
	      } else {
	    	  System.out.println("next player has no move");
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
	//round = one turn of one player
	protected boolean playOneRound() {
		
	      
		
//	    for (Color c : Color.values()) {
//	    Point move = new Point();
//	      //game is over if board is full
//	      if (num_disks_played_ == (board_.getRows() * board_.getCols())) {
//	        return false;
//	      }
//	      printer_.printMessage(Message.startTurn(c));
//	      List<Cell> moves = new ArrayList<Cell>(logic_.getPossibleMoves(board_, c));
//	      boolean invalid_move = true;
//	        //player places a disk in one of possible moves
//	      if (!moves.isEmpty()) {
//	          while(invalid_move) {
//	              printer_.printMessage(Message.possibleMoves(moves));
//	              move = players_.get(c).decideOnAMove(board_, moves, logic_);
//	              printer_.printMessage("\n");
//	              if (board_.getCell(move) != null && 
//	                  moves.contains((board_.getCell(move)))) {
//	                  invalid_move = false;
//	                  players_.get(c).insertDisk(board_.getCell(move));
//	                  this.num_disks_played_++;
//	                  players_.get(c).flipDisks(logic_.getCellsToFlip(board_,
//	                      move, c));
//	              } else {
//	                  printer_.printMessage(Message.invalidInput());
//	              }
//	          }
//	        //player has no possible moves
//	        } else {
//	          printer_.printMessage(Message.noPossibleMoves());
//	          //if next player has no moves as well, game is over
//	          if (logic_.getPossibleMoves(board_,
//	              Color.values()[(c.ordinal() + 1) % Color.values().length]).isEmpty()) {
//	            return false;
//	          //else, play passes on to next player
//	          } else {
//	            players_.get(c).hasNoMoves();
//	          }
//	        }
	      
	      
	      
	      
//	      printer_.printMessage(Message.currentBoard());
//	      printer_.printBoard(board_);
//	      if (move.getRow()>-1){
//	        printer_.printMessage(Message.finishTurn(players_.get(c).getColor(), move));
//	      }
//
//	    }
	    //game continues
	    return true;
	  }
	
	@Override
	protected void endGame() {
		this.visibleBoard_.removeClickListener(this);
	    Player winner = this.determineWinner();
	    if (winner == null) {
	    	System.out.println("IT'S A TIE!");
	    	notifyNewMessage(Message.declareTie());
	    } else {
	    	System.out.println(Message.declareWinner(winner.getColor().toString()));
	    	notifyNewMessage(Message.declareWinner(winner.getColor().toString()));
//	      printer_.printMessage(Message.declareWinner(winner.getName()));
	    }
		System.out.println("Scores:\n BLACK: " + scores_.get(Color.BLACK)  + "\n WHITE: "+scores_.get(Color.WHITE));

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
