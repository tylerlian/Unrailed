import processing.core.PImage;

import java.util.List;

import static processing.core.PConstants.CODED;

public abstract class OctoEntity extends TraversingEntity{

    public OctoEntity(String id, Point position,
                       List<PImage> images, String type, int actionPeriod, int animationPeriod){
        super(id, position, images, type, actionPeriod, animationPeriod);
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
//        return destPos;
//        return destPos;


    }

}