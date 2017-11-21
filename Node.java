import java.util.Comparator;
import java.util.*;


//Node Class referenced by the AStar
//Loc = Point
//g = distance from start
//h = distance to end
//f = distance total
//prior_sq = previous node

public class Node
{
    protected Point loc;
    protected int g;
    protected int h;
    protected int f;
    protected Node prior_sq;

    public Node(Point loc, int g, int h, int f, Node prior_sq)
    {
        this.loc = loc;
        this.g = g;
        this.h = h;
        this.f = f;
        this.prior_sq = prior_sq;
    }

    public Point getloc() {
        return(loc); }

    public void setLoc(Point loc) {
        this.loc = loc; }


    public int getG() {
        return(g); }

    public void setG(int g) {
        this.g = g; }


    public int getH(){
        return(h); }

    public void setH(int h) {
        this.h = h; }


    public int getF() {
        return(f); }

    public void setF(int f) {
        this.f = f; }


    public Node getPrior_sq()
    {
        return(prior_sq);
    }

    public void setPrior_sq(Node prior_sq)
    {
        this.prior_sq = prior_sq;
    }

    protected int h_dist(Point now, Point end) {
        return(now.distanceSquared(end)); }

    protected int t_dist(int d_start, int h_dist)
    {
        return(d_start+h_dist);
    }
}
