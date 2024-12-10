import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Arrays;


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
		testGame.setRunning(false);
		uut.run();

		assertFalse(testGame.isUpdated); 
	}

	@Test
	void invokesOneUpdateIfGameIsRunning() {
		TestGame testGame = new TestGame();
		testGame.setRunning(true,false);
		GameLoop uut = new GameLoop(testGame);

		uut.run();

		assertTrue(testGame.isUpdated);
	}
	
	@Test
	void invokesUpdateAsLongAsGameIsRunnig() {
		TestGame testGame = new TestGame();
		testGame.setRunning(true, true, true, false);
		GameLoop uut = new GameLoop(testGame);

		uut.run();
		
		assertThat(testGame.numberOfUpdates, is(3));
	}

	public static class TestGame implements Game {

		public int numberOfUpdates;
		public boolean isUpdated;
		private Queue<Boolean> running;

		public void setRunning(Boolean... runningValues) {
			List<Boolean> values = Arrays.asList(runningValues);
			this.running = new LinkedList<Boolean>(values);;
		}

		public void update() {
			isUpdated = true;
			numberOfUpdates++;
		}

		public boolean isRunning() {
			return running.poll();
		}

	}

}
