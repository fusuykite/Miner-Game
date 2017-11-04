import processing.core.PImage;

public interface Animated extends Entity{
    void setPosition(Point p);

    void nextImage();
    int getAnimationPeriod();
    PImage getCurrentImage();
    Point getPosition();
}
