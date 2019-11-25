import java.awt.*;

public class Activity implements Action {
    private ActivityEntity entity;
    private ImageStore imageStore;
    private WorldModel worldModel;

    public Activity(ActivityEntity entity, WorldModel worldModel, ImageStore imageStore){
        this.entity = entity;
        this.imageStore = imageStore;
        this.worldModel = worldModel;
    }

    public void executeAction(EventScheduler scheduler) {
        (this.entity).executeActivity(worldModel,imageStore,scheduler);
    }
}
