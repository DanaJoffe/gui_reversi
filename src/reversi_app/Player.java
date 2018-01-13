package reversi_app;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Player {
	 private GridPane grid;
	 private int gridRows;
	 private int gridCols;
	 
	 private int row;
	 private int col;
	 private ImageView iv;

	 public Player(GridPane grid, int row, int col) {
		 this.grid = grid;
		 this.gridRows = getRowCount(grid);
		 this.gridCols = getColCount(grid);
		 
		 this.row = row;
		 this.col = col;
		 // Load the player's image
		 iv = new ImageView(getClass().getResource("minion.png").toExternalForm());
	 }
	 
	 public void outOfBorders() {		 
		System.out.println("OUT OF BOUNDS!!\n");
		 
		 
	 }
	 
	 public void draw(int cellWidth, int cellHeight) {
		 this.gridRows = getRowCount(grid) -1;
		 this.gridCols = getColCount(grid) -1;
		 
		 System.out.println("gridCols = " + gridCols +"\n");
		 System.out.println("gridRows = " + gridRows );
		 
		 iv.setFitWidth(cellWidth);
		 iv.setFitHeight(cellHeight);
		 grid.getChildren().remove(iv);
		 grid.add(iv, col, row);
	 }
	 public void moveUp() {
		 if (row == 0) {
			 this.outOfBorders();
		 } else {
			 row--; // need to check that player doesn't hit a wall
			 redraw();
		 }
	 }
	 public void moveDown() {
		 if (row == this.gridRows) {
			 this.outOfBorders();
		 } else {
			 row++;
			 redraw();
		 }
	 }
	 public void moveLeft() {
		 if (col == 0) {
			 this.outOfBorders();
		 } else {
			 col--;
			 redraw();
		 }
	 }
	 public void moveRight() {
		 if (col == this.gridCols) {
			 this.outOfBorders();
		 } else {
			 col++;
			 redraw();
		 }
	 }
	 public void jump(int row, int col) {
		 if (col > this.gridCols || row > this.gridRows || col < 0 || row < 0) {
			 this.outOfBorders();
		 } else {
			 this.col = col;
			 this.row = row;
			 redraw();
		 }
	 }
	 
	 
	 private void redraw() {		 
		 grid.getChildren().remove(iv);
		 grid.add(iv, col, row);
	 }
	 private int getRowCount(GridPane pane) {
	        int numRows = pane.getRowConstraints().size();
	        for (int i = 0; i < pane.getChildren().size(); i++) {
	            Node child = pane.getChildren().get(i);
	            if (child.isManaged()) {
	                Integer rowIndex = GridPane.getRowIndex(child);
	                if(rowIndex != null){
	                    numRows = Math.max(numRows,rowIndex+1);
	                }
	            }
	        }
	        return numRows;
	    }
	 private int getColCount(GridPane pane) {
	        int numColumns = pane.getColumnConstraints().size();
	        for (int i = 0; i < pane.getChildren().size(); i++) {
	            Node child = pane.getChildren().get(i);
	            if (child.isManaged()) {
	                Integer rowIndex = GridPane.getColumnIndex(child);
	                if(rowIndex != null){
	                	numColumns= Math.max(numColumns,rowIndex+1);
	                }
	            }
	        }
	        return numColumns;
	    }
}