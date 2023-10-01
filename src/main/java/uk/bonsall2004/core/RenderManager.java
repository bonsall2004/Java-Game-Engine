package uk.bonsall2004.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import uk.bonsall2004.core.entity.Entity;
import uk.bonsall2004.core.lighting.DirectionalLight;
import uk.bonsall2004.core.lighting.PointLight;
import uk.bonsall2004.core.lighting.SpotLight;
import uk.bonsall2004.core.utils.Consts;
import uk.bonsall2004.core.utils.Transformation;
import uk.bonsall2004.core.utils.Utils;
import uk.bonsall2004.launcher.Launcher;

public class RenderManager {
  private final WindowManager window = Launcher.getWindow();
  private ShaderManager shader;
  public RenderManager() {
    return;
  }

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
    shader.createPointLightUniform("pointLight");
//    shader.createSpotLightUniform("spotLight");
  }

  public void render(Entity entity, Camera camera, DirectionalLight directionalLight, PointLight pointLight, SpotLight spotLight) {
    clear();
    shader.bind();
    shader.setUniform("textureSampler", 0);
    shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
    shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
    shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    shader.setUniform("material", entity.getModel().getMaterial());
    shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
    shader.setUniform("specularPower", Consts.SPECULAR_POWER);
    shader.setUniform("directionalLight", directionalLight);
    shader.setUniform("pointLight", pointLight);
//    shader.setUniform("spotLight", spotLight);
    GL30.glBindVertexArray(entity.getModel().getId());
    GL20.glEnableVertexAttribArray(0);
    GL20.glEnableVertexAttribArray(1);
    GL20.glEnableVertexAttribArray(2);
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
    GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL20.glDisableVertexAttribArray(2);
    GL30.glBindVertexArray(0);
    shader.unbind();
  }

  public void clear() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
  }

  public void cleanup() {
    shader.cleanup();
  }
}
