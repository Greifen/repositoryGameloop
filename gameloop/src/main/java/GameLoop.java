
public class GameLoop<I> {

	private Game<I> game;
	private InputHandler<I> inputHandler;

	public GameLoop(Game<I> game, InputHandler<I> inputHandler) {
		this.game = game;
		this.inputHandler = inputHandler;

	}

	public void run() {
		while (game.isRunning())
		{
			game.update(inputHandler.getCurrentInput());
			game.render();

		}

	}

}
