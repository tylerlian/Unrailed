import processing.core.PImage;
import java.util.List;
import java.util.Optional;
public class AtlantisFull extends AbstractAnimationEntity{

    public AtlantisFull(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceCount, int resourceLimit){
        super(id, position, images, "atlantis", actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                this.createAnimationAction(7),
                this.getAnimationPeriod());
    }
}
