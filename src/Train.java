import processing.core.PImage;

//import java.util.*;
//import java.util.function.BiPredicate;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class Train extends TraversingEntity implements PathingStrategy{
//
//    public Train(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
//        super(id, position, images, "train", actionPeriod, animationPeriod);
//    }
//
//    public List<Point> computePath(Point current,
//                                   Predicate<Point> canPassThrough,
//                                   Function<Point, Stream<Point>> potentialNeighbors)
//    {
//        Comparator<AStarPoint> f = Comparator.comparing(AStarPoint::getF).reversed();
//        HashSet<Point> closed = new HashSet<>();
//        PriorityQueue search = new PriorityQueue<>(f);
//
//        search.add(current);
//
//        List<Point> neighboringPoints = potentialNeighbors.apply(current)
//                .filter(p -> !closed.contains(p))
//                .filter()
//                .collect(Collectors.toList());
//
//        // add current point to closed so not returned to
//        closed.add(current);
//
//        // if no path return empty list
//        if(search.isEmpty()) { return new LinkedList(); }
//    }
//}
