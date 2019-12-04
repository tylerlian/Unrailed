import processing.core.PImage;

import java.util.List;
public class Sgrass extends ImmobileEntity{

    public Sgrass(String id, Point position, List<PImage> images, int actionPeriod){
        super(id, position, images, 0, "seaGrass", actionPeriod);
    }

<<<<<<< HEAD
    public String toString(){
        return "(" + this.getPosition().getX() + ", " + this.getPosition().getY() + ")";
    }
//    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Point> openPt = world.findOpenAround(this.getPosition());
//
//      if (openPt.isPresent())
//      {
//         ActivityEntity fish = imageStore.createFish("fish" + this.getId(),
//                 openPt.get(), 20000 +
//                         (new Random()).nextInt(30000 - 20000),
//                 imageStore.getImageList("fish"));
//         world.addEntity((Entity) fish);
//         fish.scheduleActions(scheduler, world, imageStore);
//      }
//      scheduler.scheduleEvent(this,
//         this.createActivityAction( world, imageStore),
//         this.getActionPeriod());
//    }
=======
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
>>>>>>> parent of 6da6b3e... fixed trees
}
