package game_logic;

public interface ClickListener {
  /**
   * handle click event on screen
   * @param row of location of click
   * @param col of location of click
   */
	public void clickEvent(int row, int col);
}
