package uk.bonsall2004.test;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import uk.bonsall2004.core.*;
import uk.bonsall2004.core.entity.Entity;
import uk.bonsall2004.core.entity.Model;
import uk.bonsall2004.core.entity.Texture;
import uk.bonsall2004.launcher.Launcher;

public class TestGame implements ILogic {

  private static final float CAMERA_MOVE_SPEED = 0.05f;

  private final RenderManager renderer;
  private final ObjectLoader loader;
  private final WindowManager window;

  private Entity entity;
  private final Camera camera;

  Vector3f cameraInc;

  public TestGame() {
    renderer = new RenderManager();
    window = Launcher.getWindow();
    loader = new ObjectLoader();
    camera = new Camera();
    cameraInc = new Vector3f(0, 0, 0);
  }

  @Override
  public void init() throws Exception {
    renderer.init();

    float[] vertices = new float[] {
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
    };
    float[] textureCoord = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.0f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
    };
    int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,
            8, 10, 11, 9, 8, 11,
            12, 13, 7, 5, 12, 7,
            14, 15, 6, 4, 14, 6,
            16, 18, 19, 17, 16, 19,
            4, 6, 7, 5, 4, 7,
    };

    Model model = loader.loadModel(vertices, textureCoord, indices);
    model.setTexture(new Texture(loader.loadTexture("textures/bricks.png")));
    entity = new Entity(model, new Vector3f(1, 0, 0), new Vector3f(0,0,0), 1);
  }

  @Override
  public void input() {
    cameraInc.set(0 ,0, 0);
    if(window.isKeyPressed(GLFW.GLFW_KEY_W))
      cameraInc.z = -1;
    if(window.isKeyPressed(GLFW.GLFW_KEY_S))
      cameraInc.z = 1;

    if(window.isKeyPressed(GLFW.GLFW_KEY_A))
      cameraInc.x = -1;
    if(window.isKeyPressed(GLFW.GLFW_KEY_D))
      cameraInc.x = 1;

    if(window.isKeyPressed(GLFW.GLFW_KEY_Z))
      cameraInc.y = -1;
    if(window.isKeyPressed(GLFW.GLFW_KEY_X))
      cameraInc.y = 1;
  }

  @Override
  public void update() {
    System.out.println(cameraInc);
    camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);

    entity.incRotation(0.0f, 0.5f, 0.0f);
  }

  @Override
  public void render() {
    if(window.isResize()) {
      GL11.glViewport(0,0, window.getWidth(), window.getHeight());
      window.setResize(true);
    }

    window.setClearColour(0f, 0f, 0f, 0f);
    renderer.render(entity, camera);
  }

  @Override
  public void cleanup() {
    renderer.cleanup();
    loader.cleanup();
  }
}
