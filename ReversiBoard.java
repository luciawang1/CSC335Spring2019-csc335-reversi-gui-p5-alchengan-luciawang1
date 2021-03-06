import java.io.Serializable;

import javafx.scene.paint.Color;

public class ReversiBoard implements Serializable {
	static final long serialVersionUID = 1;

	public static int WHITE = 1;
	public static int BLACK = 2;
	public static int BLANK = 0;
	public static int DIM = 8;

	private int[][] board;



	public void setAt(int player, int row, int col) {
		board[row][col] = player;
	}

	
	
	
	
	/**
	 * row
	 */
	private int row;

	/**
	 * col
	 */
	private int col;

	/**
	 * color: white = user, black = cpu
	 */
	private Color color;

	/**
	 * Instantiate instance variables
	 * 
	 * @param row   = row where move was made
	 * @param col   = col where move was made
	 * @param color
	 */
	public void set(int row, int col, Color color) {
		this.color = color;
		this.row = row;
		this.col = col;
	}

	/**
	 * Getter for row
	 * 
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Getter for column
	 * 
	 * @return column
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Getter for color
	 * 
	 * @return color of the player (black cpu, white user)
	 */
	public Color getColor() {
		return color;
	}
}
