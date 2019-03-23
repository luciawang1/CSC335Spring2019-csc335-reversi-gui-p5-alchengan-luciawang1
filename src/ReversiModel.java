import java.util.Observable;

public class ReversiModel extends Observable {
	private ReversiBoard board;
	private String[][] stringBoard;
	private int dimension = 8;

	public ReversiModel() {
		board = new ReversiBoard();
		stringBoard = new String[dimension][dimension];
	}

	public void setAt(String player, int row, int col) {
		board.setAt((player == "W" ? ReversiBoard.WHITE : ReversiBoard.BLACK), row, col);
		setChanged();
	}

	public String getAt(int row, int col) {
		if (board.getAt(row, col) == ReversiBoard.WHITE)
			return "W";
		if (board.getAt(row, col) == ReversiBoard.BLACK)
			return "B";

		return "_";
	}

	public ReversiBoard getBoard() {
		return board;
	}
}