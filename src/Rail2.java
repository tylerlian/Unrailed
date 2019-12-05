import java.util.Optional;
import processing.core.PImage;
import java.util.List;
import java.util.Random;
public class Rail2 extends Rail{

    public Rail2(String id, Point position, List<PImage> images){
        super(id, position, images, 0, "rail2");
    }

    public String toString() {
        return "(" + this.getPosition().getX() + "," + this.getPosition().getY() + ")";
    }
}
