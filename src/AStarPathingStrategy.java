import java.util.*;
import java.util.HashSet;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        // initialize lists to hold values
        Comparator<AStarPoint> f = Comparator.comparing(AStarPoint::getF).reversed();
        HashSet<Point> closed = new HashSet<>();
        PriorityQueue search = new PriorityQueue<>(f);

        // add starting point to searching list
        AStarPoint current = new AStarPoint(start,end,null);
        search.add(current);

        // loop through all points that are not closed while current is not within reach of end
        while(!withinReach.test(current.getPos(), end)) {

            // filter out neighboring points of current
            List<Point> neighboringPoints = potentialNeighbors.apply(current.getPos())
                    .filter(p -> !closed.contains(p))
                    .filter(canPassThrough)
                    .collect(Collectors.toList());

            // add current point to closed so not returned to
            closed.add(current.getPos());

            // make all neighboringPoints into AStarPoints (with f,g,h values)
            for(Point p : neighboringPoints){
                search.add(new AStarPoint(p, end, current));
            }

            // if no path return empty list
            if(search.isEmpty()) { return new LinkedList(); }


            // variable used in lambda expression should be final
            AStarPoint lamVar = current;

            // gets beginning of the path
            List<AStarPoint> temp = new LinkedList<>();

            while(!search.isEmpty()){
                temp.add((AStarPoint) search.poll());
            }

            current = temp.get(temp.size()-1);

            // filter list so any of the same position is removed
            temp = temp.stream()
                    .filter(n -> !n.getPos().equals(lamVar.getPos()) && !n.equals(end))
                    .collect(Collectors.toList());

            for(int i = 0; i < temp.size(); i++){
                search.add(temp.get(i));
            }
        }

        // return completed path
        return createPath(current);
    }

    public List<Point> createPath(AStarPoint current) {
        List<Point> path = new LinkedList<>();
        while (current.getPrev() != null) {
            path.add(0, current.getPos());
            current = current.getPrev();
        }
        return path;
    }
}

