package reversi_app;

/**
 * Listener that watches the game and reacts to changes in it.
 */
public interface GameInfoListener {
  /**
   * Switch current player
   * @param player color of current player
   */
	 public void currentPlayerChanged(game_logic.Color player);
	 /**
	  * Change scores of players
	  * @param first the score of the first player
	  * @param second the score of the second player
	  */
	 public void scoresChanged(Integer first, Integer second);
	 /**
	  * Change message of game
	  * @param msg new message
	  */
	 public void newMessages(String msg);
}
