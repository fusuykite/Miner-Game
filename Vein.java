import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;


public class Vein implements Executable {

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

    private static final String ORE_KEY = "ore";

    private static final String ORE_ID_PREFIX = "ore -- "; //
    private static final int ORE_CORRUPT_MIN = 20000; //
    private static final int ORE_CORRUPT_MAX = 30000; //
    private static final int ORE_REACH = 1; //

    private static final String QUAKE_KEY = "quake"; //



    public Vein(String id, Point position, List<PImage> images,
                int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.resourceCount = resourceCount;
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;

    }

    public static Vein createVein(String id, int actionPeriod, List<PImage> images, Point position)
    {
        return new Vein(id, position, images, 0, 0,
                actionPeriod, 0);
    }


    public void execute(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler){
        Optional<Point> openPt = worldModel.findOpenAround(position);

        if (openPt.isPresent())
        {
            Ore ore = Ore.createOre(ORE_ID_PREFIX + id,
                    ORE_CORRUPT_MIN + rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    ImageStore.getImageList(imageStore, ORE_KEY), openPt.get());
            worldModel.addEntity(ore);
            ore.scheduleActions(scheduler, worldModel, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, worldModel, imageStore),
                actionPeriod);
    }


    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore, 0),
                actionPeriod);
    }

    public void setPosition(Point position){
        this.position = position;

    }

    public PImage getCurrentImage()
    {

        return (this.images.get(this.imageIndex));

    }


    public Point position(){
        return position ;
    }



}
