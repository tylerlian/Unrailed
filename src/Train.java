import processing.core.PImage;

import java.util.HashSet;
import java.util.List;

import java.util.Optional;
import java.util.Random;


public class Train extends TraversingEntity {

    HashSet<Point> closed;

    public Train(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, "crab", actionPeriod, animationPeriod);
    }

//    public void move(WorldModel world, Point pos) {
//        if (world.withinBounds(pos) && !(world.isOccupied(pos))) {
//            this.setPosition(pos);
//        }
//    }

    public Optional<Point> findTrackAround(WorldModel world, Point pos) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (world.withinBounds(newPt) &&
                        world.isOccupied(newPt)) {
                    Object o = world.getOccupant(newPt);
                    if (o instanceof Sgrass) {
                        return Optional.of(newPt);
                    }
                }
            }
        }
        return Optional.empty();
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Entity> crabTarget = world.findNearest(
//                this.getPosition(), "seaGrass");
        Optional<Point> track = findTrackAround(world, this.getPosition());
        long nextPeriod = this.getActionPeriod();
        if(track.isPresent());


//        if (crabTarget.isPresent())
        {
//            Point tgtPos = crabTarget.get().getPosition();
            this.moveTo(world, track.get(), scheduler);
//            {
//                ActivityEntity quake = imageStore.createQuake(track.get(),
//                        imageStore.getImageList("quake"));
//
//                world.addEntity((Entity) quake);
//                nextPeriod += this.getActionPeriod();
//                quake.scheduleActions(scheduler, world, imageStore);
//            }
        }

        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), nextPeriod);

    }

    private boolean moveTo(WorldModel world, Point rail, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(rail))
        {
//            world.removeEntity(target);
//            scheduler.unscheduleAllEvents(target);
//            return true;
            world.moveEntity(this, rail);
            return true;
        } else {
            return false;
        }
//        else
//        {
//            Point nextPos = this.nextPosition(world, target.getPosition());
//
//            if (!this.getPosition().equals(nextPos))
//            {
//                Optional<Entity> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent())
//                {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
//
//                world.moveEntity(this, nextPos);
//            }
//            return false;
        }

//    private Point nextPosition(WorldModel world, Point destPos)
//    {
//
////        PathingStrategy aStar = new AStarPathingStrategy();
//
////        List<Point> path = aStar.computePath(getPosition(), destPos, p -> (!world.isOccupied(p) && world.withinBounds(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
//
//        if(findTrackAround(world, this.getPosition()).isPresent() == true){
//            return destPos;
//        } else {
//            return getPosition();
//        }
//    }

}


//public class Train2 extends ImmobileEntity{
//
//    public Train2(String id, Point position, List<PImage> images, int actionPeriod){
//        super(id, position, images, 0, "seaGrass", actionPeriod);
//    }
//
//
//    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Point> openPt = world.findTrackAround(this.getPosition());
//
//        if (openPt.isPresent())
//        {
//            ActivityEntity fish = imageStore.createFish("fish -- " + this.getId(),
//                    openPt.get(), 20000 +
//                            (new Random()).nextInt(30000 - 20000),
//                    imageStore.getImageList("fish"));
//            world.addEntity((Entity) fish);
//            fish.scheduleActions(scheduler, world, imageStore);
//        }
//        scheduler.scheduleEvent(this,
//                this.createActivityAction( world, imageStore),
//                this.getActionPeriod());
//    }
//
//}
//
