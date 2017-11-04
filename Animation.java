public class Animation extends ActionAb{
    protected int repeatCount;

    public Animation(Entity entity, int repeatCount){
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    @Override
    public void executeAction(EventScheduler scheduler)
    {
        executeAnimationAction(scheduler);

    }


    public static Action createAnimationAction(Entity entity, int repeatCount)
    {
        return new Animation(entity, repeatCount);
    }

    protected void executeAnimationAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    Animation.createAnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    ((Animated)entity).getAnimationPeriod());
        }
    }


}
