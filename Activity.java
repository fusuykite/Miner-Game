import processing.core.PImage;

import java.awt.*;
import java.util.*;


public class Activity implements ActionInterface{
    private Executable entity;
    private WorldModel world;
    private ImageStore imagestore;
    private int repeatCount;

    public Activity(Executable entity, WorldModel world, ImageStore imageStore, int repeatCount){
        this.entity = entity;
        this.world = world;
        this.repeatCount = repeatCount;
        this.imagestore = imageStore;

    }

    public static Activity createActivityAction(Executable entity, WorldModel world,
                                                            ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }


    public void execute(EventScheduler scheduler)
    {
        if (entity instanceof Miner_Full)
            ((Miner_Full) entity).execute(world,
                        imagestore, scheduler);


        if (entity instanceof Miner_Not_Full)
            ((Miner_Not_Full) entity).execute(world,
                        imagestore, scheduler);

        if (entity instanceof Ore)
            ((Ore) entity).execute(world, imagestore,
                        scheduler);

        if (entity instanceof Ore_Blob)
            ((Ore_Blob) entity).execute(world,
                        imagestore, scheduler);


         if (entity instanceof Quake)
             ((Quake) entity).execute(world, imagestore,
                        scheduler);

         if (entity instanceof Vein)
             ((Vein) entity).execute(world, imagestore,
                        scheduler);

    }


}

