import java.util.List;
import java.util.Optional;

import processing.core.PImage;

final class Background
{
   protected String id;
   protected List<PImage> images;
   protected int imageIndex;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   public static PImage getCurrentImage(Object entity)
   {
         return ((Background)entity).images
                 .get(((Background)entity).imageIndex);
   }



}
