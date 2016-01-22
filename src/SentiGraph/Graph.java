package SentiGraph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 6/03/14
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class Graph {
    private Integer Inf = Integer.MAX_VALUE;
    public ArrayList<HashMap<Integer,Edge>> AdjList;
    public HashMap<String, Integer> _lemmas;
    public ArrayList<String> __lemmas;

    public Graph(Integer size)
    {
        _lemmas = new HashMap<String, Integer>();
        __lemmas = new ArrayList<String>();
        AdjList = new ArrayList<HashMap<Integer, Edge>>();
        for (Integer i = 0; i < size; i++)
            AdjList.add(new HashMap<Integer, Edge>());
    }

    public void AddEdge(String src, String dst, int weight)
    {
        if(AdjList.get(_lemmas.get(src)).containsKey(_lemmas.get(dst)))
            AdjList.get(_lemmas.get(src)).get(_lemmas.get(dst)).Weight+=weight;
        else
            AdjList.get(_lemmas.get(src)).put(_lemmas.get(dst), new Edge(_lemmas.get(src),_lemmas.get(dst),weight));
    }

    public void AddNode(String lemma)
    {
        if (_lemmas.containsKey(lemma)) return;
        _lemmas.put(lemma, AdjList.size());
        __lemmas.add(lemma);
        AdjList.add(new HashMap<Integer, Edge>());
    }
}
