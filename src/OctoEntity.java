import processing.core.PImage;

import java.util.List;
import java.util.Optional;

import static processing.core.PConstants.CODED;

public class OctoEntity extends TraversingEntity {

    private int resourceCount;
    private int resourceLimit = 1;
    private int railCount = 0;
    private int railLimit = 1;
    private Point newRailPos;

    public OctoEntity(String id, Point position,
                      List<PImage> images, String type, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, "octo", 0, animationPeriod);
        this.resourceCount = resourceCount;
        this.newRailPos = new Point(position.getX()+1, position.getY());
    }

    public void move(WorldModel world, int dx, int dy) {
        Point pos = new Point(this.getPosition().getX() + dx, this.getPosition().getY() + dy);
        Point railPos = new Point(this.getPosition().getX() + dx + 1, this.getPosition().getY() + dy);
        if (world.withinBounds(pos) && !(world.isOccupied(pos))) {
            this.setPosition(pos);
            newRailPos = railPos;
        }
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                "fish");

        if (this.railCount > 0) {
            Rail1 rail = imageStore.createRail1("rail1", this.getPosition(), imageStore.getImageList("rail1"));
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
