import processing.core.PImage;

import java.util.*;


public class Train extends TraversingEntity {

    private List<Point> closed;

    public Train(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, "train", actionPeriod, animationPeriod);
        this.closed = new LinkedList<>();
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Point> crabTarget = findTrackAround(world);
        Point finishLine = findFinishLine(world);

        if(finishLine != this.getPosition()){
            this.moveTo(world,finishLine);
            System.out.println("You win!");
        }

        long nextPeriod = this.getActionPeriod();

        for (Point rail : crabTarget) {
            if (!closed.contains(rail)) {
                this.moveTo(world, rail);
            }
        }

        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), nextPeriod);

    }

    public Point findFinishLine(WorldModel world) {
        List<Entity> crabTarget = world.findNearestTrack(
                this.getPosition(), "rail2", closed);
        for (Entity rail : crabTarget) {
            if(this.getPosition().adjacent(rail.getPosition())){
                return rail.getPosition();
            }
        }
        return this.getPosition();
    }

    public List<Point> findTrackAround(WorldModel world) {
        List<Entity> crabTarget = world.findNearestTrack(
                this.getPosition(), "rail1", closed);
        List<Point> temp = new LinkedList<>();
        for (Entity rail : crabTarget) {
            if (this.getPosition().adjacent(rail.getPosition())) {
                temp.add(rail.getPosition());
            }
        }
        return temp;
    }

    private void moveTo(WorldModel world, Point railPos) {
        world.moveTrainEntity(this, railPos);
        closed.add(0, railPos);
    }
}
