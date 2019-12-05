import processing.core.PImage;

import java.util.List;

public class RailFactory{
    private ImageStore imageStore;
    private Point pos;
    private RailType model;

    public RailFactory(Point pos, ImageStore imageStore, RailType model){
        this.pos = pos;
        this.model = model;
        this.imageStore = imageStore;
    }

    public static Rail buildRail(Point pos, ImageStore images, RailType model) {
        Rail rail = null;
        switch (model) {
            case RAIL1:
                return new Rail1("rail1", pos, images.getImageList("rail1"), 0, "rail1");

            case RAIL2:
                return new Rail2("rail2", pos, images.getImageList("rail2"), 0, "rail2");

            default:
                break;
        }
        return rail;
    }

}
