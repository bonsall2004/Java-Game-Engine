package uk.bonsall2004.core.rendering;

import org.lwjgl.opengl.GL11;
import uk.bonsall2004.core.Camera;
import uk.bonsall2004.core.ShaderManager;
import uk.bonsall2004.core.WindowManager;
import uk.bonsall2004.core.entity.Entity;
import uk.bonsall2004.core.entity.terrain.Terrain;
import uk.bonsall2004.core.lighting.DirectionalLight;
import uk.bonsall2004.core.lighting.PointLight;
import uk.bonsall2004.core.lighting.SpotLight;
import uk.bonsall2004.core.utils.Consts;
import uk.bonsall2004.launcher.Launcher;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {
  private final WindowManager window = Launcher.getWindow();
  private EntityRenderer entityRenderer;
  private TerrainRenderer terrainRenderer;
  public RenderManager() {
    return;
  }

  public void init() throws Exception {
    entityRenderer = new EntityRenderer();
    terrainRenderer = new TerrainRenderer();
    entityRenderer.init();
    terrainRenderer.init();
  }

  public static void renderLights(PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight, ShaderManager shader) {
    shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
    shader.setUniform("specularPower", Consts.SPECULAR_POWER);
    shader.setUniform("pointLights", pointLights);
    shader.setUniform("spotLights", spotLights);
    shader.setUniform("directionalLight", directionalLight);
  }

  public void render(Camera camera, DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights) {
    clear();

    entityRenderer.render(camera, pointLights, spotLights, directionalLight);
    terrainRenderer.render(camera, pointLights, spotLights, directionalLight);
  }

  public void processEntity(Entity entity) {
    List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
    if(entityList != null) {
      entityList.add(entity);
    } else {
      List<Entity> newEntityList = new ArrayList<>();
      newEntityList.add(entity);
      entityRenderer.getEntities().put(entity.getModel(), newEntityList);
    }
  }

  public void processTerrain(Terrain terrain) {
    terrainRenderer.getTerrains().add(terrain);
  }

  public void clear() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
  }

  public void cleanup() {
    entityRenderer.cleanup();
    terrainRenderer.cleanup();
  }
}
