package uk.bonsall2004.core.utils;

import org.lwjgl.system.MemoryUtil;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {
  public static FloatBuffer storeDataInFloatBuffer(float[] data) {
    FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
    buffer.put(data).flip();
    return buffer;
  }

  public static IntBuffer storeDataInIntBuffer(int[] data) {
    IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
    buffer.put(data).flip();
    return buffer;
  }

  public static String loadResource(String filename) throws Exception {
    String result;
    try(InputStream in = Utils.class.getResourceAsStream(filename); Scanner scanner = new Scanner(in, StandardCharsets.UTF_8)) {
      result = scanner.useDelimiter("\\A").next();
    }

    return result;
  }
}
