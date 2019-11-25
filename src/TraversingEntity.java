import processing.core.PImage;

import java.util.List;

public abstract class TraversingEntity extends AbstractAnimationEntity {

    public TraversingEntity(String id, Point position,
                        List<PImage> images, String type, int actionPeriod, int animationPeriod){
        super(id, position, images, type, actionPeriod, animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this, this.createAnimationAction(0),
                this.getAnimationPeriod());
    }
}
