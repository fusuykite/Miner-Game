import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;


public class Ore_Blob implements Move {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private Random rand;

    private static final String BLOB_KEY = "blob"; //
    private static final String BLOB_ID_SUFFIX = " -- blob"; //
    private static final int BLOB_PERIOD_SCALE = 4; //
    private static final int BLOB_ANIMATION_MIN = 50; //
    private static final int BLOB_ANIMATION_MAX = 150; //

    private static final String ORE_ID_PREFIX = "ore -- "; //
    private static final int ORE_CORRUPT_MIN = 20000; //
    private static final int ORE_CORRUPT_MAX = 30000; //
    private static final int ORE_REACH = 1; //

    private static final String QUAKE_KEY = "quake"; //


    public Ore_Blob(String id, Point position, List<PImage> images,
                   int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    public static Ore_Blob createOreBlob(String id, int actionPeriod,
                                         int animationPeriod, List<PImage> images, Point position)
    {
        return new Ore_Blob(id, position, images,0, 0, actionPeriod, animationPeriod);
    }



    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<EntityInterface> blobTarget = world.findNearest(position, Vein.class);
        long nextPeriod = actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().position();

            if (moveToOreBlob(world, blobTarget.get(), scheduler))
            {
                Quake quake = Quake.createQuake(ImageStore.getImageList(imageStore, QUAKE_KEY), tgtPos);
                world.addEntity(quake);
                nextPeriod += actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore, 0),
                actionPeriod);
        scheduler.scheduleEvent(this,
                new Animation(this, null, null,0), getAnimationPeriod());
    }


    private boolean moveToOreBlob(WorldModel world, EntityInterface target, EventScheduler scheduler) {
        if (target.position().adjacent(position)) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = nextPos(target.position(), world);

            if (!position.equals(nextPos)) {
                Optional<EntityInterface> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                move(world, nextPos);
            }
            return false;
        }
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




    public Point nextPos(Point point, WorldModel worldModel){
        int horiz = Integer.signum(point.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        Optional<EntityInterface> occupant = worldModel.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !((occupant.get()).getClass() == Ore.class)))
        {
            int vert = Integer.signum(point.y - position.y);
            newPos = new Point(position.x, position.y + vert);
            occupant = worldModel.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getClass() == Ore.class )))
            {
                newPos = position;
            }
        }

        return newPos;
    }

    public int getAnimationPeriod()
    {


                return animationPeriod;

    }

    public PImage getCurrentImage()
    {

            return (this.images.get(this.imageIndex));

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
