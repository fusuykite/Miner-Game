import java.awt.*;

public class Animation implements ActionInterface{

    private AnimPeriod entity;
    private WorldModel worldModel;
    private ImageStore imageStore;
    private int repeatCount;

    public Animation(AnimPeriod entity, WorldModel worldModel, ImageStore imageStore, int repeatCount){
        this.entity = entity;
        this.worldModel = worldModel;
        this.imageStore = imageStore;
        this. repeatCount = repeatCount;
    }

    public static Animation createAnimationAction(AnimPeriod entity, int repeatCount)
    {
        return new Animation( entity, null, null, repeatCount);
    }

    public void execute(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    createAnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }


}
