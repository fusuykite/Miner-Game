import processing.core.PImage;
import java.util.List;

public class BlackSmith extends EntityAb {


    public BlackSmith(String id, Point position, List<PImage> images){
        super(id, position, images, 0);
    }


    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }


}
