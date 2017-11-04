import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ORE extends Activity_AB {




    public ORE(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, 0, actionPeriod);
    }


    public void executeOreActivity(EventScheduler scheduler, Entity entity, WorldModel world, ImageStore imageStore) {
        Point pos = entity.getPosition();  // store current position before removing

        world.removeEntity(entity);
        scheduler.unscheduleAllEvents(entity);

        Entity blob = ORE_BLOB.createOreBlob(id + BLOB_ID_SUFFIX,
                pos, actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        ((ORE_BLOB)blob).scheduleActions(world, scheduler, imageStore);
    }




}
