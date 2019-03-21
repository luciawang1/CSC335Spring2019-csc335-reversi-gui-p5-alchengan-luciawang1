import java.util.Scanner;

/**
 * @author Lucia Wang
 * 
 *         Reversi contains the main method for the reversi game to run.
 */
public class Reversi {

	public Scanner input;

	/**
	 * Constructor
	 */
	public Reversi() {
		input = new Scanner(System.in);

	}

	public static void main(String[] args) {
		ReversiModel model = new ReversiModel();
		ReversiController controller = new ReversiController(model);
		ReversiView view = new ReversiView(model);

		view.startGame();
		System.out.println(controller.toString());
		view.playyy();
	}

}
