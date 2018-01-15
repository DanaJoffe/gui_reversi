package game_logic;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import game_components.Board;
import game_components.Color;
import game_components.Player;
import reversi_app.ClickableBoard;
import reversi_app.GameInfoListener;

/**
 * sets up game for GUI
 */
public class GUIgameSetUp extends GameSetUp {

  private GUIGameFlow flow_;

  /**
   * constructs game set up for GUI.
   * has a member of GUI game flow.
   * @param board_rows the number of rows in board
   * @param board_cols the number of columns in board
   * @param watchers the game listeners
   */
  public GUIgameSetUp(int board_rows, int board_cols, List<GameInfoListener> watchers) {
  	super(board_rows, board_cols);
  	this.flow_ = new GUIGameFlow(board_, logic_, players_, null);
   	for (GameInfoListener watcher: watchers) {
   		this.flow_.addGameWatcher(watcher);
  	}
  }

	@Override
	public void playGame() {
		this.flow_.playGame();
	}
	
	@Override
	protected Map<Color,Player> players() {
		Map<Color,Player> players = new TreeMap<Color,Player>();
	    for (Color c : Color.values()) {
	    	players.put(c, new Player("", c));
	    }
	    return players;
	}

	/**
	 * set up visible board in game flow
	 * @param visibleBoard
	 */
	public void setVisibleBoard(ClickableBoard visibleBoard) {
		this.flow_.setVisibleBoard(visibleBoard);
	}

	/**
	 * access game's board
	 * @return board
	 */
	public Board getBoard() {
		return this.board_;
	}
}
