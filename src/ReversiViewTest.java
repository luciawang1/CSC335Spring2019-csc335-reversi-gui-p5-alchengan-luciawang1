import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReversiViewTest {

	@Test
	void test() {
		ReversiController c = new ReversiController();
		ReversiModel m = new ReversiModel();
		c.model = m;
		c.resetBoard();
		ReversiBoard b = m.getBoard();
		
		assertFalse(c.gameOver());
		assertEquals(b.getAt(3, 3), 1);
		
		assertFalse(c.checkValid(3, 2, "W", true));
		assertTrue(c.checkValid(3, 2, "B", true));
		c.move(3, 2, "B");
		assertFalse(c.checkValid(3, 3, "W", true));
		
		assertEquals(c.getWScore(), 1);
		assertEquals(c.getBScore(), 4);
		
		assertTrue(c.checkValid(2, 2, "W", true));
		c.move(2, 2, "W");
		assertTrue(c.checkValid(2, 3, "B", true));
		c.move(2, 3, "B");
		assertTrue(c.checkValid(4, 2, "W", true));
		c.move(4, 2, "W");
		assertTrue(c.checkValid(2, 4, "W", true));
		c.move(2, 4, "W");
		
		assertTrue(c.gameOver());
		
		for(int i=0; i<ReversiBoard.DIM; i++) {
			for(int j=0; j<ReversiBoard.DIM; j++) {
				c.move(i, j, "B");
			}
		}
		
		assertTrue(c.gameOver());
	}

}
