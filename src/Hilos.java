import java.applet.Applet;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 26/03/14
 * Time: 22:49
 * To change this template use File | Settings | File Templates.
 */
public class Hilos extends Applet {
    protected Thread[] threads = null; //null cuando no se ejecuta

    public void init() {
    }

    protected Thread crearHilo(final String frase, final ArrayList<Preprocessor> result) {
        return new Thread(new Runnable() {
            public void run() {
                Preprocessor p = new Preprocessor(frase, false);
                result.add(p);
                int pp= result.size();
            }
        });
    }

    public synchronized void start(String frase, ArrayList<Preprocessor> result) {
        int n = 8;
        if (threads == null) {
            threads = new Thread[n];
        }
        int index=-1;
        while((index=iddle())==-1){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        threads[index]=crearHilo(frase,result);
        threads[index].start();
    }


    private int iddle() {
        for(int i=0;i<threads.length;i++)
            if(threads[i]==null ||!threads[i].isAlive())
                return i;
        return -1;
    }

    public synchronized void stop() {
        if (threads != null){  // se salta si ya se ha detenido
            for (int i = 0; i < threads.length; ++i)
                threads[i].interrupt();
            threads = null;
        }
    }
}