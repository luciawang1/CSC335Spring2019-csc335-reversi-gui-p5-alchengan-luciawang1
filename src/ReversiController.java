
/**
 * @author Lucia Wang
 * 
 *         In MVC: Controller handles how the game works, such as if the move is
 *         valid or not, if the game is over or not, and the scores
 */
public class ReversiController {

	public ReversiModel model;
	public int bScore;
	public int wScore;
	String[][] board;
	int dimension;

	/**
	 * Constructor
	 * 
	 * @param ReversiModel model
	 */
	public ReversiController() {
		this.model = new ReversiModel();
		//this.board = model.board;
		//this.dimension = model.DIMENSION;
		bScore = 2;
		wScore = 2;

	}

	/**
	 * Returns physical representation of board as String
	 * 
	 * @return String board
	 */
	public String toString() {
		String s = "";
		for (int i = 0; i < board.length; i++) {
			s += (i + 1) + " ";
			for (int j = 0; j < board.length; j++) {
				s += board[i][j] + " ";
			}
			s += "\n";
		}
		s += "  a b c d e f g h";
		return s;
	}

	/**
	 * Returns score of W(white)
	 * 
	 * @return int score
	 */
	public int getWScore() {
		wScore = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (model.getAt(i, j).equals("W"))
					wScore += 1;
			}
		}
		return wScore;

	}

	/**
	 * Returns the number of squares occupied by b
	 * 
	 * @return int score
	 */
	public int getBScore() {
		bScore = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (model.getAt(i, j).equals("B"))
					bScore += 1;
			}
		}
		return bScore;
	}

	/**
	 * prints out the current score
	 */
	public void printScore() {
		System.out.println();
		System.out.println("The score is " + getWScore() + ":" + getBScore() + ".");
	}

	/**
	 * puts the piece for the player at the specified row and col on the board
	 * 
	 * @param col: int, represents column
	 * @param row: int, represents row
	 * @param player: String, "W" or "B"
	 */
	public void move(int row, int col, String player) {
		board[row][col] = player;
	}

	/**
	 * determines if the game is over based on if there are anymore empty spaces
	 * 
	 * @return true if there are no more empty pieces; false if there are no more
	 */
	public boolean gameOver() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++)
				if (board[i][j] == "_")
					return false;
		}
		return true;
	}

	/**
	 * determines if the move at row, col for the specified player is valid
	 * 
	 * @param row: int, the row that the player wants to put their piece at
	 * @param col: int, the column that the player wants to put their piece at
	 * @param player: String, "B" or "W"
	 * @param actuallyMove: boolean, true if you actually want to move the piece, or
	 *        false if you're just trying to check if the move is valid or not
	 *        without actually moving
	 */
	public boolean checkValid(int row, int col, String player, boolean actuallyMove) {
		boolean isValid = false;
		if (model.getAt(row, col) != "_")
			return false;
		for (int dX = -1; dX < 2; dX++) {
			for (int dY = -1; dY < 2; dY++) {

				// continue if it is checking itself
				if (dX == 0 && dY == 0) {
					continue;
				}

				// check 8 directions
				int checkRow = row + dX;
				int checkCol = col + dY;

				// needs to be within board dimensions
				if (checkRow >= 0 && checkCol >= 0 && checkRow < dimension && checkCol < dimension) {

					// if it is white, check for black, and vice versa
					if (model.getAt(checkRow, checkCol) == (player == "W" ? "B" : "W")) {

						// distance
						for (int d = 0; d < dimension; d++) {
							int r = row + d * dX;
							int c = col + d * dY;

							// continue if it is not on the board
							if (r < 0 || c < 0 || r > dimension - 1 || c > dimension - 1)
								continue;

							// if you find a valid move
							if (model.getAt(r, c).equals(player)) {

								// are you actually moving or just checking
								if (actuallyMove) {

									// flip if actuallyMove is true
									for (int d2 = 1; d2 < d; d2++) {
										int rFlip = row + d2 * dX;
										int cFlip = col + d2 * dY;
										board[rFlip][cFlip] = player;
									}
								}
								isValid = true;
								break;
							}
						}
					}
				}
			}
		}
		return isValid;
	}

}
