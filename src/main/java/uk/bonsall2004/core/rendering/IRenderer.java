package uk.bonsall2004.core.rendering;

import uk.bonsall2004.core.Camera;
import uk.bonsall2004.core.entity.Model;
import uk.bonsall2004.core.lighting.DirectionalLight;
import uk.bonsall2004.core.lighting.PointLight;
import uk.bonsall2004.core.lighting.SpotLight;

public interface IRenderer<T> {

  public void init() throws Exception;
  public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

  abstract void bind(Model model);
  public void unbind();
  public void prepare(T t, Camera camera);
  public void cleanup();
}
