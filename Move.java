public interface Move extends AnimPeriod{


    public Point nextPos(Point point, WorldModel worldModel);

    public void move(WorldModel worldModel, Point pos);


}
