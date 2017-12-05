import processing.core.PImage;

import java.util.List;

public abstract class Animation_AB extends Activity_AB {

    protected static final String QUAKE_KEY = "quake";

    protected int animationPeriod;

    protected static final int QUAKE_ACTION_PERIOD = 1100;
    protected static final int QUAKE_ANIMATION_PERIOD = 100;
    protected static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    protected static final String QUAKE_ID = "quake";


    public Animation_AB(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod){
        super(id, position, images, imageIndex, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod(){
        return animationPeriod;
    }

    public void nextImage()
    {
        imageIndex = (imageIndex + 1) % images.size();
    }
}
