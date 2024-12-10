
public class GameLoop {

	private Game game;

	public GameLoop(Game game) {
		this.game = game;

	}

	public void run() {
		if (game.isRunning())
			game.update();

	}

}
