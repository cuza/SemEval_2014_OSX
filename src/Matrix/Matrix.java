package Matrix;

import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 6/03/14
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class Matrix {
    private HashMap<Integer, SortedSet<Integer>> _list;
    private HashMap<Point, Double> _matrix;
    private Integer _columns;
    private Integer _rows;

    public Matrix(Integer rows, Integer columns) {
        _matrix = new HashMap<Point, Double>();
        //_list = new Dictionary<int, SortedSet<int>>();
        _rows = rows;
        _columns = columns;
    }

    public Set<Point> Keys()
    {
        return _matrix.keySet();
    }

    public Integer length()
    {
        return _rows;
    }

    public Double get(Integer row, Integer col) {
        if (_matrix != null && _rows > row && _columns > col && row >= 0 && col >= 0)
            return _matrix.containsKey(new Point(row, col)) ? _matrix.get(new Point(row, col)) : Double.MIN_VALUE;
        return Double.MIN_VALUE;
    }

    public void set(Integer row, Integer col, Double value) {
        _rows = Integer.max(_rows, row + 1);
        _columns = Integer.max(_columns, col + 1);
        if (_list != null) {
            if (!_list.containsKey(row))
                _list.put(row, new TreeSet<Integer>());
            _list.get(row).add(col);
        }
        if (_matrix != null && value != Double.MIN_VALUE) {
            _matrix.put(new Point(row, col), value);
        }
    }

    public SortedSet<Integer> get(int row) {
        if (_list != null && _rows > row && row >= 0 && _list.containsKey(row))
            return _list.get(row);
        return new TreeSet<Integer>();
    }
}