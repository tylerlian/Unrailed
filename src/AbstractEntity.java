import processing.core.PImage;

import java.util.List;

public abstract class AbstractEntity implements Entity{

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private String type;

    public AbstractEntity(String id, Point position, List<PImage> images, int imageIndex, String type){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
        this.type = type;
    }

    public String getId(){
        return id;
    }
    public Point getPosition() { return position; }
    public List<PImage> getImages() { return images; }
    public int getImageIndex() { return imageIndex; }
    public String getType(){ return type; }
    public PImage getCurrentImage(Entity entity) { return entity.getImages().get(entity.getImageIndex()); }

    public void setPosition(Point point){ position = point; }
    protected void newImageIndex(int integer) { imageIndex = integer; }
}
