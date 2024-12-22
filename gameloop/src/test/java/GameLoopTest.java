import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Matchers.any;
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
import org.mockito.InOrder;


class GameLoopTest {


	private Game<TestInput> testGame;
	private InputHandler<TestInput> testInputHandler;
	private Timer testTimer;
	private GameLoop<TestInput> uut;

	@BeforeEach
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		testGame = mock(Game.class);
		testInputHandler = mock(InputHandler.class);
		testTimer = mock(Timer.class);
		when(testTimer.getCurrentTime()).thenReturn(0,GameLoop.FRAME_DURATION, 2*GameLoop.FRAME_DURATION, 3*GameLoop.FRAME_DURATION);
		uut = new GameLoop<TestInput>(testGame, testInputHandler, testTimer);

	}


	@Test
	void doesNothingIfGameIsNotRunning() {
		when(testGame.isRunning()).thenReturn(false);

		uut.run();
		
		verify(testGame, never()).update(any());
	}


	@Test
	void invokesOneUpdateIfGameIsRunning() {
		when(testGame.isRunning()).thenReturn(true, false);

		uut.run();
		
		verify(testGame).update(any());
	}

	@Test
	void invokesUpdateAsLongAsGameIsRunnig() {
		when(testGame.isRunning()).thenReturn(true, true, true, false);

		uut.run();
		
		verify(testGame, times(3)).update(any());
	}

	@Test
	void invokesRenderAfterUpdate() {
		when(testGame.isRunning()).thenReturn(true, false);

		uut.run();
		
		InOrder inOrder = inOrder(testGame);
		inOrder.verify(testGame).update(any());
		inOrder.verify(testGame).render();
	}

	@Test
	void passesInputToUpdate() {
		TestInput testInput = new TestInput();
		when(testInputHandler.getCurrentInput()).thenReturn(testInput);
		when(testGame.isRunning()).thenReturn(true, false);
		
		uut.run();
		
		verify(testGame).update(testInput);
	}
	

	@Test
	void skipUpdateIfLoopIsTooFast() {
		when(testGame.isRunning()).thenReturn(true, false);
		when(testTimer.getCurrentTime()).thenReturn(0,1);
		
		uut.run();
		
		verify(testGame, never()).update(any());
		verify(testGame, times(1)).render();
	}

	@Test
	void doesAdditionalUpdateIfLoopIsTooSlow() {
		when(testGame.isRunning()).thenReturn(true, false);
		when(testTimer.getCurrentTime()).thenReturn(0,2 * GameLoop.FRAME_DURATION);
		
		uut.run();
		
		verify(testGame, times(2)).update(any());
		verify(testGame, times(1)).render();
	}

	public static class TestInput {

	}

}
