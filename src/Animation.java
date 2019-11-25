public class Animation implements Action{
    private Entity entity;
    private int repeatCount;

    public Animation(Entity entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        ((AnimationEntity)this.entity).nextImage();
        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    ((AnimationEntity)this.entity).createAnimationAction(Math.max(this.repeatCount - 1, 0)),
                    ((AnimationEntity)this.entity).getAnimationPeriod());
    }

        }
    }

