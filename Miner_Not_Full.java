import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Miner_Not_Full implements Move{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private Random rand;



    public Miner_Not_Full(String id, Point position, List<PImage> images,
                        int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }



    public static Miner_Not_Full createMinerNotFull(String id, int resourceLimit, int actionPeriod, int animationPeriod, List<PImage> images, Point position)
    {
        return new Miner_Not_Full(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }


    public void move(WorldModel worldModel, Point pos){
        Point oldPos = position;
        if (worldModel.withinBounds(pos) && !pos.equals(oldPos))
        {
            worldModel.setOccupancyCell(pos, this);
            worldModel.removeEntityAt(pos);
            worldModel.setOccupancyCell(pos, this);
            position = pos;
        }
    }


    public boolean moveToNotFull(WorldModel world, EntityInterface target, EventScheduler scheduler)
    {
        if (target.position().adjacent(position))
        {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPos(target.position(), world);

            if (!position.equals(nextPos))
            {
                Optional<EntityInterface> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                move(world, nextPos);
            }
            return false;
        }
    }


    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit)
        {
            Miner_Full miner = Miner_Full.createMinerFull(id, resourceLimit, actionPeriod, animationPeriod, images, position);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }



    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<EntityInterface> notFullTarget = world.findNearest(position, Ore.class);

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    actionPeriod);
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore, 0),
                actionPeriod);
        scheduler.scheduleEvent(this,
                new Animation(this, null, null, 0), getAnimationPeriod());
    }

    public  PImage getCurrentImage()
    {

            return (this.images.get(this.imageIndex));

    }

    public int getAnimationPeriod()
    {

                return animationPeriod;

    }



    public Point nextPos(Point point, WorldModel world)
    {
        int horiz = Integer.signum(point.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(point.y - position.y);
            newPos = new Point(position.x,
                    position.y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = position;
            }
        }

        return newPos;
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    public Point position(){
        return position ;
    }

    public void setPosition(Point position){
        this.position = position;

    }
}
