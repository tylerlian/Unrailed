import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Fish extends ImmobileEntity{

    public Fish(String id, Point position, List<PImage> images, int actionPeriod){
        super(id, position, images, 0, "fish", actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = this.getPosition();  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      AnimationEntity crab = imageStore.createCrab(this.getId() + " -- crab",
              pos, this.getActionPeriod() / 4,
              50 +
                      (new Random()).nextInt(150 - 50),
              imageStore.getImageList("crab"));

      world.addEntity((Entity) crab);
      crab.scheduleActions(scheduler, world, imageStore);
    }

}
