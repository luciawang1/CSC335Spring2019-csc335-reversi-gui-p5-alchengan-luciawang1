
public class ReversiBoard {
	public static int WHITE = 1;
	public static int BLACK = 2;
	public static int BLANK = 0;
	public static int DIM = 8;
	
	private int[][] board;
	
	public ReversiBoard() {
		board = new int[DIM][DIM];
		board[3][3] = WHITE;
		board[4][4] = WHITE;
		board[3][4] = BLACK;
		board[4][3] = BLACK;
	}
	
	public int getAt(int row, int col) {
		return board[row][col];
	}
}
