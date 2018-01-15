package reversi_app;

import game_logic.ClickListener;

/**
 * notify click listeners in list
 */
public interface ClickNotifier {
  /**
   * add click listener to list of click listeners
   * @param listener
   */
	 public void addClickListener(ClickListener listener);
	 /**
	  * remove click listener from list of click listeners
	  * @param listener
	  */
	 public void removeClickListener(ClickListener listener);
}
