
public class GameLoop {

	private Game game;

	public GameLoop(Game game) {
		this.game = game;

	}

	public void run() {
		while (game.isRunning())
			game.update();
			game.render();

	}

}
