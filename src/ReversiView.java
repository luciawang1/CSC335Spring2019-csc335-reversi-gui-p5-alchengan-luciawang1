import java.io.*;
import java.util.Observable;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

	// 46 * 8 + 16
	private int rowPixels = 384;
	private int colPixels = 384;

	/**
	 * Constructor
	 * 
	 * @param ReversiModel model
	 */
	public ReversiView() {
		controller = new ReversiController();
		controller.model.addObserver(this);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Reversi");
		BorderPane window = new BorderPane();

		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		Label l = new Label("New Game");
		MenuItem newGame = new CustomMenuItem(l);
		l.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				Stage stage2 = new Stage();
				try {
					start(stage2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("can't restart");
				}
			}
		});

		menuFile.getItems().add(newGame);
		menuBar.getMenus().add(menuFile);

		Label score = new Label(scoreString());

		Canvas board = new Canvas(rowPixels, colPixels);
		gc = board.getGraphicsContext2D();

		// set background green
		gc.setFill(Color.GREEN);
		gc.fillRect(0, 0, rowPixels, colPixels);

		// set grid
		gc.setFill(Color.BLACK);
		// horizontal
		for (int y = 9; y < rowPixels; y += 46) {
			gc.setLineWidth(2);
			gc.strokeLine(9, y, 377, y);
		}
		// vertical
		for (int x = 9; x < colPixels; x += 46) {
			gc.strokeLine(x, 9, x, 377);
		}

		// set circles clear initially
		gc.setFill(Color.TRANSPARENT);
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				gc.fillOval(getPixels(i), getPixels(j), 40, 40);
			}
		}

		// set the 4 in the center the colors they are supposed to be
		gc.setFill(Color.WHITE);
		gc.fillOval(getPixels(3), getPixels(3), 40, 40);
		gc.fillOval(getPixels(4), getPixels(4), 40, 40);
		gc.setFill(Color.BLACK);
		gc.fillOval(getPixels(3), getPixels(4), 40, 40);
		gc.fillOval(getPixels(4), getPixels(3), 40, 40);

		// lets user move
		play(board, score, stage);

		window.setTop(menuBar);
		window.setCenter(board);
		window.setBottom(score);

		// display board
		Group group = new Group();
		group.getChildren().add(window);
		Scene scene = new Scene(group);
		stage.setScene(scene);
		stage.show();

	}

	// given the row/col, calculate pixel location
	private int getPixels(int i) {
		return 12 + 46 * i;
	}

	// given pixel, determine col/row
	private int getRowCol(double pixel) {
		return (int) (pixel - 9) / 46;
	}

	private void play(Canvas board, Label score, Stage stage) {

		// set on mouse clicked
		board.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				if (!controller.hasValidMoves("W") && !controller.hasValidMoves("B")) { // return if game over
					gameOver();
					return;
				}

				// play
				String turn = "W";
				boolean wValid = false;
				boolean bValid = false;
				int row = 0;
				int col = 0;

				// user
				if (turn == "W" && controller.hasValidMoves("W")) {
					// keep letting the user click until the move is legal
					row = getRowCol(mouse.getX());
					col = getRowCol(mouse.getY());
					if (row >= 0 && row < 8 && col >= 0 && col < 8) {
						if (controller.checkValid(row, col, turn, false)) {
							controller.checkValid(row, col, turn, true);
							controller.move(row, col, turn);
							turn = "B";
						}
					}
				}
				score.setText(scoreString());
				if (!controller.hasValidMoves("W") && !controller.hasValidMoves("B")) { // return if game over
					gameOver();
					return;
				}

				// cpu
				if (turn == "B" && controller.hasValidMoves("B")) {
					while (!bValid) {
						row = (int) (Math.random() * dimension);
						col = (int) (Math.random() * dimension);
						bValid = controller.checkValid(row, col, turn, false);
					}
					controller.checkValid(row, col, turn, true);
					controller.move(row, col, turn);
				}
				score.setText(scoreString());

				// check if game over
				if (!controller.hasValidMoves("W") && !controller.hasValidMoves("B")) { // return if game over
					gameOver();
					return;
				}
			}
		});

		// clicking exit
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent wc) {
				try {
					FileOutputStream save = new FileOutputStream("save_game.dat");
					ObjectOutputStream out = new ObjectOutputStream(save);
					out.writeObject(controller.getModel().getBoard());
					out.close();
					save.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void update(Observable model, Object oBoard) {
		ReversiBoard rb = (ReversiBoard) oBoard;
		for (int i = 0; i < ReversiBoard.DIM; i++) {
			for (int j = 0; j < ReversiBoard.DIM; j++) {
				if (rb.getAt(i, j) == ReversiBoard.BLANK)
					gc.setFill(Color.TRANSPARENT);
				else if (rb.getAt(i, j) == ReversiBoard.WHITE)
					gc.setFill(Color.WHITE);
				else if (rb.getAt(i, j) == ReversiBoard.BLACK)
					gc.setFill(Color.BLACK);

				gc.fillOval(this.getPixels(i), this.getPixels(j), 40, 40);
			}
		}
		// gc.setFill(rb.getColor());
		// gc.fillOval(this.getPixels(rb.getRow()), this.getPixels(rb.getCol()), 20,
		// 20);
	}

	private String scoreString() {
		StringBuilder scoreSB = new StringBuilder();
		scoreSB.append("White: ");
		scoreSB.append(controller.getWScore());
		scoreSB.append(" - Black: ");
		scoreSB.append(controller.getBScore());

		return scoreSB.toString();
	}

	private void gameOver() {
		if (controller.getBScore() > controller.getWScore())
			new Alert(Alert.AlertType.INFORMATION, "You lose :(").showAndWait();
		else if (controller.getWScore() > controller.getBScore())
			new Alert(Alert.AlertType.INFORMATION, "You win! :)").showAndWait();
		else
			new Alert(Alert.AlertType.INFORMATION, "It's a tie!").showAndWait();
	}

}