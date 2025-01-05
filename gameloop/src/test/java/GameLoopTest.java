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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class GameLoopTest {

	@Mock
	private Game<TestInput> testGame;
	
	@Mock
	private InputHandler<TestInput> testInputHandler;
	
	@Mock
	private Timer testTimer;
	private int time;
	
	@InjectMocks
	private GameLoop<TestInput> uut;

	@BeforeEach
	public void setUp() throws Exception {
		time=0;
		when(testTimer.getCurrentTime()).then(i->{
			int oldTime=time;
			time+=GameLoop.FRAME_DURATION;
			return oldTime;
		});
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

	@Test
	public void doesExecuteUpdateEventually() {
		when(testGame.isRunning()).thenReturn(true,true,true,false);
		int halfFrameDuration = (int)(GameLoop.FRAME_DURATION/2);
        when(testTimer.getCurrentTime()).thenReturn(0,halfFrameDuration,2*halfFrameDuration,3*halfFrameDuration);
        
        uut.run();
		
		verify(testGame, times(1)).update(any());
	}
	
	public static class TestInput {

	}

}
