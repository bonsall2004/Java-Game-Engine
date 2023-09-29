package uk.bonsall2004.launcher;

import uk.bonsall2004.core.WindowManager;
import uk.bonsall2004.core.EngineManager;
import uk.bonsall2004.core.utils.Consts;
import uk.bonsall2004.test.TestGame;

public class Launcher {

  private static WindowManager window;
  private static TestGame game;

  public static WindowManager getWindow() {
    return window;
  }

  public static TestGame getGame() {
    return game;
  }

  public static void main(String[] args) {
    window = new WindowManager(Consts.TITLE, 800, 600, false);
    game = new TestGame();
    EngineManager engine = new EngineManager();

    try {
      engine.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
