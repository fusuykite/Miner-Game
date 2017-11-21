import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MINER_NOT_FULL extends Miners implements Move{


    public MINER_NOT_FULL(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, 0, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }



    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit)
        {
            Entity miner = MINER_FULL.createMinerFull(id, resourceLimit,
                    position, actionPeriod, animationPeriod,
                    images, imageIndex);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((MINER_FULL)miner).scheduleActions(world, scheduler, imageStore);

            return true;
        }

        return false;
    }

    public  boolean moveToNotFull(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(world, target.getPosition());


            if (!getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }







    public static Entity createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new MINER_NOT_FULL(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    //need to override because different type of scheduleactions
    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0),
                getAnimationPeriod());
    }

    public void execute( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        ORE_Visit ore_visit = new ORE_Visit();
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                ore_visit);

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    actionPeriod);
        }
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

}
