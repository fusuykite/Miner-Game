import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Quake implements AnimPeriod {


    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private Random rand;

    public Quake(String id, Point position, List<PImage> images,
                 int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }



    public static Quake createQuake(List<PImage> images, Point position)
    {
        return new Quake(QUAKE_ID, position, images,0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }


    public void execute(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler){
        scheduler.unscheduleAllEvents(this);
        worldModel.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore,0),
                actionPeriod);
        scheduler.scheduleEvent(this,
                new Animation(this, null, null, QUAKE_ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());
    }


    public int getAnimationPeriod()
    {
                return animationPeriod;

    }

    public PImage getCurrentImage()
    {
            return (this.images.get(this.imageIndex));

    }

    public Point position(){
        return position ;
    }

    public void setPosition(Point position){
        this.position = position;

    }

    public void nextImage()
    {
        imageIndex = (imageIndex + 1) % images.size();
    }





}
