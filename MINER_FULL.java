import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MINER_FULL extends Miners implements Move{


    public MINER_FULL(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, 0, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }




    public void transformFull(WorldModel world,
                              EventScheduler scheduler, ImageStore imageStore)
    {
        Entity miner = MINER_NOT_FULL.createMinerNotFull(id, resourceLimit, position, actionPeriod, animationPeriod, images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((MINER_NOT_FULL)miner).scheduleActions(world, scheduler, imageStore);
    }

    public boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition());

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





    public static Entity createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images, int imageIndex)
    {
        return new MINER_FULL(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }



    public void execute( WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler)
    {
        Move_Visit move_visit = new Move_Visit();
        BlackSmith_Visit blackSmith_visit = new BlackSmith_Visit();
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                blackSmith_visit);

        if (fullTarget.isPresent() && this.accept(move_visit)&&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    actionPeriod);
        }
    }

    //need to override becasuse different type of scheduleActions
    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0),
                getAnimationPeriod());
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

}
