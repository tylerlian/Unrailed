import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class OctoNotFull extends OctoEntity{

    private int resourceLimit;
    private int resourceCount;

    public OctoNotFull(String id, Point position, List<PImage> images,
                       int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images,"octo", actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = 0;
    }

   public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
         "fish");

      if (!notFullTarget.isPresent() ||
         !this.moveTo(world, notFullTarget.get(), scheduler) ||
         !this.transform(world, scheduler, imageStore))
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
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionOcto(world, target.getPosition());

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


    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            OctoFull octo = imageStore.createOctoFull(this.getId(), this.resourceLimit,
                    this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


}
