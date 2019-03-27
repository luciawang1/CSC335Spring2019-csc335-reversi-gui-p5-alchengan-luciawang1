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
 * @author Alan Cheng
 * 
 *         ReversiView Creates Reversi GUI with JavaFX to allow users to
 *         interact with the board
 */
public class ReversiView extends javafx.application.Application implements java.util.Observer {

	/**
	 * ReversiController to allow view to communicate changes to model
	 */
	public ReversiController controller;

	/**
	 * Number of rows/columns
	 */
	public int dimension = 8;

	/**
	 * Graphics context to draw board
	 */
	private GraphicsContext gc;

	/**
	 * Display the score
	 */
	private Label score;

	/**
	 * 8 rows, pieces have 20px radius, 2px insets, border is 2px, edge is 8px
	 */
	private int rowPixels = 384;
	private int colPixels = 384;

	/**
	 * Constructs ReversiView object with new ReversiController object whose model
	 * is an observer. ReversiView needs a no-parameter constructor in order to
	 * launch
	 * 
	 * @param ReversiModel model
	 */
	public ReversiView() {
		controller = new ReversiController();
		controller.model.addObserver(this);
	}

	/**
	 * Generages GUI for Reversi game, allows user to interact with the board
	 * through clicks on the tiles
	 * 
	 * @param stage Stage to display GUI
	 */
	@Override
	public void start(Stage stage) {
		// title
		stage.setTitle("Reversi");
		BorderPane window = new BorderPane();

		// new game
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		Label label = new Label("New Game");
		MenuItem newGame = new CustomMenuItem(label);
		menuFile.getItems().add(newGame);
		menuBar.getMenus().add(menuFile);
		score = new Label(scoreString()); // score on bottom
		Canvas board = new Canvas(rowPixels, colPixels); // game board
		gc = board.getGraphicsContext2D();
		reset(board, stage, label); // reset canvas

		// lets user move
		play(board, stage, label);

		// set up window
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

	/**
	 * Resets visuals of the board (green background, black and white circle pieces
	 * in the middle, transparent circle pieces everywhere else, black lines to
	 * separate squares)
	 * 
	 * @param board Canvas displays and updates board
	 * @param stage Stage displays the canvas
	 * @param label Label to show score
	 */
	private void reset(Canvas board, Stage stage, Label label) {
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

		// ReversiBoard independent of model
		ReversiBoard rb = controller.getModel().getBoard();
		// set colors based on the board
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
		score.setText(scoreString());
	}

	/**
	 * Given the row/col, calculate pixel location
	 * 
	 * @param i Row/Col we want the pixels of
	 * @return corresponding pixels
	 */
	private int getPixels(int i) {
		return 12 + 46 * i;
	}

	/**
	 * Given pixel, determine col/row
	 * 
	 * @param pixel Pixels on the canvas
	 * @return Row/Col of pixel
	 */
	private int getRowCol(double pixel) {
		return (int) (pixel - 9) / 46;
	}

	/**
	 * Starts the game, user goes first. Validates the user's move and updates the
	 * board when a valid move has been made. Alternates turns with CPU, who moves
	 * randomly. Updates score, and terminates game when neither cpu nor user has
	 * any valid moves
	 * 
	 * @param board Canvas object to handle user mouse click events and update GUI
	 * @param stage Stage to display GUI
	 * @param label Label to show score
	 */
	private void play(Canvas board, Stage stage, Label label) {

		// handles user clicks on the board
		board.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			/**
			 * Determine if move at Mouse Click Location is valid, update view/model through
			 * ReversiBoard object if move is valid; keeps track of user/CPU turn, updates
			 * score every turn
			 * 
			 * @param: mouse click MouseEvent
			 */
			public void handle(MouseEvent mouse) {
				if (controller.gameOver()) { // return if game over
					gameOver(board, stage, label);
					return;
				}
				String turn = "W";
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
					gameOver(board, stage, label);
					return;
				}

				// cpu moves randomly
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
				if (controller.gameOver()) { // return if game over
					gameOver(board, stage, label);
					return;
				}
			}
		});

		// clicking exit
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			/**
			 * Handles WindowClosing event and saves the current game to "save_game.dat" by
			 * writing out the serialized ReversiBoard class
			 * 
			 * @param wc WindowEvent
			 */
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

		// allow user to start a new game
		label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			/**
			 * Starts a new game with a new model, resets view, deletes the save_game.dat
			 * file if it exists
			 */
			public void handle(MouseEvent event) {
				try {
					File file = new File("save_game.dat");
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				newGame();
				reset(board, stage, label);
				update(controller.model, controller.model.getBoard());
			}
		});
	}

	/**
	 * Creates new model, adds view as observer, resets ReversiBoard object
	 */
	private void newGame() {
		controller.model = new ReversiModel();
		controller.model.addObserver(this);
		controller.resetBoard();
	}

	/**
	 * Updates view if changes have been made to model
	 * 
	 * @param model  Model that indicates whether changes have been made
	 * @param oBoard ReversiBoard object that contains the most recent move's col,
	 *               row and color
	 */
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
		score.setText(scoreString());
	}

	/**
	 * Builds a string object to display the score underneath the board
	 * 
	 * @return scoreSB StringBuilder that contains the score
	 */
	private String scoreString() {
		StringBuilder scoreSB = new StringBuilder();
		scoreSB.append("White: ");
		scoreSB.append(controller.getWScore());
		scoreSB.append(" - Black: ");
		scoreSB.append(controller.getBScore());

		return scoreSB.toString();
	}

	/**
	 * Determines win/lose/tie, display info through Alert, starts a new game when
	 * user acknowledges Alert by deleting "save_game.dat"
	 * 
	 * @param board Canvas for Reversi
	 * @param stage Stage to display GUI
	 * @param label Label for "New Game"
	 */
	private void gameOver(Canvas board, Stage stage, Label label) {
		if (controller.getBScore() > controller.getWScore())
			new Alert(Alert.AlertType.INFORMATION, "You lose :(").showAndWait();
		else if (controller.getWScore() > controller.getBScore())
			new Alert(Alert.AlertType.INFORMATION, "You win! :)").showAndWait();
		else
			new Alert(Alert.AlertType.INFORMATION, "It's a tie!").showAndWait();
		try {
			File file = new File("save_game.dat");
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		newGame();
		reset(board, stage, label);
		update(controller.model, controller.model.getBoard());
	}
}