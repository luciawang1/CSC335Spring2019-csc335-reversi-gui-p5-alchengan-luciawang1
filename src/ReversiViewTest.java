import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReversiViewTest {

	@Test
	void test() {
		ReversiController c = new ReversiController();
		assertFalse(c.gameOver());
	}

}
