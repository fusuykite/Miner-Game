final class Viewport
{
   protected int row;
   protected int col;
   protected int numRows;
   protected int numCols;

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }

   public void shift(int col, int row)
   {
      this.col = col;
      this.row = row;
   }

   public boolean contains(Point p)
   {
      return p.y >= row && p.y < row + numRows &&
              p.x >= col && p.x < col + numCols;
   }

   public Point viewportToWorld(int column, int rows)
   {
      return new Point(column + col, rows + row);
   }

   public Point worldToViewport(int column, int rows)
   {
      return new Point(column - col, rows - row);
   }


}
