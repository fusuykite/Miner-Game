import processing.core.PImage;

import java.util.*;

public class Ornaments extends Activity_AB {





    public Ornaments(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, 0, actionPeriod);
    }



    public <R> R accept(EntityVisitor<R> visitor)
    {
        return visitor.visit(this);
    }


}
