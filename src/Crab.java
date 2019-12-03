import processing.core.PImage;
import java.util.List;

import java.util.Optional;


public class Crab extends TraversingEntity {

    public Crab(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, "crab", actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> crabTarget = world.findNearest(
            this.getPosition(), "octo");
        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (this.moveTo(world, crabTarget.get(), scheduler))
            {
                ActivityEntity quake = imageStore.createQuake(tgtPos,
                        imageStore.getImageList("quake"));

                world.addEntity((Entity) quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), nextPeriod);

    }

    private boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private Point nextPosition(WorldModel world, Point destPos)
    {
        PathingStrategy aStar = new AStarPathingStrategy();

        List<Point> path = aStar.computePath(getPosition(), destPos, p -> (!world.isOccupied(p) && world.withinBounds(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0){
            return getPosition();
        } else {
            Point next = path.get(0);
            path.remove(0);
            return next;
        }
    }
}
