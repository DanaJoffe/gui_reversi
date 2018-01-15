package reversi_app;

import game_logic.ClickListener;

public interface ClickNotifiyer {
	 public void addClickListener(ClickListener listener);
	 public void removeClickListener(ClickListener listener);
}
