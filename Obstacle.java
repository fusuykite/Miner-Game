import java.util.List;
import java.util.Optional;
import java.util.Random;
import processing.core.PImage;


public class Obstacle implements EntityInterface{

    private Random rand;
    private String id;
    private int actionPeriod;
    private Point position;
    private int resourceCount;
    private List<PImage> images;
    private int resourceLimit;
    private int imageIndex;
    private int animationPeriod;

    public Obstacle(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
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

    public static Obstacle createObstacle(String id, List<PImage> images, Point position)
    {
        return new Obstacle(id, position, images,
                0, 0, 0, 0);
    }

}
