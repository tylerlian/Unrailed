public class AStarPoint{

    private int h;
    private int g = 0;
    private int f;
    private Point pos;
    private AStarPoint prev;

    public AStarPoint(Point pos, Point end, AStarPoint prev) {
        if (prev != null) {
            this.prev = prev;
            this.g = prev.getG() + 1;
        }
        this.h = Math.abs(end.getX() - pos.getX()) + Math.abs(end.getY() - pos.getY());
        this.f = this.g + this.h;
        this.pos = pos;
    }

    public int getG() {
        return g;
    }

    public int getF() {
        return this.f;
    }

    public Point getPos() {
        return pos;
    }

    public AStarPoint getPrev() {
        return prev;
    }

    public String toString() {
        return ("F: " + f + " Pos: " + pos);
    }

}
