import java.util.Observable;

import javafx.scene.paint.Color;

public class ReversiModel extends java.util.Observable {
	private ReversiBoard board;
	private String[][] stringBoard;

	private int dimension = 8;

	public ReversiModel() {
		board = new ReversiBoard();
		stringBoard = new String[dimension][dimension];
		for (int i = 0; i < stringBoard.length; i++) {
			for (int j = 0; j < stringBoard.length; j++) {
				stringBoard[i][j] = "_";
			}
		}
		stringBoard[3][3] = "W";
		stringBoard[3][4] = "B";
		stringBoard[4][4] = "W";
		stringBoard[4][3] = "B";
	}

	public void setAt(String player, int row, int col) {
		board.setAt((player == "W" ? ReversiBoard.WHITE : ReversiBoard.BLACK), row, col);
		setChanged();
	}

	public String getAt(int row, int col) {
		return stringBoard[row][col];
	}

	public ReversiBoard getBoard() {
		return board;
	}

	public String[][] getStringBoard() {
		return stringBoard;
	}

	public int getDimension() {
		return dimension;
	}

	/**
	 * updates board after a player has moved with the right color, and the right
	 * color flips on the board, then notifies view that changes have been made
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	public void setBoard(int row, int col, Color color) {
		board.set(row, col, color);
		setChanged();
		notifyObservers(board);
	}
}