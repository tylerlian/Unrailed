import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Tree extends AbstractEntity{

    public Tree(String id, Point position, List<PImage> images){
        super(id, position, images, 0, "tree");
    }
}
