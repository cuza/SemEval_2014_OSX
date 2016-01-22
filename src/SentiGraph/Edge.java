package SentiGraph;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 6/03/14
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class Edge {
    public Integer Dst;
    public Integer Src;
    public Integer Weight;

    public Edge(Integer src, Integer dst, Integer weight)
    {
        Src = src;
        Weight = weight;
        Dst = dst;
    }

    public int compareTo(Edge other)
    {
        return Src != other.Src
                ? Src.compareTo(other.Src)
                : Dst != other.Dst
                ? Dst.compareTo(other.Dst)
                : Weight.compareTo(other.Weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "Dst=" + Dst +
                ", Src=" + Src +
                ", Weight=" + Weight +
                '}';
    }
}
