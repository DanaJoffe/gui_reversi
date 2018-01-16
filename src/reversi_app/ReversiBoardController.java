package reversi_app;
import game_logic.*;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import game_components.Board;
import game_components.Cell;
import game_components.Point;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * controls the GUI reversi boar and is the GUI reversi baord
 */
public class ReversiBoardController extends GridPane implements ClickableBoard {
	//grid info 
	 private int gridRows_;
	 private int gridCols_;
	
	 private Board board;	
	 private Map<game_components.Color ,Color> playersColor_;
	 
	 private List<ClickListener> clickListeners_;
	 
	 private Rectangle rectPressed_;
	 private Rectangle rectEntered_;
	 private double xPosEntered_;
	 private double yPosEntered_;
	 	 
	 private Circle[][] disks_;
	 

	 /**
	  * construct reversi board controller
	  * reversi board controller has click listeners and disks
	  * will load fxml of board and set on mouse: pressed, released and moved
	  * @param board the game board
	  * @param playersColor the player's colors
	  */
	 public ReversiBoardController(Board board, Map<game_components.Color ,Color> playersColor) {
		 this.xPosEntered_ = -1;
		 this.yPosEntered_ = -1;
		 
		 this.board = board;
		 this.playersColor_=playersColor;
		 this.clickListeners_ = new ArrayList<ClickListener>();
		 
		 this.disks_ = new Circle[this.board.getRows()][this.board.getCols()];
		 
		 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReversiBoard.fxml"));
		 fxmlLoader.setRoot(this);
		 fxmlLoader.setController(this);
		 
		 try {
			 fxmlLoader.load(); 
			 
			this.setOnMousePressed(event -> {			
			  double x = event.getX();
			  double y = event.getY();
			  Point p = location(x, y);
			  Rectangle rec = new Rectangle(cellWidth(), cellHeight());
			  rec.setFill(Color.TRANSPARENT);
			  rec.setStroke(Color.CYAN);

			  if (p.getCol() < this.gridCols_ && p.getRow() < this.gridRows_ && p.getCol() >= 0 && p.getRow() >= 0) { 
			    this.notifyClickOnBoard(p.getRow(), p.getCol());
			    this.add(rec, p.getCol(), p.getRow());	 
			    this.rectPressed_ = rec;
			  }
			  event.consume();
			});
			
			this.setOnMouseReleased(event -> {						 
				this.getChildren().remove(rectPressed_);
				event.consume();
			});

			this.setOnMouseMoved(event -> {				
				double x = event.getX();
				double y = event.getY();
				Point p = location(x, y);
				 
				int cellHeight =cellHeight();
				int cellWidth = cellWidth();
				
				if (this.xPosEntered_ != x || this.yPosEntered_ != y) {
					this.getChildren().remove(rectEntered_);
					this.xPosEntered_=x;
					this.yPosEntered_=y;
 
				    Rectangle rec = new Rectangle(cellWidth, cellHeight);
				    rec.setFill(Color.TRANSPARENT);
			        rec.setStroke(Color.BLUE);

			    if (p.getCol() < this.gridCols_ && p.getRow() < this.gridRows_ &&
			        p.getCol() >= 0 && p.getRow() >= 0) { 
			      this.add(rec, p.getCol(), p.getRow());	 
			     	this.rectEntered_= rec;
			    }
				}
				event.consume();
			});
			
			this.setOnMouseExited(event -> {
				this.getChildren().remove(rectEntered_);
			});
		 } catch (IOException exception) {
			 throw new RuntimeException(exception);
		 }
	 }
	 
	 @Override
	 public void addClickListener(ClickListener listener) {
		 this.clickListeners_.add(listener);
	 }
	 
	 @Override
	 public void removeClickListener(ClickListener listener) {
		 this.clickListeners_.remove(listener);
	 }
	 
	 /**
	  * notify click listeners that board was clicked
	  * @param row of location of click
	  * @param col of location of click
	  */
	 private void notifyClickOnBoard(int row, int col) {
		 List<ClickListener> clickListeners = new ArrayList<ClickListener>(this.clickListeners_);
		 if (!clickListeners.isEmpty()) {
			 for (ClickListener listener: clickListeners) {
				 listener.clickEvent(row, col);
			 }	 
		 }
	 }

	 /**
	  * get point of cell location on board of (x,y)
	  * @param x
	  * @param y
	  * @return point
	  */
	 private Point location(double x, double y) {
		 int row=0, col=0;		 
		 int cellHeight = cellHeight();
		 int cellWidth = cellWidth();

		 int height = cellHeight;
		 int width = cellWidth;

		 while(x > width) {
			 width += cellWidth;
			 col++;
		 }
		 while(y > height) {
			 height += cellHeight;
			 row++;
		 }
		 return new Point(row,col);
	 }

	 /**
	  * draw board
	  */
	 public void draw() {
		 this.getChildren().clear();
		 int cellHeight = cellHeight();
		 int cellWidth = cellWidth();
		 
		 Set<Cell> cells = new HashSet<Cell>(); 
		 Rectangle rec;

		 for (int i = 0; i < board.getRows(); i++) {
			 for (int j = 0; j < board.getCols(); j++) {
				 rec = new Rectangle(cellWidth, cellHeight);
				 if (board.getCell(i, j).hasDisk()) {
					 
					 if (!cells.contains(board.getCell(i, j)))
						 cells.add(board.getCell(i, j));
					 else
						 System.out.println("contains");

				 }
				 rec.setFill(Color.ANTIQUEWHITE);
				 rec.setStroke(Color.BROWN);
				 this.add(rec, j, i);
			 }
		 }

		 drawDisks(cells);
		 initGridSizes();
	}
	
	/**
	 * set number of rows and columns in gridpane of board
	 */
  private void initGridSizes() {
    this.gridRows_ = getRowCount(this);
	  this.gridCols_ = getColCount(this);
	}
	 
	/**
	 * draw disks in given cells 
	 */
  public void drawDisks(Set<Cell> cells) {
    int radius = (int)Math.min(cellHeight(), cellWidth())/4;
		
		for (Cell cell: cells) {
		  int cellRow = cell.getLocation().getRow();
			int cellCol = cell.getLocation().getCol();

			Circle circle = new Circle(radius);
			circle.setFill(this.playersColor_.get(cell.getDisk().getColor()));
			
			DropShadow ds = new DropShadow();
			ds.setOffsetX(4.0f);
			ds.setOffsetY(4.0f);
			ds.setColor(Color.BLACK);
			circle.setEffect(ds);
			 
			this.getChildren().remove(this.disks_[cellRow][cellCol]);
			 
			this.add(circle, cellCol, cellRow);
			GridPane.setHalignment(circle, HPos.CENTER);
			GridPane.setValignment(circle, VPos.CENTER);
			 
			this.disks_[cellRow][cellCol] = circle;
		}	
	}
	
  /**
   * get single cell height
   * @return cell height
   */
	private int cellHeight() {
		return (int)this.getPrefHeight() / board.getRows();
	}
	
	/**
	 * get single cell's width
	 * @return cell width
	 */
	private int cellWidth() {
		return (int)this.getPrefWidth() / board.getCols();
	}
	
	/**
	 * get number of rows in gridpane of board
	 * @param pane the gridpane
	 * @return the number of rows
	 */
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
	
	 /**
   * get number of columns in gridpane of board
   * @param pane the gridpane
   * @return the number of columns
   */
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
