package Mesure;

import SentiGraph.*;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 6/03/14
 * Time: 12:53
 * To change this template use File | Settings | File Templates.
 */
public class SentiMesure {
    public Graph Positive;
    public Graph Negative;
    public Graph Neutral;

    public HashMap<String, Mesure> Mesures;

    public SentiMesure(Graph Positive, Graph Negative, Graph Neutral, String name) {
        Mesures = new HashMap<String, Mesure>();
        this.Positive = Positive;
        this.Negative = Negative;
        this.Neutral = Neutral;
        LoadMesures();
        ToFile(name);
    }

    public SentiMesure(String name) {
        Mesures = new HashMap<String, Mesure>();
        FromFile(name);
    }

    private void FromFile(String name) {
        for (String line : ReadLines(name)) {
            Mesure mesure = new Mesure(line);
            Mesures.put(mesure.Lemma, mesure);
        }
    }

    private ArrayList<String> ReadLines(String name) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(name));
            ArrayList<String> lines = new ArrayList<String>();
            while (reader.ready())
                lines.add(reader.readLine());
            return lines;
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private void WriteText(String name, String buffer) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(name));
            writer.write(buffer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void ToFile(String name) {
        String buffer = "";
        for (Map.Entry<String, Mesure> mesure : Mesures.entrySet())
            buffer += (mesure.getValue()) + "\r\n";
        WriteText(name, buffer);
    }

    private void LoadMesures() {
        TreeSet<String> pwords = new TreeSet<String>();
        TreeSet<String> nwords = new TreeSet<String>();
        TreeSet<String> allwords = new TreeSet<String>();

        for (String s : ReadLines("dicts/pos words.dic")) {
            pwords.add(s);
            allwords.add(s);
        }
        for (String s : ReadLines("dicts/neg words.dic")) {
            nwords.add(s);
            allwords.add(s);
        }

        double[] values = UKB.PageRank(Positive, pwords, false);
        double maxP = Double.MIN_VALUE;
        for (Map.Entry<String, Integer> lemma : Positive._lemmas.entrySet()) {
            if (!Mesures.containsKey(lemma.getKey())) {
                Mesure tmp = new Mesure();
                tmp.Lemma = lemma.getKey();
                tmp.setPositive(values[Positive._lemmas.get(lemma.getKey())]);
                Mesures.put(lemma.getKey(), tmp);
            } else Mesures.get(lemma.getKey())._positive += values[lemma.getValue()];
            maxP = Double.max(values[Positive._lemmas.get(lemma.getKey())], maxP);
        }

        values = UKB.PageRank(Negative, nwords, false);
        double maxN = Double.MIN_VALUE;
        for (Map.Entry<String, Integer> lemma : Negative._lemmas.entrySet()) {
            if (!Mesures.containsKey(lemma.getKey())) {
                Mesure tmp = new Mesure();
                tmp.Lemma = lemma.getKey();
                tmp.setNegative(values[Negative._lemmas.get(lemma.getKey())]);
                Mesures.put(lemma.getKey(), tmp);
            } else{
                try{
                    Mesures.get(lemma.getKey())._negative += values[lemma.getValue()];
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            maxN = Double.max(values[Negative._lemmas.get(lemma.getKey())], maxN);
        }

        values = UKB.PageRank(Neutral, allwords, true);
        double maxU = Double.MIN_VALUE;
        for (Map.Entry<String, Integer> lemma : Neutral._lemmas.entrySet()) {
            if (!Mesures.containsKey(lemma.getKey())) {
                Mesure tmp = new Mesure();
                tmp.Lemma = lemma.getKey();
                tmp.setNeutral(values[Neutral._lemmas.get(lemma.getKey())]);
                Mesures.put(lemma.getKey(), tmp);
            } else Mesures.get(lemma.getKey())._neutral += values[lemma.getValue()];
            maxU = Double.max(values[Neutral._lemmas.get(lemma.getKey())], maxU);
        }

        Double maxP2 = Double.MIN_VALUE, maxN2 = Double.MIN_VALUE;
        for (Mesure item : Mesures.values()) {
            item._positive /= maxP;
            item._negative /= maxN;
            maxP2 = Math.max(item.getPositive(), maxP2);
            maxN2 = Math.max(item.getNegative(), maxN2);
        }

        Mesure._maxp = maxP2;
        Mesure._maxn = maxN2;
        Mesure._maxu = maxU;
    }
}