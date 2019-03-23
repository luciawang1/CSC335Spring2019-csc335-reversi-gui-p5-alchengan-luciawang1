
public class ReversiBoard {
	public static int WHITE = 1;
	public static int BLACK = 2;
	public static int BLANK = 0;
	public static int DIM = 8;
	
	private int[][] board;
	
	public ReversiBoard() {
		board = new int[DIM][DIM];
	}
	
	public int getAt(int row, int col) {
		return board[row][col];
	}
	
	public void setAt(int player, int row, int col) {
		board[row][col] = player;
	}
}
