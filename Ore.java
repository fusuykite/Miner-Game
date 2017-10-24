import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;



public class Ore implements Executable{

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private Random rand;
    private int animationPeriod;


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



    public Ore(String id, Point position, List<PImage> images,
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


    public static Ore createOre(String id, int actionPeriod, List<PImage> images, Point position)
    {
        return new Ore(id, position, images, 0, 0,
                actionPeriod, 0);
    }


    public void execute(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler){
        Point pos = position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Ore_Blob blob = Ore_Blob.createOreBlob(id + BLOB_ID_SUFFIX,
                actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                ImageStore.getImageList(imageStore, BLOB_KEY), pos);

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }



    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore, 0),
                actionPeriod);
    }



    public Point position(){
        return position ;
    }

    public void setPosition(Point position){
        this.position = position;

    }

    public PImage getCurrentImage()
    {

        return (this.images.get(this.imageIndex));

    }
}
