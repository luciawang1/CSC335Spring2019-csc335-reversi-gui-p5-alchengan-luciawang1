import java.util.Observable;

public class ReversiModel extends Observable {
	private ReversiBoard board;
	
	public ReversiModel() {
		board = new ReversiBoard();
	}
	
	public String getAt(int row, int col) {
		if(board.getAt(row, col) == ReversiBoard.WHITE)
			return "W";
		if(board.getAt(row, col) == ReversiBoard.BLACK)
			return "B";
		
		return "O";
	}
}