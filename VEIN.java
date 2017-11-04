import processing.core.PImage;

import java.util.*;

public class VEIN extends Activity_AB {





    public VEIN(String id, Point position, int actionPeriod, List<PImage> images){

        super(id, position, images, 0, actionPeriod);
    }




    public void executeVeinActivity(Entity entity, WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(entity.getPosition());

        if (openPt.isPresent())
        {
            Entity ore = new ORE(ORE_ID_PREFIX + id,
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ((ORE)ore).scheduleActions(world, scheduler, imageStore);
        }

        scheduler.scheduleEvent(entity,
                Activity.createActivityAction(entity, world, imageStore),
                actionPeriod);
    }

}
