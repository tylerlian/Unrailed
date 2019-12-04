import processing.core.PImage;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import java.util.Optional;


public class Crab extends TraversingEntity {

    private List<Point> closed;

    public Crab(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, "crab", actionPeriod, animationPeriod);
        this.closed = new LinkedList<>();
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> crabTarget = findTrackAround(world);

        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get();
            this.moveTo(world, tgtPos);
        }

        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), nextPeriod);

    }

    public Optional<Point> findTrackAround(WorldModel world) {
        Optional<Entity> crabTarget = world.findNearestTrack(
            this.getPosition(), "seaGrass", closed);
        if(crabTarget.isPresent()){
            Point rail = crabTarget.get().getPosition();
            if(this.getPosition().adjacent(rail)){
                return Optional.of(rail);
            }
        }
        return Optional.empty();
    }


    private void moveTo(WorldModel world, Point railPos)
    {
        world.moveEntity(this, railPos);
        closed.add(0, railPos);
    }
}
