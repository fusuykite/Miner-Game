import processing.core.PImage;

import java.util.List;

public abstract class EntityAb implements Entity{
    protected String id;
    protected Point position;
    protected List<PImage> images;
    protected int imageIndex;

    public EntityAb(String id, Point position, List<PImage> images, int imageIndex){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
    }

    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }

    public void setImages(List<PImage> images){this.images = images;}

    public Point getPosition(){
        return position;
    }

    public void setPosition(Point p){
        position = p;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public String getId(){
        return id;
    }

    public List<PImage> getImages() {
        return images;

    }

    public void nextImage()
    {
        imageIndex = (imageIndex + 1) % images.size();
    }


}
