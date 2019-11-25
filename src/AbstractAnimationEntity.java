import processing.core.PImage;
import java.util.List;

public abstract class AbstractAnimationEntity extends AbstractActivityEntity implements AnimationEntity {

    private int animationPeriod;

    public AbstractAnimationEntity(String id, Point position,
                                   List<PImage> images, String type, int actionPeriod, int animationPeriod){

        super(id, position, images, 0, type, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, repeatCount);
    }

    public void nextImage()
    {
        this.newImageIndex((this.getImageIndex()+ 1) % this.getImages().size());
    }

}
