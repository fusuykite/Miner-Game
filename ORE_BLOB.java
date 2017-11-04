import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class ORE_BLOB extends Animation_AB {



    public ORE_BLOB(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod) {
        super(id, position, images, imageIndex, actionPeriod, animationPeriod);
    }



    public Point nextPosition(WorldModel world,
                                     Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof ORE)))
        {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof ORE)))
            {
                newPos = position;
            }
        }

        return newPos;
    }





    public boolean moveToOreBlob(EventScheduler scheduler, Entity blob, WorldModel world, Entity target)
    {
        if (blob.getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!blob.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(blob, nextPos);
            }
            return false;
        }
    }
    public static Entity createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new ORE_BLOB(id, position, images, 0, actionPeriod, animationPeriod);
    }

    public void executeOreBlobActivity(EventScheduler scheduler, Entity entity, WorldModel world, ImageStore imageStore)
    {
        Optional<Entity> blobTarget = world.findNearest(entity.getPosition(), "VEIN");
        long nextPeriod = actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(scheduler, entity, world, blobTarget.get()))
            {
                Entity quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += actionPeriod;
                ((Quake)quake).scheduleActions(world, scheduler, imageStore);
            }
        }

        scheduler.scheduleEvent(entity,
                Activity.createActivityAction(entity, world, imageStore),
                nextPeriod);
    }


    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0),
                getAnimationPeriod());
    }

}
