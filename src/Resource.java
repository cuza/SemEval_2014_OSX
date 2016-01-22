import SentiGraph.Graph;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 23/02/14
 * Time: 11:59
 * To change this template use File | Settings | File Templates.
 */
public class Resource {
    public String Path;
    public HashMap<String,Integer> Frecuency;
    public HashMap<String,ArrayList<String>> Link;

    private BufferedWriter _writerFrec;
    private BufferedWriter _writerLink;

    public Resource(String path) throws IOException {
        Path = path;
        Frecuency = new HashMap<String, Integer>();
        Link = new HashMap<String, ArrayList<String>>();
        _writerFrec = new BufferedWriter(new FileWriter(Path+".frec"));
        _writerLink = new BufferedWriter(new FileWriter(Path+".link"));
    }

    public Integer Increment(String Lemma){
        if(Frecuency.containsKey(Lemma))
            return Frecuency.put(Lemma,Frecuency.get(Lemma)+1);
        Link.put(Lemma,new ArrayList<String>());
        return Frecuency.put(Lemma,1);
    }

    public boolean AddLink(String Lemma, String other){
        return Link.get(Lemma).add(other);
    }

    public void Save() throws IOException {
        for (String key:Frecuency.keySet())
            _writerFrec.write(String.format("%1$s:%2$s\r\n",key,Frecuency.get(key)));
        _writerFrec.close();
        for (String key:Link.keySet()){
            _writerLink.write(String.format("%1$s:",key));
            for (String val:Link.get(key))
                _writerLink.write(String.format("%1$s;",val));
            _writerLink.write("\r\n");
        }
        _writerLink.close();
    }

    public Graph getGraph() {
        Graph graph = new Graph(0);
        for (String s:Frecuency.keySet())
            graph.AddNode(s);
        for (String s:Frecuency.keySet())
            for (String ss:Link.get(s))
                graph.AddEdge(s,ss,1);
        return graph;
    }
}
