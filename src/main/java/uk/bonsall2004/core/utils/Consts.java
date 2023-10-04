package uk.bonsall2004.core.utils;

import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Consts {
  public static final String TITLE = "IM GUNNA CUMM!";

  public static final float FOV = (float) Math.toRadians(60);
  public static final float Z_NEAR = 0.01f;
  public static final float Z_FAR = 1000f;

  public static final float MOUSE_SENSITIVITY = 0.2f;
  public static final float CAMERA_STEP = 0.1f;
  public static final float SPECULAR_POWER = 10f;

  public static final Vector4f DEFAULT_COLOUR = new Vector4f(1f, 1f, 1f, 1f);
  public static final Vector3f AMBIENT_LIGHT = new Vector3f(0.3f, 0.3f, 0.3f);


}
