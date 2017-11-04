public class Activity extends ActionAb{
    protected WorldModel world;
    protected ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore){
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }


    @Override
    public void executeAction(EventScheduler scheduler)
    {
        executeActivityAction(scheduler);
    }

    public void executeActivityAction(EventScheduler scheduler)
    {

            if (entity instanceof MINER_FULL) {
                ((MINER_FULL)entity).executeMinerFullActivity(entity, world,
                        imageStore, scheduler);
            }
            if (entity instanceof MINER_NOT_FULL) {
                ((MINER_NOT_FULL)entity).executeMinerNotFullActivity(entity, world,
                        imageStore, scheduler);
            }

            if (entity instanceof ORE) {
                ((ORE)entity).executeOreActivity(scheduler, entity, world, imageStore);
            }

            if (entity instanceof ORE_BLOB) {
                ((ORE_BLOB)entity).executeOreBlobActivity(scheduler, entity, world,
                        imageStore);
            }

            if (entity instanceof Quake) {
                ((Quake)entity).executeQuakeActivity(scheduler, entity, world);
            }

            if (entity instanceof VEIN) {
                ((VEIN)entity).executeVeinActivity(entity, world, imageStore, scheduler);
            }
    }

    public static Action createActivityAction(Entity entity, WorldModel world,
                                              ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }
}
