import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   private int numRows;
   private int numCols;
   public int getrows(){return numRows;
   }
   public int getCols(){return numCols;}
   public Background background[][];
   public EntityInterface occupancy[][];

   private Set<EntityInterface> entities;

   private static final String BLOB_KEY = "blob";
   private static final String BLOB_ID_SUFFIX = " -- blob";
   private static final int BLOB_PERIOD_SCALE = 4;
   private static final int BLOB_ANIMATION_MIN = 50;
   private static final int BLOB_ANIMATION_MAX = 150;

   private static final String ORE_ID_PREFIX = "ore -- ";
   private static final int ORE_CORRUPT_MIN = 20000;
   private static final int ORE_CORRUPT_MAX = 30000;
   private static final int ORE_REACH = 1;

   private static final String QUAKE_KEY = "quake";
   private static final String QUAKE_ID = "quake";
   private static final int QUAKE_ACTION_PERIOD = 1100;
   private static final int QUAKE_ANIMATION_PERIOD = 100;
   private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   private static final int PROPERTY_KEY = 0;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final String MINER_KEY = "miner";
   private static final int MINER_NUM_PROPERTIES = 7;
   private static final int MINER_ID = 1;
   private static final int MINER_COL = 2;
   private static final int MINER_ROW = 3;
   private static final int MINER_LIMIT = 4;
   private static final int MINER_ACTION_PERIOD = 5;
   private static final int MINER_ANIMATION_PERIOD = 6;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String ORE_KEY = "ore";
   private static final int ORE_NUM_PROPERTIES = 5;
   private static final int ORE_ID = 1;
   private static final int ORE_COL = 2;
   private static final int ORE_ROW = 3;
   private static final int ORE_ACTION_PERIOD = 4;

   private static final String SMITH_KEY = "blacksmith";
   private static final int SMITH_NUM_PROPERTIES = 4;
   private final int SMITH_ID = 1;
   private static final int SMITH_COL = 2;
   private static final int SMITH_ROW = 3;

   private static final String VEIN_KEY = "vein";
   private static final int VEIN_NUM_PROPERTIES = 5;
   private static final int VEIN_ID = 1;
   private static final int VEIN_COL = 2;
   private static final int VEIN_ROW = 3;
   private static final int VEIN_ACTION_PERIOD = 4;



   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new EntityInterface[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows(){
      return this.numRows;
   }

   public int getNumCols(){
      return this.numCols;
   }

   public Set<EntityInterface> getEntities(){
      return this.entities;
   }

   public static Optional<EntityInterface> nearestEntity(List<EntityInterface> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         EntityInterface nearest = entities.get(0);
         int nearestDistance = Point.distanceSquared(nearest.position(), pos);

         for (EntityInterface other : entities)
         {
            int otherDistance = Point.distanceSquared(other.position(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public  Optional<EntityInterface> findNearest( Point pos,
                                              Class other)
   {
      List<EntityInterface> ofType = new LinkedList<>();
      for (EntityInterface entity : entities)
      {
         if (entity.getClass() == other)
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }



   public boolean processLine(String line,
                              ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return parseBackground(properties, imageStore);
            case MINER_KEY:
               return parseMiner(properties, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, imageStore);
            case ORE_KEY:
               return parseOre(properties, imageStore);
            case SMITH_KEY:
               return parseSmith(properties, imageStore);
            case VEIN_KEY:
               return parseVein(properties, imageStore);
         }
      }

      return false;
   }


   public boolean parseObstacle(String[] properties,
                                ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Obstacle entity = Obstacle.createObstacle(properties[OBSTACLE_ID],
                 ImageStore.getImageList(imageStore, OBSTACLE_KEY), pt);
         tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }


   public boolean parseBackground(String[] properties,
                                  ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         setBackground(pt,
                 new Background(id, ImageStore.getImageList(imageStore, id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public boolean parseMiner(String[] properties,
                             ImageStore imageStore)
   {
      if (properties.length == MINER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                 Integer.parseInt(properties[MINER_ROW]));
         Miner_Not_Full entity = Miner_Not_Full.createMinerNotFull(properties[MINER_ID],
                 Integer.parseInt(properties[MINER_LIMIT]),
                 Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                 Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                 ImageStore.getImageList(imageStore, MINER_KEY), pt);
         tryAddEntity(entity);
      }

      return properties.length == MINER_NUM_PROPERTIES;
   }


   public boolean parseOre(String[] properties,
                           ImageStore imageStore)
   {
      if (properties.length == ORE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                 Integer.parseInt(properties[ORE_ROW]));
         Ore entity = Ore.createOre(properties[ORE_ID],
                 Integer.parseInt(properties[ORE_ACTION_PERIOD]),
                 ImageStore.getImageList(imageStore, ORE_KEY), pt);
         tryAddEntity(entity);
      }

      return properties.length == ORE_NUM_PROPERTIES;
   }

   public boolean parseSmith(String[] properties,
                             ImageStore imageStore)
   {
      if (properties.length == SMITH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
            Integer.parseInt(properties[SMITH_ROW]));
         BlackSmith entity = BlackSmith.createBlacksmith(properties[SMITH_ID],
            ImageStore.getImageList(imageStore, SMITH_KEY), pt);
         tryAddEntity(entity);
      }

      return properties.length == SMITH_NUM_PROPERTIES;
   }

   public boolean parseVein(String[] properties,
                            ImageStore imageStore)
   {
      if (properties.length == VEIN_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
            Integer.parseInt(properties[VEIN_ROW]));
         Vein entity = Vein.createVein(properties[VEIN_ID],
            Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
            ImageStore.getImageList(imageStore, VEIN_KEY),
                 pt);
         tryAddEntity(entity);
      }

      return properties.length == VEIN_NUM_PROPERTIES;
   }

   public void tryAddEntity(EntityInterface entity)
   {
      if (isOccupied(entity.position()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < numRows &&
         pos.x >= 0 && pos.x < numCols;
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
         getOccupancyCell(pos) != null;
   }

   public void moveEntity(EntityInterface entity, Point pos)
   {
      Point oldPos = entity.position();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(EntityInterface entity)
   {
      removeEntityAt(entity.position());
   }


    public void removeEntityAt(Point pos)
    {
       if (withinBounds(pos)
          && getOccupancyCell(pos) != null)
       {
          EntityInterface entity = getOccupancyCell(pos);

          /* this moves the entity just outside of the grid for
             debugging purposes */
          entity.setPosition(new Point(-1, -1));
          entities.remove(entity);
          setOccupancyCell(pos, null);
       }
    }


    /*
           Assumes that there is no entity currently occupying the
           intended destination cell.
        */


       public void addEntity(EntityInterface entity)
       {
          if (entity.position().withinBounds(this))
          {
             entity.position().setOccupancyCell(entity, this);
             entities.add(entity);
          }
       }


    public Optional<PImage> getBackgroundImage(Point pos)
    {
       if (withinBounds(pos))
       {
          return Optional.of((getBackgroundCell(pos)).getCurrentImage());
       }
       else
       {
          return Optional.empty();
       }
    }

   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
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


    public void setBackground(Point pos,
                              Background background)
    {
       if (withinBounds(pos))
       {
          setBackgroundCell(pos, background);
       }
    }

    public Optional<EntityInterface> getOccupant(Point pos)
    {
       if (isOccupied(pos))
       {
          return Optional.of(getOccupancyCell(pos));
       }
       else
       {
          return Optional.empty();
       }
    }

    public EntityInterface getOccupancyCell(Point pos)
    {
       return occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(Point pos,
                                 EntityInterface entity)
    {
       occupancy[pos.y][pos.x] = entity;
    }

    public Background getBackgroundCell(Point pos)
    {
       return background[pos.y][pos.x];
    }

    public void setBackgroundCell(Point pos,
                                  Background background)
    {
       this.background[pos.y][pos.x] = background;
    }


   //   public EntityKind kind;
   //   public String id;
   //   public Point position;
   //   public List<PImage> images;
   //   public int imageIndex;
   //   public int resourceLimit;
   //   public int resourceCount;
   //   public int actionPeriod;
   //   public int animationPeriod;
   //
   //   public static final String BLOB_KEY = "blob";
   //   public static final String BLOB_ID_SUFFIX = " -- blob";
   //   public static final int BLOB_PERIOD_SCALE = 4;
   //   public static final int BLOB_ANIMATION_MIN = 50;
   //   public static final int BLOB_ANIMATION_MAX = 150;
   //
   //   public static final String ORE_ID_PREFIX = "ore -- ";
   //   public static final int ORE_CORRUPT_MIN = 20000;
   //   public static final int ORE_CORRUPT_MAX = 30000;
   //   public static final int ORE_REACH = 1;
   //
   //   public static final String QUAKE_KEY = "quake";
   //   public static final String QUAKE_ID = "quake";
   //   public static final int QUAKE_ACTION_PERIOD = 1100;
   //   public static final int QUAKE_ANIMATION_PERIOD = 100;
   //   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   //
   //    public static final int PROPERTY_KEY = 0;
   //
   //    public static final String BGND_KEY = "background";
   //    public static final int BGND_NUM_PROPERTIES = 4;
   //    public static final int BGND_ID = 1;
   //    public static final int BGND_COL = 2;
   //    public static final int BGND_ROW = 3;
   //
   //    public static final String MINER_KEY = "miner";
   //    public static final int MINER_NUM_PROPERTIES = 7;
   //    public static final int MINER_ID = 1;
   //    public static final int MINER_COL = 2;
   //    public static final int MINER_ROW = 3;
   //    public static final int MINER_LIMIT = 4;
   //    public static final int MINER_ACTION_PERIOD = 5;
   //    public static final int MINER_ANIMATION_PERIOD = 6;
   //
   //    public static final String OBSTACLE_KEY = "obstacle";
   //    public static final int OBSTACLE_NUM_PROPERTIES = 4;
   //    public static final int OBSTACLE_ID = 1;
   //    public static final int OBSTACLE_COL = 2;
   //    public static final int OBSTACLE_ROW = 3;
   //
   //    public static final String ORE_KEY = "ore";
   //    public static final int ORE_NUM_PROPERTIES = 5;
   //    public static final int ORE_ID = 1;
   //    public static final int ORE_COL = 2;
   //    public static final int ORE_ROW = 3;
   //    public static final int ORE_ACTION_PERIOD = 4;
   //
   //    public static final String SMITH_KEY = "blacksmith";
   //    public static final int SMITH_NUM_PROPERTIES = 4;
   //    public static final int SMITH_ID = 1;
   //    public static final int SMITH_COL = 2;
   //    public static final int SMITH_ROW = 3;
   //
   //    public static final String VEIN_KEY = "vein";
   //    public static final int VEIN_NUM_PROPERTIES = 5;
   //    public static final int VEIN_ID = 1;
   //    public static final int VEIN_COL = 2;
   //    public static final int VEIN_ROW = 3;
   //    public static final int VEIN_ACTION_PERIOD = 4;
   //
   //
   //
   //
   //   public Entity(EntityKind kind, String id, Point position,
   //      List<PImage> images, int resourceLimit, int resourceCount,
   //      int actionPeriod, int animationPeriod)
   //   {
   //      this.kind = kind;
   //      this.id = id;
   //      this.position = position;
   //      this.images = images;
   //      this.imageIndex = 0;
   //      this.resourceLimit = resourceLimit;
   //      this.resourceCount = resourceCount;
   //      this.actionPeriod = actionPeriod;
   //      this.animationPeriod = animationPeriod;
   //   }
   //
   //   public static PImage getCurrentImage(Object entity)
   //   {
   //      if (entity instanceof Background)
   //      {
   //         return ((Background)entity).images
   //            .get(((Background)entity).imageIndex);
   //      }
   //      else if (entity instanceof Entity)
   //      {
   //         return ((Entity)entity).images.get(((Entity)entity).imageIndex);
   //      }
   //      else
   //      {
   //         throw new UnsupportedOperationException(
   //            String.format("getCurrentImage not supported for %s",
   //            entity));
   //      }
   //   }
   //
      public Optional<Point> findOpenAround(Point pos)
      {
         for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
         {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
            {
               Point newPt = new Point(pos.x + dx, pos.y + dy);
               if (withinBounds(newPt) &&
                  !isOccupied(newPt))
               {
                  return Optional.of(newPt);
               }
            }
         }

         return Optional.empty();
      }
}
