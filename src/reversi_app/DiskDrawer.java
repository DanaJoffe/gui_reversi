package reversi_app;

import java.util.Set;

import game_components.Cell;

/**
 * draws disks
 */
public interface DiskDrawer {
  /**
   * draw disks
   * @param cells in which to draw disks
   */
	public void drawDisks(Set<Cell> cells);

}
