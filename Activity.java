public class Activity extends ActionAb {
    protected WorldModel world;
    protected ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        executeActivityAction(scheduler);
    }

    public void executeActivityAction(EventScheduler scheduler) {

        MINER_FULL_Visit miner_full_visit = new MINER_FULL_Visit();
        MINER_NOT_FULL_Visit miner_not_full_visit = new MINER_NOT_FULL_Visit();
        ORE_Visit ore_visit = new ORE_Visit();
        ORE_BLOB_Visit ore_blob_visit = new ORE_BLOB_Visit();
        Quake_Visit quake_visit = new Quake_Visit();
        VEIN_Visit vein_visit = new VEIN_Visit();

        if (entity.accept(miner_full_visit)) {
            ((MINER_FULL)entity).execute(world,
                    imageStore, scheduler);
        }
        if (entity.accept(miner_not_full_visit)) {
            ((MINER_NOT_FULL)entity).execute(world,
                    imageStore, scheduler);
        }

        if (entity.accept(ore_visit)) {
            ((ORE)entity).execute(world, imageStore, scheduler);
        }

        if (entity.accept(ore_blob_visit)) {
            ((ORE_BLOB)entity).execute( world,
                    imageStore, scheduler);
        }

        if (entity.accept(quake_visit)) {
            ((Quake)entity).execute(world, imageStore, scheduler);
        }

        if (entity.accept(vein_visit)) {
            ((VEIN)entity).execute(world, imageStore, scheduler);
        }
    }


    public static Action createActivityAction(Entity entity, WorldModel world,
                                              ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }
}

