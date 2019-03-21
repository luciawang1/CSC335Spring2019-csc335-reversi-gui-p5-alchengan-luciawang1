/**
 * @author Lucia Wang
 * 
 *         In MVC: Model represents the board for reversi (8 x 8 board) where
 *         the middle pieces are already set up
 */
public class ReversiModel {
	public int DIMENSION = 8;
	public String[][] board;

	/**
	 * Constructor
	 */
	public ReversiModel() {
		board = new String[DIMENSION][DIMENSION];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = "_";
			}
			// System.out.println();
		}
		board[3][3] = "W";
		board[3][4] = "B";
		board[4][4] = "W";
		board[4][3] = "B";
	}

	/**
	 * get the piece at board[r][c]
	 * 
	 * @param r: int, represents row
	 * @param c: int, represents column
	 * @return the piece at board[r][c] (String)
	 */
	public String getAt(int r, int c) {
		return board[r][c];
	}
}
