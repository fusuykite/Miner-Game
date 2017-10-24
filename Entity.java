final class Entityy
{
//
//   public static Entity createBlacksmith(String id, Point position,
//                                         List<PImage> images)
//   {
//      return new Entity(EntityKind.BLACKSMITH, id, position, images,
//         0, 0, 0, 0);
//   }
//
//   public static Entity createMinerFull(String id, int resourceLimit,
//                                        Point position, int actionPeriod, int animationPeriod,
//                                        List<PImage> images)
//   {
//      return new Entity(EntityKind.MINER_FULL, id, position, images,
//         resourceLimit, resourceLimit, actionPeriod, animationPeriod);
//   }
//
//   public static Entity createMinerNotFull(String id, int resourceLimit,
//                                           Point position, int actionPeriod, int animationPeriod,
//                                           List<PImage> images)
//   {
//      return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
//         resourceLimit, 0, actionPeriod, animationPeriod);
//   }
//
//   public static Entity createObstacle(String id, Point position,
//                                       List<PImage> images)
//   {
//      return new Entity(EntityKind.OBSTACLE, id, position, images,
//         0, 0, 0, 0);
//   }
//
//   public static Entity createOre(String id, Point position, int actionPeriod,
//                                  List<PImage> images)
//   {
//      return new Entity(EntityKind.ORE, id, position, images, 0, 0,
//         actionPeriod, 0);
//   }
//
//   public static Entity createOreBlob(String id, Point position,
//                                      int actionPeriod, int animationPeriod, List<PImage> images)
//   {
//      return new Entity(EntityKind.ORE_BLOB, id, position, images,
//            0, 0, actionPeriod, animationPeriod);
//   }
//
//   public static Entity createQuake(Point position, List<PImage> images)
//   {
//      return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
//         0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
//   }
//
//   public static Entity createVein(String id, Point position, int actionPeriod,
//                                   List<PImage> images)
//   {
//      return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
//         actionPeriod, 0);
//   }
//
//   public void nextImage()
//   {
//      this.imageIndex = (this.imageIndex + 1) % this.images.size();
//   }
//
//   public int getAnimationPeriod()
//    {
//       switch (kind)
//       {
//       case MINER_FULL:
//       case MINER_NOT_FULL:
//       case ORE_BLOB:
//       case QUAKE:
//          return animationPeriod;
//       default:
//          throw new UnsupportedOperationException(
//             String.format("getAnimationPeriod not supported for %s",
//             kind));
//       }
//    }
//
//   public boolean transformNotFull(WorldModel world,
//                                   EventScheduler scheduler, ImageStore imageStore)
//   {
//      if (resourceCount >= resourceLimit)
//      {
//         Entity miner = createMinerFull(id, resourceLimit,
//            position, actionPeriod, animationPeriod,
//            images);
//
//         world.removeEntity(this);
//         scheduler.unscheduleAllEvents(this);
//
//         world.addEntity(miner);
//         miner.scheduleActions(world, imageStore, scheduler);
//
//         return true;
//      }
//
//      return false;
//   }
//
//   public void transformFull(WorldModel world,
//                             EventScheduler scheduler, ImageStore imageStore)
//   {
//      Entity miner = createMinerNotFull(id, resourceLimit,
//         position, actionPeriod, animationPeriod,
//         images);
//
//      world.removeEntity(this);
//      scheduler.unscheduleAllEvents(this);
//
//      world.addEntity(miner);
//      miner.scheduleActions(world, imageStore, scheduler);
//   }
//
//   public boolean moveToNotFull(WorldModel world,
//                                Entity target, EventScheduler scheduler)
//   {
//      if (Point.adjacent(position, target.position))
//      {
//         resourceCount += 1;
//         world.removeEntity(target);
//         scheduler.unscheduleAllEvents(target);
//
//         return true;
//      }
//      else
//      {
//         Point nextPos = nextPositionMiner(world, target.position);
//
//         if (!position.equals(nextPos))
//         {
//            Optional<Entity> occupant = world.getOccupant(nextPos);
//            if (occupant.isPresent())
//            {
//               scheduler.unscheduleAllEvents(occupant.get());
//            }
//
//            world.moveEntity(this, nextPos);
//         }
//         return false;
//      }
//   }
//
//   public boolean moveToFull(WorldModel world,
//                             Entity target, EventScheduler scheduler)
//   {
//      if (Point.adjacent(position, target.position))
//      {
//         return true;
//      }
//      else
//      {
//         Point nextPos = nextPositionMiner(world, target.position);
//
//         if (!position.equals(nextPos))
//         {
//            Optional<Entity> occupant = world.getOccupant(nextPos);
//            if (occupant.isPresent())
//            {
//               scheduler.unscheduleAllEvents(occupant.get());
//            }
//
//            world.moveEntity(this, nextPos);
//         }
//         return false;
//      }
//   }
//
//   public boolean moveToOreBlob(WorldModel world,
//                                Entity target, EventScheduler scheduler)
//   {
//      if (Point.adjacent(position, target.position))
//      {
//         world.removeEntity(target);
//         scheduler.unscheduleAllEvents(target);
//         return true;
//      }
//      else
//      {
//         Point nextPos = nextPositionOreBlob(world, target.position);
//
//         if (!position.equals(nextPos))
//         {
//            Optional<Entity> occupant = world.getOccupant(nextPos);
//            if (occupant.isPresent())
//            {
//               scheduler.unscheduleAllEvents(occupant.get());
//            }
//
//            world.moveEntity(this, nextPos);
//         }
//         return false;
//      }
//   }
//
//   public Point nextPositionMiner(WorldModel world,
//                                  Point destPos)
//   {
//      int horiz = Integer.signum(destPos.x - position.x);
//      Point newPos = new Point(position.x + horiz,
//         position.y);
//
//      if (horiz == 0 || world.isOccupied(newPos))
//      {
//         int vert = Integer.signum(destPos.y - position.y);
//         newPos = new Point(position.x,
//            position.y + vert);
//
//         if (vert == 0 || world.isOccupied(newPos))
//         {
//            newPos = position;
//         }
//      }
//
//      return newPos;
//   }
//
//   public Point nextPositionOreBlob(WorldModel world,
//                                    Point destPos)
//   {
//      int horiz = Integer.signum(destPos.x - position.x);
//      Point newPos = new Point(position.x + horiz,
//         position.y);
//
//      Optional<Entity> occupant = world.getOccupant(newPos);
//
//      if (horiz == 0 ||
//         (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
//      {
//         int vert = Integer.signum(destPos.y - position.y);
//         newPos = new Point(position.x, position.y + vert);
//         occupant = world.getOccupant(newPos);
//
//         if (vert == 0 ||
//            (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
//         {
//            newPos = position;
//         }
//      }
//
//      return newPos;
//   }
//
//   public void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
//   {
//      Optional<Entity> notFullTarget = WorldModel.findNearest(world, position,
//         EntityKind.ORE);
//
//      if (!notFullTarget.isPresent() ||
//         !moveToNotFull(world, notFullTarget.get(), scheduler) ||
//         !transformNotFull(world, scheduler, imageStore))
//      {
//         scheduler.scheduleEvent(this,
//            Action.createActivityAction(this, world, imageStore),
//            actionPeriod);
//      }
//   }
//
//   public void executeOreActivity(WorldModel world,
//                                  ImageStore imageStore, EventScheduler scheduler)
//   {
//      Point pos = position;  // store current position before removing
//
//      world.removeEntity(this);
//      scheduler.unscheduleAllEvents(this);
//
//      Entity blob = createOreBlob(id + BLOB_ID_SUFFIX,
//         pos, actionPeriod / BLOB_PERIOD_SCALE,
//         BLOB_ANIMATION_MIN +
//            Functions.rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
//         ImageStore.getImageList(imageStore, BLOB_KEY));
//
//      world.addEntity(blob);
//      blob.scheduleActions(world, imageStore, scheduler);
//   }
//
//   public void executeOreBlobActivity(WorldModel world,
//                                      ImageStore imageStore, EventScheduler scheduler)
//   {
//      Optional<Entity> blobTarget = WorldModel.findNearest(world,
//         position, EntityKind.VEIN);
//      long nextPeriod = actionPeriod;
//
//      if (blobTarget.isPresent())
//      {
//         Point tgtPos = blobTarget.get().position;
//
//         if (moveToOreBlob(world, blobTarget.get(), scheduler))
//         {
//            Entity quake = createQuake(tgtPos,
//               ImageStore.getImageList(imageStore, QUAKE_KEY));
//
//            world.addEntity(quake);
//            nextPeriod += actionPeriod;
//            quake.scheduleActions(world, imageStore, scheduler);
//         }
//      }
//
//      scheduler.scheduleEvent(this,
//         Action.createActivityAction(this, world, imageStore),
//         nextPeriod);
//   }
//
//   public void executeQuakeActivity(WorldModel world,
//                                    ImageStore imageStore, EventScheduler scheduler)
//   {
//      scheduler.unscheduleAllEvents(this);
//      world.removeEntity(this);
//   }
//
//   public void executeVeinActivity(WorldModel world,
//                                   ImageStore imageStore, EventScheduler scheduler)
//   {
//      Optional<Point> openPt = findOpenAround(world, position);
//
//      if (openPt.isPresent())
//      {
//         Entity ore = createOre(ORE_ID_PREFIX + id,
//            openPt.get(), ORE_CORRUPT_MIN +
//               Functions.rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
//            ImageStore.getImageList(imageStore, ORE_KEY));
//         world.addEntity(ore);
//         ore.scheduleActions(world, imageStore, scheduler);
//      }
//
//      scheduler.scheduleEvent(this,
//         Action.createActivityAction(this, world, imageStore),
//         actionPeriod);
//   }
//
//   public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
//   {
//      switch (kind)
//      {
//      case MINER_FULL:
//         scheduler.scheduleEvent(this,
//            Action.createActivityAction(this, world, imageStore),
//            actionPeriod);
//         scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0),
//            getAnimationPeriod());
//         break;
//
//      case MINER_NOT_FULL:
//         scheduler.scheduleEvent(this,
//            Action.createActivityAction(this, world, imageStore),
//            actionPeriod);
//         scheduler.scheduleEvent(this,
//            Action.createAnimationAction(this, 0), getAnimationPeriod());
//         break;
//
//      case ORE:
//         scheduler.scheduleEvent(this,
//            Action.createActivityAction(this, world, imageStore),
//            actionPeriod);
//         break;
//
//      case ORE_BLOB:
//         scheduler.scheduleEvent(this,
//            Action.createActivityAction(this, world, imageStore),
//            actionPeriod);
//         scheduler.scheduleEvent(this,
//            Action.createAnimationAction(this, 0), getAnimationPeriod());
//         break;
//
//      case QUAKE:
//         scheduler.scheduleEvent(this,
//            Action.createActivityAction(this, world, imageStore),
//            actionPeriod);
//         scheduler.scheduleEvent(this,
//            Action.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
//            getAnimationPeriod());
//         break;
//
//      case VEIN:
//         scheduler.scheduleEvent(this,
//            Action.createActivityAction(this, world, imageStore),
//            actionPeriod);
//         break;
//
//      default:
//      }
//   }
}
