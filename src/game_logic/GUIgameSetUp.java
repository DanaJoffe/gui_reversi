package game_logic;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import reversi_app.ClickableBoard;
import reversi_app.GameInfoListener;

public class GUIgameSetUp extends GameSetUp {

    private GUIGameFlow flow_;

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
	    	players.put(c, new Player("don't_care", c));
	    }
	    return players;
	}

	public void setVisibleBoard(ClickableBoard visibleBoard) {
		this.flow_.setVisibleBoard(visibleBoard);
	}

	public Board getBoard() {
		return this.board_;
	}
}
