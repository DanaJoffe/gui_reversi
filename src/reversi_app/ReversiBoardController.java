package reversi_app;
import game_logic.*;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import game_logic.Point;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ReversiBoardController extends GridPane implements ClickableBoard {
	//grid info 
	 private int gridRows_;
	 private int gridCols_;
	
	 private Board board;	
	 private Map<game_logic.Color ,Color> playersColor_;
	 
	 private List<ClickListener> clickListeners_;
	 
	 private Rectangle rectPressed_;
	 private Rectangle rectEntered_;
	 private double xPosEntered_;
	 private double yPosEntered_;
	 	 
	 private Circle[][] disks_;
	 

	 public ReversiBoardController(Board board, Map<game_logic.Color ,Color> playersColor) {
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
	 
	 public void addClickListener(ClickListener listener) {
		 this.clickListeners_.add(listener);
	 }
	 public void removeClickListener(ClickListener listener) {
		 this.clickListeners_.remove(listener);
	 }
	 
	 private void notifyClickOnBoard(int row, int col) {
		 List<ClickListener> clickListeners = new ArrayList<ClickListener>(this.clickListeners_);
		 if (!clickListeners.isEmpty()) {
			 for (ClickListener listener: clickListeners) {
				 listener.clickEvent(row, col);
			 }	 
		 }
	 }

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
	 
	 private void initGridSizes() {
		 this.gridRows_ = getRowCount(this);
		 this.gridCols_ = getColCount(this);
	 }
	 
	public void drawDisks(Set<Cell> cells) {
		int radius = (int)Math.min(cellHeight(), cellWidth())/4;
		
		for (Cell cell: cells) {
			 int cellRow = cell.getLocation().getRow();
			 int cellCol = cell.getLocation().getCol();

			 Circle circle = new Circle(radius);
			 circle.setFill(this.playersColor_.get(cell.getDisk().getColor()));
			 circle.setStroke(Color.BLACK);
			 
			 this.getChildren().remove(this.disks_[cellRow][cellCol]);
			 
			 this.add(circle, cellCol, cellRow);
			 GridPane.setHalignment(circle, HPos.CENTER);
			 GridPane.setValignment(circle, VPos.CENTER);
			 
			 this.disks_[cellRow][cellCol] = circle;
		}	
	}
	
	private int cellHeight() {
		return (int)this.getPrefHeight() / board.getRows();
	}
	
	private int cellWidth() {
		return (int)this.getPrefWidth() / board.getCols();
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


