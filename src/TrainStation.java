import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class TrainStation extends AbstractEntity {

    public TrainStation(String id, Point position, List<PImage> images) {
        super(id, position, images, 0, "trainstation");
    }

}
