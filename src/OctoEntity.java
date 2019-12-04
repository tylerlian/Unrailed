import processing.core.PImage;

import java.util.List;
import java.util.Optional;

import static processing.core.PConstants.CODED;

public class OctoEntity extends TraversingEntity {

    private int resourceCount = 0;
    private int resourceLimit = 1;
    private int railCount = 0;
    private int railLimit = 1;
    private Point newRailPos;

    public OctoEntity(String id, Point position,
                      List<PImage> images, String type, int actionPeriod, int animationPeriod) {
        super(id, position, images, "octo", 0, animationPeriod);
        this.newRailPos = new Point(position.getX()+1, position.getY());
    }

    public Point nextPositionOcto(WorldModel world, Point destPos) {
        PathingStrategy aStar = new AStarPathingStrategy();

        List<Point> path = aStar.computePath(getPosition(), destPos, p -> (!world.isOccupied(p) && world.withinBounds(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0) {
            return getPosition();
        } else {
            Point next = path.get(0);
            path.remove(0);
            return next;
        }
    }

    public void move(WorldModel world, int dx, int dy) {
//        System.out.println("I'm in move");
        Point pos = new Point(this.getPosition().getX() + dx, this.getPosition().getY() + dy);
        Point railPos = new Point(this.getPosition().getX() + dx + 1, this.getPosition().getY() + dy);
        if (world.withinBounds(pos) && !(world.isOccupied(pos))) {
            this.setPosition(pos);
            newRailPos = railPos;
        }
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        System.out.println("Activity activated");
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                "fish");

        if (this.railCount > 0) {
            Sgrass rail = imageStore.createSgrass("seaGrass", this.getPosition(), 0, imageStore.getImageList("seaGrass"));
            world.addEntity(rail);
            this.railCount -= 1;
            return;
        }

        if (resourceCount < resourceLimit) {
            notFullTarget = world.findNearest(this.getPosition(),
                    "fish");
        } else if (resourceCount == resourceLimit) {
            notFullTarget = world.findNearest(this.getPosition(), "atlantis");
        }

        if (notFullTarget.isPresent() && resourceCount < resourceLimit) {
            if (this.getPosition().adjacent(notFullTarget.get().getPosition())) {
                this.resourceCount += 1;
                world.removeEntity(notFullTarget.get());
                scheduler.unscheduleAllEvents(notFullTarget.get());
            }
        } else if (notFullTarget.isPresent() && resourceCount == resourceLimit) {
            if (this.getPosition().adjacent(notFullTarget.get().getPosition())) {
                ((ActivityEntity) notFullTarget.get()).scheduleActions(scheduler, world, imageStore);
                this.resourceCount -= 1;
                if (this.railCount == 0) {
                    this.railCount += 1;
                }
            }
        }

    }
}
