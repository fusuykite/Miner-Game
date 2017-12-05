import java.util.*;
import java.util.function.BiPredicate;
import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;

class AStarPathingStrategy implements PathingStrategy {
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        //Initialize Lists and variables
        Map<Point, Node> closed = new HashMap<>();
        Map<Point, Node> open = new HashMap<>();
        int destination = 1;
        int g = 0;
        int h = this.h_dist(start, end);
        int f = this.t_dist(g, h);
        Node now = new Node(start, g, h, f, null);
        Comparator<Node> sort1 = Comparator.comparing(Node::getF);
        PriorityQueue<Node> f_rank = new PriorityQueue<>(sort1);
        f_rank.add(now);
        while (destination >= 1) {
            List<Point> adjacent = potentialNeighbors.apply(now.getloc())
                    .filter(canPassThrough)
                    .filter(pt -> !pt.equals(end))
                    .collect(Collectors.toList());


            //Iterates through the neighbors list for a set of possible points
            for (Point adj: adjacent) {
                if (open.containsKey(adj)) {
                    int g1;
                    g1 = now.getG() + 1;
                    if (g1 < open.get(adj).getG()) { //UPDATE VALUES
                        h = h_dist(adj, end);
                        Node update = open.get(adj);
                        update.setH(h);
                        update.setF(this.t_dist(g1, h));
                        update.setG(g1);
                        update.setPrior_sq(now);
                        destination += 1;
                    }
                }
                //If neighbor not in open list create new node
                else {
                    g = now.getG() + 1;
                    h = h_dist(adj, end);
                    Node n = new Node(adj, g, h, t_dist(g, h), now);
                    f_rank.add(n);
                    open.put(adj, n);
                    destination += 1;
                }
            }
            Point location = now.getloc();
        closed.put(location, now);
        open.remove(location);
        now = f_rank.poll();
        location = now.getloc();
            {if (withinReach.test(location, end) == false){
            destination += 1;
        }
        else{
            closed.put(location, now);
            destination = 0;}
        }}
        List<Point> final_path = new ArrayList<>();
        final_path = PathCreator(now, final_path);
        return final_path;

    }

    public int h_dist(Point now, Point end) {
        return(now.distanceSquared(end)); }

    public int t_dist(int d_start, int h_dist) {
        return(d_start+h_dist); }

    protected List<Point> PathCreator (Node n1, List<Point> final_path)
        {
            Point lo = n1.getloc();
            Node Sq_P = n1.getPrior_sq();
            if (Sq_P != null) {
                final_path.add(0, lo); }
            else
                return final_path;
            return PathCreator(Sq_P, final_path);
        }



}