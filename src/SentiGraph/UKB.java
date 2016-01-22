package SentiGraph;

import Matrix.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 6/03/14
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class UKB {
    private static Integer Stop = 30;

    private static double[] Product(double[] v, double x) {
        double[] res = new double[v.length];
        for (Integer j = 0; j < v.length; j++)
            res[j] = (v[j] * x);
        return res;
    }

    private static double[] Sum(double[] v1, double[] v2) {
        double[] res = new double[v1.length];
        for (Integer j = 0; j < v1.length; j++)
            res[j] = (v1[j] + v2[j]);
        return res;
    }

    private static double[] MatrixProduct(Matrix matrix, double[] v) {
        double[] res = new double[v.length];
        for (Point d : matrix.Keys())
            res[d.Row] += matrix.get(d.Row, d.Col) * v[d.Col];
        return res;
    }

    public static double[] PageRank(Graph graph, SortedSet<String> words, Boolean exclude) {
        double[] pr = new double[graph._lemmas.size()];
        double[] vector = new double[graph._lemmas.size()];
        Matrix cm = new Matrix(graph._lemmas.size(), 0);
        ArrayList<Integer> kernelIndex = new ArrayList<Integer>();
        for (Integer i = 0; i < graph._lemmas.size(); i++) {
            if ((!exclude && words.contains(graph.__lemmas.get(i))) || (exclude && !words.contains(graph.__lemmas.get(i)))) {
                vector[i] = 1.0;
                kernelIndex.add(i);
            } else
                vector[i] = (1.0 / (graph._lemmas.size()));

            pr[i] = (1.0 / (graph._lemmas.size()));
            for (Map.Entry<Integer, Edge> p : graph.AdjList.get(i).entrySet())
                cm.set(i, p.getValue().Dst, (graph.AdjList.get(p.getValue().Dst).size() > 0
                        ? (0.85 *
                        (1 - (1.0 / graph.AdjList.get(p.getValue().Dst).size())))
                        : 0.0));
        }
        for (Integer j = 0; j < Stop; j++)
            pr = Sum(MatrixProduct(cm, pr), Product(vector, 1.0 - 0.85));
        return pr;
    }
}
