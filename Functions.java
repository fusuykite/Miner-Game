import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import processing.core.PImage;
import processing.core.PApplet;

final class Functions {
   public static final Random rand = new Random();

   public static final int PROPERTY_KEY = 0;

   public static final int COLOR_MASK = 0xffffff;


   /*
     Called with color for which alpha should be set and alpha value.
     setAlpha(img, color(255, 255, 255), 0));
   */
   public static void setAlpha(PImage img, int maskColor, int alpha) {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++) {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   public static int clamp(int value, int low, int high) {
      return Math.min(high, Math.max(value, low));
   }


   public static List<PImage> getImages(Map<String, List<PImage>> images,
                                        String key) {
      List<PImage> imgs = images.get(key);
      if (imgs == null) {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }
}