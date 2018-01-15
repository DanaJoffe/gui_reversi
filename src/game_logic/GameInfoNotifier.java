package game_logic;

import reversi_app.GameInfoListener;

/**
 * notifies game watchers
 */
public interface GameInfoNotifier {
  
  /**
   * add a game listener
   * @param watcher the game listener
   */
  public void addGameWatcher(GameInfoListener watcher); 
  /**
   * remove a game listener
   * @param watcher the game listener
   */
  public void removeGameWatcher(GameInfoListener watcher);
  
}
