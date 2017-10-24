import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;


public class BlackSmith implements EntityInterface {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private Random rand;


    public BlackSmith(String id, Point position, List<PImage> images,
                      int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.imageIndex = 0;
        this.images = images;
        this.animationPeriod = animationPeriod;
    }

    public PImage getCurrentImage() {
        return (this.images.get(this.imageIndex));
    }

    public Point position() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public static BlackSmith createBlacksmith(String id, List<PImage> images, Point position) {
        return new BlackSmith(id, position, images,
                0, 0, 0, 0);
    }
}


