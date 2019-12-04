import java.util.Optional;
import processing.core.PImage;
import java.util.List;
import java.util.Random;
public class Sgrass extends ImmobileEntity{

    public Sgrass(String id, Point position, List<PImage> images, int actionPeriod){
        super(id, position, images, 0, "seaGrass", actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

      if (openPt.isPresent())
      {
         ActivityEntity fish = imageStore.createFish("fish" + this.getId(),
                 openPt.get(), 20000 +
                         (new Random()).nextInt(30000 - 20000),
                 imageStore.getImageList("fish"));
         world.addEntity((Entity) fish);
         fish.scheduleActions(scheduler, world, imageStore);
      }
      scheduler.scheduleEvent(this,
         this.createActivityAction( world, imageStore),
         this.getActionPeriod());
    }
}
