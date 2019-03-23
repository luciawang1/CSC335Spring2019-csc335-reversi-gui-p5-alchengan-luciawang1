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
		play(board);

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

	private void play(Canvas board) {
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
							turn = "W";
						}
					}
				}
			}
		});
	}

	public void update(Observable model, Object move) {
		// TODO Auto-generated method stub
		ReversiBoard2 rb = (ReversiBoard2) move;
		gc.setFill(rb.getColor());
		gc.fillOval(this.getPixels(rb.getRow()), this.getPixels(rb.getCol()), 20, 20);

	}

}