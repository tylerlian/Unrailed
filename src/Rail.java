import java.util.Optional;
import processing.core.PImage;
import java.util.List;
import java.util.Random;
public abstract class Rail extends AbstractEntity{

    public Rail(String id, Point position, List<PImage> images, int imageIndex, String type){
        super(id, position, images, imageIndex, type);
    }

    public abstract String toString();
}
