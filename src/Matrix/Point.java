package Matrix;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 6/03/14
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class Point
{
    public Integer Col;
    public  Integer Row;

    public Point(int row, int col)
    {
        Row = row;
        Col = col;
    }

    public int compareTo(Point other)
    {
        return (Row.compareTo(other.Row) != 0) ? (Row.compareTo(other.Row)) : (Col.compareTo(other.Col));
    }
}