import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.stream.Stream;


public abstract class Miners extends Animation_AB {

    protected int resourceLimit;
    protected int resourceCount;
    protected List<Point> path = new ArrayList<>();
    protected int numRows;
    protected int numCols;

    public Miners(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, imageIndex, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strategy = new AStarPathingStrategy();

        List<Point> points = strategy.computePath(position, destPos, canPassThrough(world), withinReach(), CARDINAL_NEIGHBORS);


        if (points.size() == 0) {
            return position;
        }
        return points.get(0);
    }



    protected static Predicate<Point> canPassThrough(WorldModel world){
        return p -> (!world.isOccupied(p) && world.withinBounds(p));
    }

    public boolean withinBounds(Point pos)
    {
        return pos.y >= 0 && pos.y < numRows &&
                pos.x >= 0 && pos.x < numCols;
    }


    protected static BiPredicate<Point, Point> withinReach(){
        return (Point p1, Point p2) -> p1.adjacent(p2);

    }

    public static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x, point.y - 1))
                            .add(new Point(point.x, point.y + 1))
                            .add(new Point(point.x - 1, point.y))
                            .add(new Point(point.x + 1, point.y))
                            .build();

    public static final Function<Point, Stream<Point>> DIAGONAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x-1, point.y - 1))
                            .add(new Point(point.x+1, point.y + 1))
                            .add(new Point(point.x - 1, point.y+1))
                            .add(new Point(point.x + 1, point.y-1))
                            .build();


    public Point nextPositionMiner(WorldModel world, Point destPos)
    {
       PathingStrategy strategy = new AStarPathingStrategy();

       List<Point> points = strategy.computePath(position, destPos, canPassThrough(world), withinReach(), CARDINAL_NEIGHBORS);


       if (points.size() == 0) {
          return position;
       }
       return points.get(0);
    }
}
