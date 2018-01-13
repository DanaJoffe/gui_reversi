package reversi_app;

public interface GameInfoListener {
	 public void currentPlayerChanged(game_logic.Color player);
	 public void scoresChanged(Integer first, Integer second);
	 public void newMessages(String msg);
}
