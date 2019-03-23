import java.util.Observable;
import java.util.Scanner;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Lucia Wang
 * 
 *         In MVC: View represents the actual play of the game, such as
 *         switching between 2 players and keeping track of whether the game is
 *         over or not
 */
public class ReversiView extends javafx.application.Application implements java.util.Observer {

	// handle user input and cpu
	public ReversiController controller;

	// 8 by 8 board in this case
	public int dimension = 8;

	// graphics context to draw board
	private GraphicsContext gc;

	// flag for game over
	private boolean gameOver;

	// 16 + 8 * 20 = 208
	private int rowPixels = 208;
	private int colPixels = 208;

	/**
	 * Constructor
	 * 
	 * @param ReversiModel model
	 */
	public ReversiView(ReversiModel model) {
		controller = new ReversiController(model);
		controller.model.addObserver(this);
		gameOver = false;
	}

	@Override
	public void start(Stage stage) throws Exception {
		Canvas board = new Canvas(rowPixels, colPixels);
		gc = board.getGraphicsContext2D();

		// set background green
		gc.setFill(Color.GREEN);
		gc.fillRect(0, 0, rowPixels, colPixels);

		// set circles clear initially
		gc.setFill(Color.TRANSPARENT);
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				gc.fillOval(getPixels(i), getPixels(j), 20, 20);
			}
		}

		// set the 4 in the center the colors they are supposed to be
		gc.setFill(Color.WHITE);
		gc.fillOval(getPixels(3), getPixels(3), 20, 20);
		gc.fillOval(getPixels(4), getPixels(4), 20, 20);
		gc.setFill(Color.BLACK);
		gc.fillOval(getPixels(3), getPixels(4), 20, 20);
		gc.fillOval(getPixels(4), getPixels(3), 20, 20);

		// lets user move
		// canvasMoves(board);

		// display board
		Group group = new Group();
		group.getChildren().add(board);
		Scene scene = new Scene(group);
		stage.setScene(scene);
		stage.show();

	}

	// given the row/col, calculate pixel location
	private int getPixels(int i) {
		return 8 + 24 * i;
	}

	// given pixel, determine col/row
	private int getRowCol(double pixel) {
		return (int) (pixel - 8) / 24;
	}

	private void canvasMoves(Canvas board) {
		board.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {

				// play game
				String turn = "W";
				if (turn == "W") {
					while (!controller.gameOver()) {
						boolean wValid = false;
						boolean bValid = false;
						int row = 0;
						int col = 0;

						if (turn == "W") {
							// keep letting the user click until the move is legal
							while (!wValid) {
								row = getRowCol(mouse.getY());
								col = getRowCol(mouse.getX());
								wValid = controller.checkValid(row, col, turn, false);
							}
							controller.checkValid(row, col, turn, true);
							controller.move(row, col, turn);
							turn = "B";
						}
						
						// cpu
						if (turn == "B") {
							while (!bValid) {
								row = (int) (Math.random() * dimension);
								col = (int) (Math.random() * dimension);
								bValid = controller.checkValid(row, col, turn, false);
							}
							controller.checkValid(row, col, turn, true);
							controller.move(row, col, turn);
							controller.printScore();
							System.out.println("The computer places a piece at " + intToString(c) + (r + 1));
							System.out.println();
							System.out.println(controller.toString());
							System.out.println();
							turn = "W";
						}
					}
				}

				

			}

		});

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

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

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}