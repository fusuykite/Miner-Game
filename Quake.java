import processing.core.PImage;

import java.util.List;

public class Quake extends  Animation_AB implements Animated, Work{



    public Quake(String id, Point position, List<PImage> images,  int actionPeriod, int animationPeriod) {
        super(id, position, images, 0, actionPeriod, animationPeriod);
    }



    public static Entity createQuake(Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images,
                QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public void execute( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }


}
