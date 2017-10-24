final class Point
{
   public final int x;
   public final int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

    public static int distanceSquared(Point p1, Point p2)
    {
       int deltaX = p1.x - p2.x;
       int deltaY = p1.y - p2.y;

       return deltaX * deltaX + deltaY * deltaY;
    }

    public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

    public boolean adjacent(Point p2)
    {
       return (x == p2.x && Math.abs(y - p2.y) == 1) ||
          (y == p2.y && Math.abs(x - p2.x) == 1);
    }

    public boolean withinBounds(WorldModel worldModel)
    {
        return y >= 0 && y < worldModel.getrows() &&
                x >= 0 && x < worldModel.getCols();
    }

    public void setOccupancyCell(EntityInterface entity, WorldModel worldModel)
    {
        worldModel.occupancy[y][x] = entity;
    }

    public void setBackgroundCell(Background background, WorldModel worldModel)
    {
        worldModel.background[y][x] = background;
    }

    public EntityInterface getOccupancyCell(WorldModel worldModel)
    {
        return worldModel.occupancy[y][x];
    }

    public Background getBackgroundCell(WorldModel worldModel)
    {
        return worldModel.background[y][x];
    }

    public boolean contains(Viewport viewport)
    {
        return y >= viewport.row && y < viewport.row + viewport.numRows &&
                x >= viewport.col && x < viewport.col + viewport.numCols;
    }


}
