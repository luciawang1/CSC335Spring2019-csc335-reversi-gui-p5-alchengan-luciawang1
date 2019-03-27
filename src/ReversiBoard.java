import java.io.Serializable;

public class ReversiBoard implements Serializable {
	static final long serialVersionUID = 1L;

	public static int WHITE = 1;
	public static int BLACK = 2;
	public static int BLANK = 0;
	public static int DIM = 8;

	private int[][] board;

	public ReversiBoard() {
		this.board = new int[DIM][DIM];
	}

	public void setAt(int row, int col, int color) {
		board[row][col] = color;
	}
	
	public int getAt(int row, int col) {
		return board[row][col];
	}
<<<<<<< HEAD
}
=======

	/**
	 * Getter for row
	 * 
	 * @return row
	 */
	/*public int getRow() {
		return row;
	}*/

	/**
	 * Getter for column
	 * 
	 * @return column
	 */
	/*public int getCol() {
		return col;
	}*/

	/**
	 * Getter for color
	 * 
	 * @return color of the player (black cpu, white user)
	 */
	/*public Color getColor() {
		return color;
	}*/
}
>>>>>>> branch 'master' of https://github.com/CSC335Spring2019/csc335-reversi-gui-p5-alchengan-luciawang1.git
