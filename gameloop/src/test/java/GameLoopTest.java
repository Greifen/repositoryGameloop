import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Arrays;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameLoopTest {

	private TestGame testGame;
	private GameLoop uut;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	public void setUpGameLoop() throws Exception {
		testGame = new TestGame();
		uut = new GameLoop(testGame);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void doesNothingIfGameIsNotRunning() {
		testGame.setRunning(false);
		uut.run();
		assertThat(testGame.numberOfUpdates, is(0));
	}

	@Test
	void invokesOneUpdateIfGameIsRunning() {
		testGame.setRunning(true,false);
		uut.run();
		assertThat(testGame.numberOfUpdates, is(1));
	}
	
	@Test
	void invokesUpdateAsLongAsGameIsRunnig() {
		testGame.setRunning(true, true, true, false);
		uut.run();
		assertThat(testGame.numberOfUpdates, is(3));
	}
	
	@Test
	void invokesRenderAfterUpdate() {
		testGame.setRunning(true, false);
		uut.run();
		assertThat(testGame.numberOfRenders, is(1));
	}

	public static class TestGame implements Game {

		public int numberOfRenders;
		public int numberOfUpdates;
		private Queue<Boolean> running;

		public void setRunning(Boolean... runningValues) {
			List<Boolean> values = Arrays.asList(runningValues);
			this.running = new LinkedList<Boolean>(values);;
		}

		public void update() {
			numberOfUpdates++;
		}

		public boolean isRunning() {
			return running.poll();
		}


		public void render() {
			if(numberOfRenders!=numberOfUpdates-1) 
				throw new RenderBeforeUpdateException();
			numberOfRenders++;
			
			
			
		}

	}

}
