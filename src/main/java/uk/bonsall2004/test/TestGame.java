package uk.bonsall2004.test;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import uk.bonsall2004.core.ILogic;
import uk.bonsall2004.core.ObjectLoader;
import uk.bonsall2004.core.RenderManager;
import uk.bonsall2004.core.WindowManager;
import uk.bonsall2004.core.entity.Model;
import uk.bonsall2004.core.entity.Texture;
import uk.bonsall2004.launcher.Launcher;

public class TestGame implements ILogic {
  private int direction = 0;
  private float colour = 0f;

  private final RenderManager renderer;
  private final ObjectLoader loader;
  private final WindowManager window;

  private Model model;

  public TestGame() {
    renderer = new RenderManager();
    window = Launcher.getWindow();
    loader = new ObjectLoader();
  }

  @Override
  public void init() throws Exception {
    renderer.init();

    float[] vertices = {
            -0.5f,  0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f,  0.5f, 0f,
    };

    int[] indices = {
            0, 1, 3,
            3, 1, 2
    };

    float[] textureCoord = {
            0, 0,
            0, 1,
            1, 1,
            1, 0
    };
    model = loader.loadModel(vertices, textureCoord, indices);
    model.setTexture(new Texture(loader.loadTexture("textures/bricks.png")));
  }

  @Override
  public void input() {
    if(window.isKeyPressed(GLFW.GLFW_KEY_W))
      direction = 1;
    else if(window.isKeyPressed(GLFW.GLFW_KEY_S))
      direction = -1;
  }

  @Override
  public void update() {
    colour += direction * 0.05f;
    if(colour > 1)
      colour = 1f;
    else if (colour < 0)
      colour = 0f;
  }

  @Override
  public void render() {
    if(window.isResize()) {
      GL11.glViewport(0,0, window.getWidth(), window.getHeight());
      window.setResize(true);
    }

    window.setClearColour(colour, colour, colour, 0f);
    renderer.render(model);
  }

  @Override
  public void cleanup() {
    renderer.cleanup();
    loader.cleanup();
  }
}
