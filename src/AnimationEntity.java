import processing.core.PImage;

import java.util.List;

interface AnimationEntity extends ActivityEntity
{
    void nextImage();
    int getAnimationPeriod();
    Action createAnimationAction(int repeatCount);
}