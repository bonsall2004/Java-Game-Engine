package uk.bonsall2004.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import uk.bonsall2004.core.entity.Entity;
import uk.bonsall2004.core.entity.Model;
import uk.bonsall2004.core.lighting.DirectionalLight;
import uk.bonsall2004.core.lighting.PointLight;
import uk.bonsall2004.core.lighting.SpotLight;
import uk.bonsall2004.core.utils.Consts;
import uk.bonsall2004.core.utils.Transformation;
import uk.bonsall2004.core.utils.Utils;
import uk.bonsall2004.launcher.Launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderManager {
  private final WindowManager window = Launcher.getWindow();
  private ShaderManager shader;
  public RenderManager() {
    return;
  }

  private Map<Model, List<Entity>> entities = new HashMap<>();

  public void init() throws Exception {
    shader = new ShaderManager();
    shader.createVertexShader(Utils.loadResource("/shaders/vertex.vert"));
    shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fsh"));
    shader.link();
    shader.createUniform("textureSampler");
    shader.createUniform("transformationMatrix");
    shader.createUniform("projectionMatrix");
    shader.createUniform("viewMatrix");
    shader.createUniform("ambientLight");
    shader.createMaterialUniform("material");
    shader.createUniform("specularPower");
    shader.createDirectionalLightUniform("directionalLight");
    shader.createPointLightListUniform("pointLights", 5);
    shader.createSpotLightListUniform("spotLights", 5);
  }

  public void bind(Model model) {
    GL30.glBindVertexArray(model.getId());
    GL20.glEnableVertexAttribArray(0);
    GL20.glEnableVertexAttribArray(1);
    GL20.glEnableVertexAttribArray(2);
    shader.setUniform("material", model.getMaterial());
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
  }

  public void unbind() {
    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL20.glDisableVertexAttribArray(2);
    GL30.glBindVertexArray(0);
  }

  public void prepare(Entity entity, Camera camera) {
    shader.setUniform("textureSampler", 0);
    shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
    shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
  }

  public void renderLights(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
    shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
    shader.setUniform("specularPower", Consts.SPECULAR_POWER);
    shader.setUniform("pointLights", pointLights);
    shader.setUniform("spotLights", spotLights);
    shader.setUniform("directionalLight", directionalLight);
  }

  public void render(Camera camera, DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights) {
    clear();

    shader.bind();
    shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
    renderLights(camera, pointLights, spotLights, directionalLight);

    for(Model model :entities.keySet()) {
      bind(model);
      List<Entity> entityList = entities.get(model);
      for(Entity entity : entityList) {
        prepare(entity, camera);
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
      }
      unbind();
    }
    entities.clear();
    shader.unbind();
  }

  public void processEntity(Entity entity) {
    List<Entity> entityList = entities.get(entity.getModel());
    if(entityList != null) {
      entityList.add(entity);
    } else {
      List<Entity> newEntityList = new ArrayList<>();
      newEntityList.add(entity);
      entities.put(entity.getModel(), newEntityList);
    }
  }

  public void clear() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
  }

  public void cleanup() {
    shader.cleanup();
  }
}
