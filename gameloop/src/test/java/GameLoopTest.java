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




	@BeforeEach
	public void setUpGameLoop() throws Exception {

	}


	@Test
	void doesNothingIfGameIsNotRunning() {
		TestGame testGame = new TestGame();
		TestInputHandler testInputHandler = new TestInputHandler();
		GameLoop uut = new GameLoop(testGame, testInputHandler);
		
		testGame.setRunning(false);
		uut.run();
		assertThat(testGame.numberOfUpdates, is(0));
	}

	@Test
	void invokesOneUpdateIfGameIsRunning() {
		TestGame testGame = new TestGame();
		TestInputHandler testInputHandler = new TestInputHandler();
		GameLoop uut = new GameLoop(testGame, testInputHandler);
		
		testGame.setRunning(true,false);
		uut.run();
		assertThat(testGame.numberOfUpdates, is(1));
	}
	
	@Test
	void invokesUpdateAsLongAsGameIsRunnig() {
		TestGame testGame = new TestGame();
		TestInputHandler testInputHandler = new TestInputHandler();
		GameLoop uut = new GameLoop(testGame, testInputHandler);
		
		testGame.setRunning(true, true, true, false);
		uut.run();
		assertThat(testGame.numberOfUpdates, is(3));
	}
	
	@Test
	void invokesRenderAfterUpdate() {
		TestGame testGame = new TestGame() {
			@Override
			public void render() {
				assertEquals(numberOfRenders, numberOfUpdates-1);
				super.render();
			}
		};
		TestInputHandler testInputHandler = new TestInputHandler();
		GameLoop uut = new GameLoop(testGame, testInputHandler);
		
		testGame.setRunning(true, false);
		uut.run();
		assertThat(testGame.numberOfRenders, is(1));
	}

	@Test
	void passesInputToUpdate() {
		TestGame testGame = new TestGame();
		TestInputHandler testInputHandler = new TestInputHandler();
		GameLoop uut = new GameLoop(testGame, testInputHandler);
		
		TestInput testInput = new TestInput();
		testInputHandler.setInput(testInput);
		testGame.setRunning(true,false);
		uut.run();
		assertThat(testGame.updatedWithInput, is(testInput));
	}
	
	public static class TestInput{
		
	}
	
	public static class TestInputHandler implements InputHandler<TestInput> {
		private GameLoopTest.TestInput testInput;

		public void setInput(TestInput testInput) {
			this.testInput = testInput;
			
		}
		
		@Override
		public GameLoopTest.TestInput getCurrentInput() {
			return testInput;
		}

	}
	
	public static class TestGame implements Game<TestInput> {

		public TestInput updatedWithInput;
		public int numberOfRenders;
		public int numberOfUpdates;
		private Queue<Boolean> running;

		public void setRunning(Boolean... runningValues) {
			List<Boolean> values = Arrays.asList(runningValues);
			this.running = new LinkedList<Boolean>(values);;
		}

		public void update(TestInput input) {
			numberOfUpdates++;
			updatedWithInput = input;
		}

		public boolean isRunning() {
			return running.poll();
		}


		public void render() {

			numberOfRenders++;
		}

	}

}
