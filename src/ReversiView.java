import java.util.Scanner;

/**
 * @author Lucia Wang
 * 
 *         In MVC: View represents the actual play of the game, such as
 *         switching between 2 players and keeping track of whether the game is
 *         over or not
 */
public class ReversiView {

	private Scanner input;
	public ReversiModel model;
	public ReversiController controller;
	public int dimension;

	/**
	 * Constructor
	 * 
	 * @param ReversiModel model
	 */
	public ReversiView(ReversiModel model) {
		model = new ReversiModel();
		controller = new ReversiController(model);
		input = new Scanner(System.in);
		this.dimension = model.DIMENSION;

	}

	/**
	 * Starts game by printing out intro.
	 */
	public void startGame() {
		System.out.println("Welcome to Reversi");
		System.out.println();
		System.out.println("You are W.");
		System.out.println();

	}

	/**
	 * Ends game by counting pieces on the board and determining who has more
	 */
	public void endGame() {
		if (controller.getWScore() > controller.getBScore()) {
			System.out.println("You win!");
		} else if (controller.getBScore() > controller.getWScore()) {
			System.out.println("CPU wins");
		} else {
			System.out.println("tie");
		}
	}

	/**
	 * Col is entered as a character between a and h, this converts the character to
	 * an integer that's within the dimensions of the board
	 * 
	 * @param col: String to be converted to int
	 * @return int
	 */
	public int convertColToInt(String col) {
		if (col.equals("a"))
			return 0;
		else if (col.equals("b"))
			return 1;
		else if (col.equals("c"))
			return 2;
		else if (col.equals("d"))
			return 3;
		else if (col.equals("e"))
			return 4;
		else if (col.equals("f"))
			return 5;
		else if (col.equals("g"))
			return 6;
		else if (col.equals("h"))
			return 7;
		else
			return -1;
	}

	/**
	 * starts player v cpu game, starting with player and alternating turns every
	 * time someone moves.
	 */
	public void playyy() {
		String turn = "W";
		while (!controller.gameOver()) {
			controller.printScore();
			System.out.print("Where do you want to place your token? ");
			int row = 0;
			int col = 0;
			boolean wValid = false;
			boolean bValid = false;

			// keep prompting for input until valid user input
			if (turn == "W") {
				while (!wValid) {
					String location = input.nextLine();
					row = Integer.parseInt(location.toString().substring(1, 2)) - 1;
					if (row > dimension - 1 || row < 0) {
						System.out.println("Invalid move, try again. ");
						continue;
					}
					col = convertColToInt(location.toString().substring(0, 1));
					if (col > dimension - 1 || col < 0) {
						System.out.println("Invalid move, try again. ");
						continue;
					}
					wValid = controller.checkValid(row, col, turn, false);
					if (!wValid) {
						System.out.println("Invalid move, try again. ");
						System.out.println("askldjf");
					}
				}
				controller.checkValid(row, col, turn, true);
				controller.move(row, col, turn);
				System.out.println("\n" + controller.toString());
				turn = "B";
			}

			// cpu
			if (turn == "B") {
				int r = 0;
				int c = 0;
				while (!bValid) {
					r = (int) (Math.random() * dimension);
					c = (int) (Math.random() * dimension);
					bValid = controller.checkValid(r, c, turn, false);
				}
				controller.checkValid(r, c, turn, true);
				controller.move(r, c, turn);
				controller.printScore();
				System.out.println("The computer places a piece at " + intToString(c) + (r + 1));
				System.out.println();
				System.out.println(controller.toString());
				System.out.println();
				turn = "W";
			}
		}
		this.endGame();
	}

	/**
	 * converts integer to String(when printing out where cpu placed its piece.)
	 * 
	 * @param i: int, integer to be converted to String
	 * @return String
	 */
	private String intToString(int i) {
		if (i == 0)
			return "a";
		if (i == 1)
			return "b";
		if (i == 2)
			return "c";
		if (i == 3)
			return "d";
		if (i == 4)
			return "e";
		if (i == 5)
			return "f";
		if (i == 6)
			return "g";
		else
			return "h";

	}
}
