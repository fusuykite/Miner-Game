import processing.core.PImage;

public interface Entity {

    void setPosition(Point p);

    PImage getCurrentImage();
    Point getPosition();
    void nextImage();

    public abstract <R> R accept(EntityVisitor<R> visitor);

}
