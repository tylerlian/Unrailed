import processing.core.PImage;
import java.util.List;
import java.util.Optional;


public class OctoFull extends OctoEntity {

    private int resourceLimit;
    private int resourceCount;

    public OctoFull(String id, Point position, List<PImage> images,
                    int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, "octo", actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), "atlantis");

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((ActivityEntity)fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
   {
      if (this.getPosition().adjacent(target.getPosition()))
      {
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionOcto( world, target.getPosition());

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

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        AnimationEntity octo = (AnimationEntity) imageStore.createOctoNotFull(this.getId(), this.resourceLimit,
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity((Entity) octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }

}
