import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameLoopTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void doesNothingIfGameIsNotRunning() {
		TestGame testGame = new TestGame();
		GameLoop uut = new GameLoop(testGame);
		uut.run();

		assertFalse(testGame.isUpdated);
	}

	@Test
	void invokeOneUpdateIfGameIsRunning() {
		TestGame testGame = new TestGame();
		testGame.setRunning(true);
		GameLoop uut = new GameLoop(testGame);

		uut.run();

		assertTrue(testGame.isUpdated);
	}

	public static class TestGame implements Game {

		public boolean isUpdated;
		private boolean running;

		public void setRunning(boolean running) {
			this.running = running;
		}

		public void update() {
			isUpdated = true;

		}

		public boolean isRunning() {
			return running;
		}

	}

}
