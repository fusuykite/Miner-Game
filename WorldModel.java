import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   protected int numRows;
   protected int numCols;
   protected Background background[][];
   protected Entity occupancy[][];
   protected Set<Entity> entities;

   public static final int ORE_REACH = 1;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public Optional<Entity> nearestEntity(List<Entity> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(pos);

         for (Entity other : entities)
         {
            int otherDistance = other.getPosition().distanceSquared(pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }
   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < numRows &&
              pos.x >= 0 && pos.x < numCols;
   }


   public Optional<Entity> findNearest(Point pos, String type)
   {
      List<Entity> ofType = new LinkedList<>();

      if (type.equals("VEIN")) {
         for (Entity entity : entities) {
            if (entity instanceof VEIN) {
               ofType.add(entity);
            }
         }
      }
      else if (type.equals("ORE")) {
         for (Entity entity : entities) {
            if (entity instanceof ORE) {
               ofType.add(entity);
            }
         }
      }
      else if (type.equals("BlackSmith")) {
         for (Entity entity : entities) {
            if (entity instanceof BlackSmith) {
               ofType.add(entity);
            }
         }
      }

      return nearestEntity(ofType, pos);
   }



   public Entity getOccupancyCell(Point pos)
   {
      return occupancy[pos.y][pos.x];
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   public Optional<Entity> getOccupant(Point pos)
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

   public void setOccupancyCell(Point pos, Entity entity)
   {
      occupancy[pos.y][pos.x] = entity;
   }

   public void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }







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


   public void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(Background.getCurrentImage(getBackgroundCell(pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   public void setBackground(Point pos, Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   public void addEntity(Entity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         entities.add(entity);
      }
   }






   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }




   public Background getBackgroundCell(Point pos)
   {
      return background[pos.y][pos.x];
   }

   public void setBackgroundCell(Point pos, Background back)
   {
      background[pos.y][pos.x] = back;
   }


   public void removeEntity(Entity target)
   {removeEntityAt(target.getPosition());
   }

   public Point nextPositionMiner(Entity miner, Point destPos)
   {
      int horiz = Integer.signum(destPos.x - miner.getPosition().x);
      Point newPos = new Point(miner.getPosition().x + horiz,
              miner.getPosition().y);

      if (horiz == 0 || isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.y - miner.getPosition().y);
         newPos = new Point(miner.getPosition().x,
                 miner.getPosition().y + vert);

         if (vert == 0 || isOccupied(newPos))
         {
            newPos = miner.getPosition();
         }
      }

      return newPos;
   }
}
