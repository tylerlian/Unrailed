import processing.core.PImage;

import java.util.List;

public abstract class AtlantisEntity extends AbstractAnimationEntity{

    public AtlantisEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceCount, int resourceLimit){
        super(id, position, images, "atlantis", actionPeriod, animationPeriod);
    }

    public Point nextPositionOcto(WorldModel world,  Point destPos)
    {
        PathingStrategy aStar = new AStarPathingStrategy();

        List<Point> path = aStar.computePath(getPosition(), destPos, p -> (!world.isOccupied(p) && world.withinBounds(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0){
            return getPosition();
        } else {
            Point next = path.get(0);
            path.remove(0);
            return next;
        }

    }

}


