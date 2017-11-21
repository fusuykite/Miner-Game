import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class Activity_AB extends EntityAb {


        protected static final String BLOB_KEY = "blob";
        protected static final String BLOB_ID_SUFFIX = " -- blob";
        protected static final int BLOB_PERIOD_SCALE = 4;
        protected static final int BLOB_ANIMATION_MIN = 50;
        protected static final int BLOB_ANIMATION_MAX = 150;


        protected static final String ORE_ID_PREFIX = "ore -- ";
        protected static final int ORE_CORRUPT_MIN = 20000;
        protected static final int ORE_CORRUPT_MAX = 30000;
        protected static final String ORE_KEY = "ore";

        protected int actionPeriod;
        protected int imageIndex = 0;
        protected static final Random rand = new Random();

        public Activity_AB(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod){
            super(id, position, images, imageIndex);
            this.actionPeriod = actionPeriod;
        }


        public abstract <R> R accept(EntityVisitor<R> visitor);

        public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    actionPeriod);

        }




    }


