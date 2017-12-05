import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Santa extends Animation_AB implements Animated {



    public Santa(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod) {
        super(id, position, images, imageIndex, actionPeriod, animationPeriod);
    }



    public Point nextPosition(WorldModel world,
                              Point destPos)
    {
        PathingStrategy strategy = new SingleStepPathingStrategy();

        List<Point> points = strategy.computePath(position, destPos, canPassThrough(world), withinReach(), CARDINAL_NEIGHBORS);


        if (points.size() == 0) {
            return position;
        }
        return points.get(0);
    }



    protected static Predicate<Point> canPassThrough(WorldModel world){
        return p -> (!world.isOccupied(p) && world.withinBounds(p));
    }



    protected static BiPredicate<Point, Point> withinReach(){
        return (Point p1, Point p2) -> p1.adjacent(p2);

    }

    public static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x, point.y - 1))
                            .add(new Point(point.x, point.y + 1))
                            .add(new Point(point.x - 1, point.y))
                            .add(new Point(point.x + 1, point.y))
                            .build();

    public static final Function<Point, Stream<Point>> DIAGONAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x-1, point.y - 1))
                            .add(new Point(point.x+1, point.y + 1))
                            .add(new Point(point.x - 1, point.y+1))
                            .add(new Point(point.x + 1, point.y-1))
                            .build();




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
    public static Entity createSanta(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Santa(id, position, images, 0, actionPeriod, animationPeriod);
    }

    public void execute( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        MINER_NOT_FULL_Visit vein_visit = new MINER_NOT_FULL_Visit();
        ORE_BLOB_Visit ore_blob_visit = new ORE_BLOB_Visit();
        Optional<Entity> blobTarget = world.findNearest(this.getPosition(), ore_blob_visit);
        long nextPeriod = actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(scheduler, this, world, blobTarget.get()))
            {
                Entity quake = Quake.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += actionPeriod;
                ((Quake)quake).scheduleActions(world, scheduler, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
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

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }

}
