import processing.core.PImage;

import java.util.List;

public abstract class OctoEntity extends TraversingEntity{

    public OctoEntity(String id, Point position,
                       List<PImage> images, String type, int actionPeriod, int animationPeriod){
        super(id, position, images, type, actionPeriod, animationPeriod);
    }

    public Point nextPositionOcto(WorldModel world,  Point destPos)
    {
//        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
//        Point newPos = new Point(this.getPosition().getX() + horiz,
//                this.getPosition().getY());
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        {
//            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(),
//                    this.getPosition().getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;

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