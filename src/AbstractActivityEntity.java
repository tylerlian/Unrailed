import processing.core.PImage;

import java.util.List;

public abstract class AbstractActivityEntity extends AbstractEntity implements ActivityEntity{

    private int actionPeriod;

    public AbstractActivityEntity(String id, Point position, List<PImage> images,
                                  int imageIndex, String type, int actionPeriod){
        super(id, position, images, imageIndex, type);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod(){
        return actionPeriod;
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) { }
}
