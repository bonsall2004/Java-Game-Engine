package uk.bonsall2004.core.entity;

import com.sun.source.doctree.TextTree;

public class Model {
  private int id;
  private int vertexCount;
  private Material material;

  public Model(int id, int vertexCount) {
    this.id = id;
    this.vertexCount = vertexCount;
    this.material = new Material();
  }

  public Model(int id, int vertexCount, Texture texture) {
    this.id = id;
    this.vertexCount = vertexCount;
    this.material = new Material(texture);
  }

  public Model(Model model, Texture texture) {
    this.id = model.getId();
    this.vertexCount = model.getVertexCount();
    this.material = model.getMaterial();
    this.material.setTexture(texture);
  }

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public Texture getTexture() {
    return material.getTexture();
  }

  public void setTexture(Texture texture) {
    this.material.setTexture(texture);
  }

  public int getId() {
    return id;
  }

  public int getVertexCount() {
    return vertexCount;
  }

  public void setTexture(Texture texture, float reflectance) {
    this.material.setTexture(texture);
    this.material.setReflectance(reflectance);
  }
}
