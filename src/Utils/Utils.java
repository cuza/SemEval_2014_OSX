package Utils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by dave on 22/1/16.
 */
public class Utils {

    public static void WriteText(String name, String buffer) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(name));
            writer.write(buffer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Nullable
    public static ArrayList<String> ReadLines(String name) {
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
}

