import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Andy
 * Date: 25/01/14
 * Time: 18:33
 * To change this template use File | Settings | File Templates.
 */
public class Preprocessor {

    private String _tweet;

    private ArrayList<String> loadDict(String source) {
        ArrayList<String> dictsList = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(source));
            while (reader.ready())
                dictsList.add(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return dictsList;
    }

    private HashMap<String, Double> loadMapDict(String source) {
        HashMap<String, Double> dictsMap = new HashMap<String, Double>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(source));
            while (reader.ready()) {
                String[] line = reader.readLine().split("\t");
                if (!dictsMap.containsKey(line[0])) {
                    dictsMap.put(line[0], Double.parseDouble(line[1]));
                } else {
                    dictsMap.put(line[0], (dictsMap.get(line[0]) + Double.parseDouble(line[1])) / 2.0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return dictsMap;
    }

    private static String _vulgarSlang = "dicts/vulgar slang.dic";

    private ArrayList<String> getVulgarSlang() {
        return loadDict(_vulgarSlang);
    }

    private static String _positiveEmoticDic = "dicts/positive emoticons.dic";

    private ArrayList<String> getPositiveEmoticDic() {
        return loadDict(_positiveEmoticDic);
    }

    private static String _negativeEmoticDic = "dicts/negative emoticons.dic";

    private ArrayList<String> getNegativeEmoticDic() {
        return loadDict(_negativeEmoticDic);
    }

    private static String _ngramasDic = "dicts/ngrams.dic";

    private HashMap<String, Double> getNgramsDic() {
        return loadMapDict(_ngramasDic);
    }

    private static String _pairsDic = "dicts/pairs.dic";

    private HashMap<String, Double> getPairsDic() {
        return loadMapDict(_pairsDic);
    }

    private static String _hashtagDic = "dicts/hashtags.dic";

    private ArrayList<String> getHashtagDic() {
        return loadDict(_hashtagDic);
    }

    public String tweet;
    public String result;
    public Integer positiveEmoticons = 0;
    public Integer negativeEmoticons = 0;
    public Double ngrams = 0.0;
    public Double pairs = 0.0;
    public Integer positiveHashtags = 0;
    public Integer negativeHashtags = 0;

    Preprocessor(String tweet, boolean b) {
        this._tweet = this.tweet = tweet + " ";
        this.HtmlParser();
        if (b) {
            this.CountHashTags();
            this.CountNgrams();
            this.CountPairs();
        }
        this.CountPositivesEmoticons();
        this.CountNegativesEmoticons();
        this.UriParser();
        this.Tweetifier();
        this.Process();
    }

    private void CountPairs() {
        for (String line : getPairsDic().keySet()) {
            String[] _pairs = line.split("---");
            if (_tweet.contains(_pairs[0]) && _tweet.contains(_pairs[1]))
                ngrams += getPairsDic().get(line);
        }
    }

    private void CountNgrams() {
        for (String line : getNgramsDic().keySet())
            ngrams += (_tweet.split(Pattern.quote(line)).length - 1) * getNgramsDic().get(line);
    }

    private void CountHashTags() {
        String[] line;
        for (String _line : getHashtagDic()) {
            line = _line.split("\t");
            if (line[1].equals("positive"))
                positiveHashtags += _tweet.split(Pattern.quote(line[0])).length - 1;
            else
                negativeHashtags += _tweet.split(Pattern.quote(line[0])).length - 1;
        }
    }

    private void UriParser() {
        String emailPattern = "(mailto:)?([-_\\.\\w])+@[\\.\\w]+\\.[\\w]{2,4}";
        String urlPattern = "(https|http|ftp)://[^\\s]+";
        _tweet = _tweet.replaceAll(urlPattern, " ");
        //Todo: Buscar Regex para el correo
    }

    private void HtmlParser() {
//        _tweet = Html.fromHtml();
    }

    private void Process() {
        Stack<Integer> s = new Stack<Integer>();
        List<Integer> remove = new ArrayList<Integer>();
        boolean[] trouble = new boolean[256 * 256];
        trouble['$'] = true;
        trouble['%'] = true;
        trouble['#'] = true;
        trouble['@'] = true;
        trouble['*'] = true;
        trouble['&'] = true;
        trouble['('] = true;
        trouble[')'] = true;
        trouble['"'] = true;

        result = "";
        _tweet = _tweet.replace('.', ';');
        if (_tweet.charAt(_tweet.length() - 1) != '.') {
            _tweet += ".";
        }

        Character[][] check = new Character[3][2];
        check[0][0] = '(';
        check[0][1] = ')';
        check[1][0] = '{';
        check[1][1] = '}';
        check[2][0] = '[';
        check[2][1] = ']';
        for (Integer index = 0; index < 3; index++) {
            for (Integer i = 0; i < _tweet.length(); i++) {
                if (_tweet.charAt(i) == check[index][0]) {
                    s.push(i);
                } else if (_tweet.charAt(i) == check[index][1]) {
                    if (s.empty()) {
                        remove.add(i);
                    } else {
                        s.pop();
                    }
                }
            }
            while (!s.empty()) {
                remove.add(s.pop());
            }
        }
        for (Integer i = 0; i < _tweet.length(); i++) {
            if (!(remove.contains(i) || trouble[_tweet.charAt(i)])) {
                result += _tweet.charAt(i);
            }
        }
    }

    private void Tweetifier() {
        Character[] tags = new Character[2];
        tags[0] = '@';
        tags[1] = '#';
        for (Integer i = 0; i < 2; i++) {
            Pattern p = Pattern.compile("(^|[^\\w])" + tags[i] + "([^ ]+)");
            Matcher m = p.matcher(_tweet);
            while (m.find()) { // Find each match in turn; String can't do this.
                String name = m.group(2); // Access a submatch group; String can't do this.
                String aux = "";
                aux += name.charAt(0);
                aux = aux.toUpperCase() + name.substring(1);
                _tweet = _tweet.replace(tags[i] + name, aux);
            }
        }

        String[] line;
        for (String _line : getVulgarSlang()) {
            line = _line.split("\t");
            try {
                _tweet = _tweet.replaceAll("[^\\w](?i:" + line[0] + ")[^\\w]", " " + line[1] + " ");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void CountNegativesEmoticons() {
        String[] line;

        for (String _line : getNegativeEmoticDic()) {
            line = _line.split("\t");
            negativeEmoticons += _tweet.split(Pattern.quote(line[0])).length - 1;
            _tweet = _tweet.replaceAll(Pattern.quote(line[0]), line[1]);
        }
    }

    private void CountPositivesEmoticons() {
        String[] line;

        for (String _line : getPositiveEmoticDic()) {
            line = _line.split("\t");
            positiveEmoticons += _tweet.split(Pattern.quote(line[0])).length - 1;
            _tweet = _tweet.replaceAll(Pattern.quote(line[0]), line[1]);
        }
    }
}

