
public class GameLoop<I> {

	public static final int FRAME_DURATION = 16;
	private Game<I> game;
	private InputHandler<I> inputHandler;
	private Timer timer;

	public GameLoop(Game<I> game, InputHandler<I> inputHandler, Timer timer) {
		this.game = game;
		this.inputHandler = inputHandler;
		this.timer = timer;

	}

	public void run() {
		int previousTime = timer.getCurrentTime();
		while (game.isRunning()) {
			int currentTime = timer.getCurrentTime();

			for (int lag = currentTime - previousTime; lag >= FRAME_DURATION; lag -= FRAME_DURATION) {
				game.update(inputHandler.getCurrentInput());

			}
			game.render();
			previousTime = currentTime;
		}

	}

}
