import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import processing.core.*;
import java.util.ArrayList;
import java.util.List;

public final class VirtualWorld
   extends PApplet {
   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "gaia.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;


   public void settings() {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup() {
      this.imageStore = new ImageStore(
              createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
              createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
              TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduler.scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw() {
      long time = System.currentTimeMillis();
      if (time >= next_time) {
         EventScheduler.updateOnTime(this.scheduler, time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed() {


      if(key ==CODED)

   {
      int dx = 0;
      int dy = 0;

      switch (keyCode) {
         case UP:
            dy = -1;
            break;
         case DOWN:
            dy = 1;
            break;
         case LEFT:
            dx = -1;
            break;
         case RIGHT:
            dx = 1;
            break;
      }
      view.shiftView(dx, dy);

   }

}

   public void mousePressed()
   {

      Point pressed = new Point(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
      pressed = view.getViewport().viewportToWorld(pressed.x, pressed.y);
      List<Point> sets = new ArrayList<>();
      sets.add(pressed);
      sets.add(new Point(pressed.x, pressed.y+1));
      sets.add(new Point(pressed.x-1, pressed.y-1));
      sets.add(new Point(pressed.x+1, pressed.y+1));
      sets.add(new Point(pressed.x, pressed.y-1));
      sets.add(new Point(pressed.x-1, pressed.y));
      sets.add(new Point(pressed.x-1, pressed.y+1));
      sets.add(new Point(pressed.x+1, pressed.y));
      sets.add(new Point(pressed.x+1, pressed.y-1));

      Background tree = new Background("tree", imageStore.getImageList("tree"));
      Entity santa = Santa.createSanta("santa", pressed, 0, 0, imageStore.getImageList("santa"));
      world.addEntity(santa);
      ((Santa)santa).scheduleActions(world, scheduler, imageStore);

      for(Point p: sets)
      {
            world.setBackground(p, tree);
      }

      for(Point p: sets)
      {

         if(world.getOccupant(p).isPresent())
         {
            if (world.getOccupant(p).get() instanceof VEIN){
               String hi = "1";
            }
            if (world.getOccupant(p).get() instanceof MINER_FULL || world.getOccupant(p).get() instanceof MINER_NOT_FULL){
               Miners miner = (Miners) world.getOccupant(p).get();
               miner.setImages(imageStore.getImageList("reindeer"));
               miner.setActionPeriod(10);

            }

         }

      }

      redraw();

   }

   private Point mouseToPoint(int x, int y)
   {
      return new Point(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
   }






   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         Functions.load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }


   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }



   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
