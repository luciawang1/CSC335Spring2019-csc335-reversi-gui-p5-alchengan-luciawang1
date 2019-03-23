import java.util.Scanner;

import javafx.application.Application;

/**
 * @author Lucia Wang
 * 
 *         Reversi contains the main method for the reversi game to run.
 */
public class Reversi {

	public Scanner input;
	public ReversiView view;
	public ReversiModel model;

	/**
	 * Constructor
	 */
	public Reversi() {
		input = new Scanner(System.in);
		view = new ReversiView(model);

	}

	public static void main(String[] args) {
//		ReversiModel model = new ReversiModel();
//		ReversiController controller = new ReversiController(model);
//		ReversiView view = new ReversiView(model);
//
//		view.startGame();
//		System.out.println(controller.toString());
//		view.playyy();
		Application.launch(ReversiView.class, args);
	}

}
