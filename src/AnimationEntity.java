import processing.core.PImage;

import java.util.List;

interface AnimationEntity extends ActivityEntity
{
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    int getActionPeriod();
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    String getId();
    Point getPosition();
    void setPosition(Point point);
    List<PImage> getImages();
    int getImageIndex();
    String getType();
    PImage getCurrentImage(Entity entity);

    void nextImage();
    int getAnimationPeriod();
    Action createAnimationAction(int repeatCount);
}