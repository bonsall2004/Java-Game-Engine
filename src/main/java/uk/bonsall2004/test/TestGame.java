package uk.bonsall2004.test;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import uk.bonsall2004.core.*;
import uk.bonsall2004.core.entity.Entity;
import uk.bonsall2004.core.entity.Model;
import uk.bonsall2004.core.entity.Texture;
import uk.bonsall2004.core.lighting.DirectionalLight;
import uk.bonsall2004.core.utils.Consts;
import uk.bonsall2004.launcher.Launcher;

public class TestGame implements ILogic {

  private final RenderManager renderer;
  private final ObjectLoader loader;
  private final WindowManager window;

  private Entity entity;
  private final Camera camera;

  Vector3f cameraInc;

  private float lightAngle;
  private DirectionalLight directionalLight;

  public TestGame() {
    renderer = new RenderManager();
    window = Launcher.getWindow();
    loader = new ObjectLoader();
    camera = new Camera();
    cameraInc = new Vector3f(0, 0, 0);
    lightAngle = -90;
  }

  @Override
  public void init() throws Exception {
    renderer.init();

    Model model = loader.loadOBJModel("/models/bunny.obj");
    model.setTexture(new Texture(loader.loadTexture("textures/bricks.png")), 1f);
    entity = new Entity(model, new Vector3f(1, 0, 0), new Vector3f(0,0,0), 10);

    float lightIntensity = 0.0f;
    Vector3f lightPosition = new Vector3f(-1, -10, 0);
    Vector3f lightColour = new Vector3f(1, 1, 1);
    directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
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
  public void update(MouseInput mouseInput) {
    camera.movePosition(cameraInc.x * Consts.CAMERA_STEP, cameraInc.y * Consts.CAMERA_STEP, cameraInc.z * Consts.CAMERA_STEP);

    if(mouseInput.isRightButtonPress()) {
      Vector2f rotVec = mouseInput.getDisplVec();
      camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);

    }

//    entity.incRotation(0.0f, 0.25f, 0.0f);
    lightAngle += 1.05f;
    if (lightAngle > 90) {
      directionalLight.setIntensity(0);
      if(lightAngle >= 360)
        lightAngle = -90;
    } else if(lightAngle <= -80 || lightAngle >= 80) {
      float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
      directionalLight.setIntensity(factor);
      directionalLight.getColour().y = Math.max(factor, 0.9f);
      directionalLight.getColour().z = Math.max(factor, 0.5f);
    } else {
      directionalLight.setIntensity(1);
      directionalLight.getColour().x = 1;
      directionalLight.getColour().y = 1;
      directionalLight.getColour().z = 1;
    }
    double angRad = Math.toRadians(lightAngle);
    directionalLight.getDirection().x = (float) Math.sin(angRad);
    directionalLight.getDirection().y = (float) Math.cos(angRad);
  }

  @Override
  public void render() {
    if(window.isResize()) {
      GL11.glViewport(0,0, window.getWidth(), window.getHeight());
      window.setResize(true);
    }

    renderer.render(entity, camera, directionalLight);
  }

  @Override
  public void cleanup() {
    renderer.cleanup();
    loader.cleanup();
  }
}
