package uk.bonsall2004.test;

import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import uk.bonsall2004.core.*;
import uk.bonsall2004.core.entity.Entity;
import uk.bonsall2004.core.entity.Material;
import uk.bonsall2004.core.entity.Model;
import uk.bonsall2004.core.entity.Texture;
import uk.bonsall2004.core.entity.terrain.Terrain;
import uk.bonsall2004.core.lighting.DirectionalLight;
import uk.bonsall2004.core.lighting.PointLight;
import uk.bonsall2004.core.lighting.SpotLight;
import uk.bonsall2004.core.rendering.RenderManager;
import uk.bonsall2004.core.utils.Consts;
import uk.bonsall2004.launcher.Launcher;

import java.util.ArrayList;
import java.util.List;

public class TestGame implements ILogic {

  private final RenderManager renderer;
  private final ObjectLoader loader;
  private final WindowManager window;

  private List<Entity> entities;
  private List<Terrain> terrains;
  private final Camera camera;

  Vector3f cameraInc;

  private float lightAngle;
  private DirectionalLight directionalLight;
  private PointLight[] pointLights;
  private SpotLight[] spotLights;

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
    Model model = loader.loadOBJModel("/models/cube.obj");
    model.setTexture(new Texture(loader.loadTexture("textures/sand.png")));

    terrains = new ArrayList<>();
    Terrain terrain = new Terrain(new Vector3f(0, -1, -800), loader, new Material(new Texture(loader.loadTexture("textures/sand.png")), 0.1f));
    terrains.add(terrain);

    entities = new ArrayList<>();
    Random rnd = new Random();
    for(int i = 0; i < 10; i++) {
      float x = rnd.nextFloat() * 100 - 50;
      float y = rnd.nextFloat() * 100 - 50;
      float z = rnd.nextFloat() * -300;
      entities.add(new Entity(model, new Vector3f(x, y, z), new Vector3f(rnd.nextFloat() * 180, rnd.nextFloat() * 180, 0f), 1));
    }
    entities.add(new Entity(model, new Vector3f(0 ,0, -2f), new Vector3f(0,0,0), 1));

    float lightIntensity = 1.0f;
    Vector3f lightPosition = new Vector3f(0f, 0f, -3.2f);
    Vector3f lightColour = new Vector3f(1f, 0f, 1f);
    PointLight pointLight = new PointLight(lightColour, lightPosition, 0f);
    lightPosition = new Vector3f(0f, 2f, 0f);
    PointLight pointLight1 = new PointLight(lightColour, lightPosition, 0f);

    Vector3f conedir = new Vector3f(-1, 5, 5);
    float cutoff = (float) Math.cos(Math.toRadians(180));
    SpotLight spotLight = new SpotLight(new PointLight(lightColour, new Vector3f(0, 0, -5f), 0f, 0, 0, 1), conedir, cutoff);

    conedir = new Vector3f(-1, 5, 5);
    SpotLight spotLight1 = new SpotLight(new PointLight(new Vector3f(0, 0, 0), new Vector3f(0, 0, -5f), 0f, 3, 9, 1), conedir, cutoff);


    lightIntensity = 0f;
    lightPosition = new Vector3f(0, 10, 0);
    lightColour = new Vector3f(1, 1, 1);
    directionalLight = new DirectionalLight(new Vector3f(1,1,1), lightPosition, 2);

    pointLights = new PointLight[] {
            pointLight
    };

    spotLights = new SpotLight[] {
            spotLight,
            spotLight1
    };
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

    if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
      cameraInc.y = -1;
    if(window.isKeyPressed(GLFW.GLFW_KEY_E))
      cameraInc.y = 1;

    if(window.isKeyPressed(GLFW.GLFW_KEY_O))
      spotLights[0].getPointLight().getPosition().x -= 0.01f;
    if(window.isKeyPressed(GLFW.GLFW_KEY_P))
      spotLights[0].getPointLight().getPosition().x += 0.01f;
  }

  @Override
  public void update(MouseInput mouseInput) {
    camera.movePosition(cameraInc.x * Consts.CAMERA_STEP, cameraInc.y * Consts.CAMERA_STEP, cameraInc.z * Consts.CAMERA_STEP);

    if(mouseInput.isRightButtonPress()) {
      Vector2f rotVec = mouseInput.getDisplVec();
      camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);

    }

    lightAngle += 1.05f;
    if (lightAngle > 90) {
      directionalLight.setIntensity(0);
      if(lightAngle >= 360)
        lightAngle = -90;
    } else if(lightAngle <= -80 || lightAngle >= 80) {
      float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
      directionalLight.setIntensity(factor+2);
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

    for(Entity entity : entities) {
      renderer.processEntity(entity);
    }

    for(Terrain terrain : terrains) {
      renderer.processTerrain(terrain);
    }
  }

  @Override
  public void render() {
    if(window.isResize()) {
      GL11.glViewport(0,0, window.getWidth(), window.getHeight());
      window.setResize(true);
    }

    renderer.render(camera, directionalLight, pointLights, spotLights);
  }

  @Override
  public void cleanup() {
    renderer.cleanup();
    loader.cleanup();
  }
}