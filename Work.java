import processing.core.PImage;

public interface Work {

    void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

    Point getPosition();


    void setPosition(Point p);



}
