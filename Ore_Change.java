//import processing.core.PImage;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Optional;
//
//public class Ore_Change extends Miners implements Move
//{
//
//    private static final String QUAKE_KEY = "quake";
//    private static final int QUAKE_ACTION_PERIOD = 1100;
//    private static final int QUAKE_ANIMATION_PERIOD = 100;
//
//    public Ore_Change(Point position,
//                   List<PImage> images,
//                   int actionPeriod, int animationPeriod)
//    {
//        super(id, position, images, 0, resourceLimit, resourceCount, actionPeriod, animationPeriod);
//    }
//
//
//    public void executeActivity(WorldModel world,
//                                ImageStore imageStore, EventScheduler scheduler)
//    {
//        //Optional<Entity> blobTarget = this.getPosition().findNearest(world, Vein.class);
//
//        Optional<Entity> blobTarget = this.findNearest(world, this.getPosition());
//
//        long nextPeriod = this.getActionPeriod();
//
//        if (blobTarget.isPresent())
//        {
//            Point tgtPos = blobTarget.get().getPosition();
//
//            if (this.moveTo(world, blobTarget.get(), scheduler))
//            {
//                Quake quake = new Quake(tgtPos,
//                        imageStore.getImageList(QUAKE_KEY), QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
//
//                world.addEntity(quake);
//                nextPeriod += this.getActionPeriod();
//                quake.scheduleActions(scheduler, world, imageStore);
//            }
//        }
//
//        Action activity = new Activity(this, world, imageStore, 0);
//        scheduler.scheduleEvent(this,
//                activity,
//                nextPeriod);
//    }
//
//    public Optional<Entity> findNearest(WorldModel world, Point pos)
//    {
//        List<Entity> ofType = new LinkedList<>();
//        OreBlobVisitor oreBlobVisitor = new OreBlobVisitor();
//
//        for (Entity entity : world.getEntities())
//        {
//            if (entity.accept(oreBlobVisitor))
//            {
//                ofType.add(entity);
//            }
//        }
//
//        return pos.nearestEntity(ofType);
//    }
//
//
//    public boolean moveTo(WorldModel world,
//                          Entity target, EventScheduler scheduler)
//    {
//        if (this.getPosition().adjacent(target.getPosition()))
//        {
//            world.removeEntity(target);
//            scheduler.unscheduleAllEvents(target);
//            return true;
//        }
//        else
//        {
//            Point nextPos = this.nextPosition(world, target.getPosition());
//
//            if (!this.getPosition().equals(nextPos))
//            {
//                Optional<Entity> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent())
//                {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
//
//                world.moveEntity(this, nextPos);
//            }
//            return false;
//        }
//    }
//
//
//    public Point nextPosition(WorldModel world,
//                              Point destPos)
//    {
//        OreBlobVisitor oreVisitor = new OreBlobVisitor();
//
//
//        PathingStrategy p = new SingleStepPathingStrategy();
//        List<Point> path = p.computePath(this.getPosition(), destPos,
//                pos -> world.withinBounds(pos) && !(world.isOccupied(pos)),
//                (p1, p2) -> neighbors(p1,p2),
//                PathingStrategy.CARDINAL_NEIGHBORS);
//
//
//        try
//        {
//            if(path.size() == 0)
//            {
//                return(this.getPosition());
//            }
//            else
//            {
//                return(path.get(0));
//            }
//        }
//        catch(Exception e)
//        {
//            System.out.println("OreBlob");
//            return(this.getPosition());
//        }
//
///*
//        if(path == null)
//        {
//            return(this.getPosition());
//        }
//        else
//        {
//            if(path.size() == 0)
//            {
//                return(this.getPosition());
//            }
//            return path.get(0);
//        }
//        */
//
///*
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz,
//                this.getPosition().y);
//        Optional<Entity> occupant = world.getOccupant(newPos);
//        if (horiz == 0 ||
//                (occupant.isPresent() && !(occupant.get().accept(oreVisitor))))
//        {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//            occupant = world.getOccupant(newPos);
//            if (vert == 0 ||
//                    (occupant.isPresent() && !(occupant.get().accept(oreVisitor))))
//            {
//                newPos = this.getPosition();
//            }
//        }
//        return newPos;
//        */
//    }
//
//    private static boolean neighbors(Point p1, Point p2)
//    {
//        return p1.x+1 == p2.x && p1.y == p2.y ||
//                p1.x-1 == p2.x && p1.y == p2.y ||
//                p1.x == p2.x && p1.y+1 == p2.y ||
//                p1.x == p2.x && p1.y-1 == p2.y;
//    }
//
//
//    public <R> R accept(EntityVisitor<R> visitor)
//    {
//        return visitor.visit(this);
//    }
//
//}