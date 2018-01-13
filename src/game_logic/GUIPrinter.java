package game_logic;

import java.util.Map;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GUIPrinter implements Printer {
	 private GridPane grid_;
	 private int gridRows;
	 private int gridCols;
	 
	 private Map<Integer,Color> playersColor_;
	
	
	public GUIPrinter(GridPane grid, Map<Integer,Color> playersColor) {
		this.grid_= grid;
		this.playersColor_ = playersColor;
	}
	@Override
	public void printBoard(Board board) { //print all cells
		// TODO Auto-generated method stub

	}
	public void printDisk(Circle disk) {
		//need to implement
	}
	public void printDisk(Cell cell, int radius) {
		//need to implement
	}
	

	@Override
	public void printMessage(String message, Point loc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printMessage(String message) {
		// TODO Auto-generated method stub

	}

}
