import processing.core.PImage;

import java.util.List;

import static processing.core.PConstants.CODED;

public class OctoEntity extends PlayerEntity{

    public OctoEntity(String id, Point position,
                       List<PImage> images, String type, int actionPeriod, int animationPeriod){
        super(id, position, images, "octo", 0, animationPeriod);
    }

//    public Point nextPositionOcto(WorldModel world,  Point destPos)
//    {
//        PathingStrategy aStar = new AStarPathingStrategy();
//
//        List<Point> path = aStar.computePath(getPosition(), destPos, p -> (!world.isOccupied(p) && world.withinBounds(p)), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
//
//        if (path.size() == 0){
//            return getPosition();
//        } else {
//            Point next = path.get(0);
//            path.remove(0);
//            return next;
//        }
//
//    }

//    public void move(WorldModel world, int dx, int dy){
//        Point pos = new Point(this.getPosition().getX() + dx, this.getPosition().getY() + dy );
//        if(world.withinBounds(pos) && !(world.isOccupied(pos)))
//        {
//            this.setPosition(pos);
//        }
//    }
}