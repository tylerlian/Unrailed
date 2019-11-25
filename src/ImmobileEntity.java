import processing.core.PImage;

import java.util.List;

public abstract class ImmobileEntity extends AbstractActivityEntity{

    public ImmobileEntity(String id, Point position, List<PImage> images,
                        int imageIndex, String type, int actionPeriod) {
        super(id, position, images, imageIndex, type, actionPeriod );
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
    }

}
