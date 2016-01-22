import java.io.*;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 18/02/14
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class CorpusReader {
    public String Path;
    public String Marker;

    private BufferedReader _reader;
    private BufferedWriter _marker;
    private Integer _position;

    public CorpusReader(String path) throws IOException {
        Path = path;
        Marker = path + ".marker";
        _reader = new BufferedReader(new FileReader(Path));
        try {
            Scanner scanner = new Scanner(new BufferedReader(new FileReader(Marker)));
            _position = scanner.nextInt();
            for (Integer i = 0; i < _position; i++)
                _reader.readLine();
            scanner.close();
        } catch (FileNotFoundException e) {
            _position = 0;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String GetLine() throws IOException {
        _marker = new BufferedWriter(new FileWriter(Marker));
        _marker.write(_position.toString());
        _marker.flush();
        String line = null;
        try {
            line = _reader.readLine();
        } catch (IOException e) {
            return null;
        }
        _position++;
        return line;
    }
}
