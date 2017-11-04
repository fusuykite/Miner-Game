import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

final class Functions
{
   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

   protected static final int COLOR_MASK = 0xffffff;

   protected static final int PROPERTY_KEY = 0;

   protected static final String SMITH_KEY = "blacksmith";
   protected static final int SMITH_NUM_PROPERTIES = 4;
   protected static final int SMITH_ID = 1;
   protected static final int SMITH_COL = 2;
   protected static final int SMITH_ROW = 3;
   protected static final String BGND_KEY = "background";
   protected static final int BGND_NUM_PROPERTIES = 4;
   protected static final int BGND_ID = 1;
   protected static final int BGND_COL = 2;
   protected static final int BGND_ROW = 3;
   protected static final String ORE_KEY = "ore";
   protected static final int ORE_NUM_PROPERTIES = 5;
   protected static final int ORE_ID = 1;
   protected static final int ORE_COL = 2;
   protected static final int ORE_ROW = 3;
   protected static final int ORE_ACTION_PERIOD = 4;
   protected static final String MINER_KEY = "miner";
   protected static final int MINER_NUM_PROPERTIES = 7;
   protected static final int MINER_ID = 1;
   protected static final int MINER_COL = 2;
   protected static final int MINER_ROW = 3;
   protected static final int MINER_LIMIT = 4;
   protected static final int MINER_ACTION_PERIOD = 5;
   protected static final int MINER_ANIMATION_PERIOD = 6;
   protected static final String OBSTACLE_KEY = "obstacle";
   protected static final int OBSTACLE_NUM_PROPERTIES = 4;
   protected static final int OBSTACLE_ID = 1;
   protected static final int OBSTACLE_COL = 2;
   protected static final int OBSTACLE_ROW = 3;


   protected static final String VEIN_KEY = "vein";
   protected static final int VEIN_NUM_PROPERTIES = 5;
   protected static final int VEIN_ID = 1;
   protected static final int VEIN_COL = 2;
   protected static final int VEIN_ROW = 3;
   protected static final int VEIN_ACTION_PERIOD = 4;


   public static void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   public static void load(Scanner in, WorldModel world, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world, imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                  lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
               lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
               lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public static boolean processLine(String line, WorldModel world,
      ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
         case BGND_KEY:
            return parseBackground(properties, world, imageStore);
         case MINER_KEY:
            return parseMiner(properties, world, imageStore);
         case OBSTACLE_KEY:
            return parseObstacle(properties, world, imageStore);
         case ORE_KEY:
            return parseOre(properties, world, imageStore);
         case SMITH_KEY:
            return parseSmith(properties, world, imageStore);
         case VEIN_KEY:
            return parseVein(properties, world, imageStore);
         }
      }

      return false;
   }

   public static boolean parseBackground(String [] properties,
      WorldModel world, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
            Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt, new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public static boolean parseMiner(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == MINER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
            Integer.parseInt(properties[MINER_ROW]));
         Entity entity = MINER_NOT_FULL.createMinerNotFull(properties[MINER_ID],
            Integer.parseInt(properties[MINER_LIMIT]),
            pt,
            Integer.parseInt(properties[MINER_ACTION_PERIOD]),
            Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
            imageStore.getImageList(MINER_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == MINER_NUM_PROPERTIES;
   }

   public static boolean parseObstacle(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
            Integer.parseInt(properties[OBSTACLE_COL]),
            Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = new Obstacle(properties[OBSTACLE_ID],
            pt, imageStore.getImageList(OBSTACLE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public static boolean parseOre(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == ORE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
            Integer.parseInt(properties[ORE_ROW]));
         Entity entity = new ORE(properties[ORE_ID],
            pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
            imageStore.getImageList(ORE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == ORE_NUM_PROPERTIES;
   }

   public static boolean parseSmith(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == SMITH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
            Integer.parseInt(properties[SMITH_ROW]));
         Entity entity = new BlackSmith(properties[SMITH_ID],
            pt, imageStore.getImageList(SMITH_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == SMITH_NUM_PROPERTIES;
   }

   public static boolean parseVein(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == VEIN_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
            Integer.parseInt(properties[VEIN_ROW]));
         Entity entity = new VEIN(properties[VEIN_ID],
            pt,
            Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
            imageStore.getImageList(VEIN_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == VEIN_NUM_PROPERTIES;
   }

   public static int clamp(int value, int low, int high)
   {
      return Math.min(high, Math.max(value, low));
   }
}
