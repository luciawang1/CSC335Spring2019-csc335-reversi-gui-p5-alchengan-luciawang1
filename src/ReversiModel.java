import java.util.Observable;

import javafx.scene.paint.Color;

public class ReversiModel extends Observable {
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
		
		board.setAt(3, 3, ReversiBoard.WHITE);
		board.setAt(3, 4, ReversiBoard.BLACK);
		board.setAt(4, 4, ReversiBoard.WHITE);
		board.setAt(4, 3, ReversiBoard.BLACK);
		
		stringBoard[3][3] = "W";
		stringBoard[3][4] = "B";
		stringBoard[4][4] = "W";
		stringBoard[4][3] = "B";
	}
	
	public ReversiModel(ReversiBoard board) {
		this.board = board;
		
		stringBoard = new String[dimension][dimension];
		for(int i=0; i < stringBoard.length; i++) {
			for(int j=0; j < stringBoard.length; j++) {
				if(this.board.getAt(i,j) == ReversiBoard.BLANK)
					stringBoard[i][j] = "_";
				else if(this.board.getAt(i, j) == ReversiBoard.WHITE)
					stringBoard[i][j] = "W";
				else if(this.board.getAt(i,j) == ReversiBoard.BLACK)
					stringBoard[i][j] = "B";
			}
		}
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
		int player = (color.equals(Color.WHITE) ? ReversiBoard.WHITE : ReversiBoard.BLACK );
		board.setAt(row, col, player);
		setChanged();
		notifyObservers(board);
	}
}