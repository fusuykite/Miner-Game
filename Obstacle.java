import processing.core.PImage;
import sun.nio.cs.ext.IBM037;

import java.util.List;

public class Obstacle extends EntityAb {

    public Obstacle(String id, Point position, List<PImage> images){
        super(id, position, images, 0);
    }

    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }


}
