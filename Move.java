
import processing.core.PImage;

public interface Move extends Animated, Work {

    Point getPosition();

    int getAnimationPeriod();

    void setPosition(Point p);

    void nextImage();

    void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

    PImage getCurrentImage();
}
