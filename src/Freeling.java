import edu.upc.freeling.*;

/**
 * Created with IntelliJ IDEA.
 * User: Andy
 * Date: 24/01/14
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class Freeling {
    private static final String DATA = "./data/";
    private static final String LANG = "en";
    private static MacoOptions op;
    private static LangIdent lgid;
    private static Tokenizer tk;
    private static Splitter sp;
    private static Maco mf;
    private static HmmTagger tg;
    private static ChartParser parser;
    private static DepTxala dep;
    private static Nec neclass;
    private static Senses sen;
    private static Ukb dis;


    public static ListSentence ParseLine(String line) {
        if (op == null) {
            System.loadLibrary("freeling_javaAPI");

            Util.initLocale("default");

            // Create options set for maco analyzer.
            // Default values are Ok, except for data files.
            op = new MacoOptions(LANG);

            op.setActiveModules(false, true, true, true,
                    true, true, true,
                    true, true, true);

            op.setDataFiles(
                    "",
                    DATA + LANG + "/locucions.dat",
                    DATA + LANG + "/quantities.dat",
                    DATA + LANG + "/afixos.dat",
                    DATA + LANG + "/probabilitats.dat",
                    DATA + LANG + "/dicc.src",
                    DATA + LANG + "/np.dat",
                    DATA + "common/punct.dat");

            // Create analyzers.
            lgid = new LangIdent(DATA + "/common/lang_ident/ident-few.dat");

            tk = new Tokenizer(DATA + LANG + "/tokenizer.dat");
            sp = new Splitter(DATA + LANG + "/splitter.dat");
            mf = new Maco(op);

            tg = new HmmTagger(DATA + LANG + "/tagger.dat", true, 2);
            parser = new ChartParser(
                    DATA + LANG + "/chunker/grammar-chunk.dat");
            dep = new DepTxala(DATA + LANG + "/dep/dependences.dat",
                    parser.getStartSymbol());
            neclass = new Nec(DATA + LANG + "/nerc/nec/nec-ab-poor1.dat");

            sen = new Senses(DATA + LANG + "/senses.dat"); // sense dictionary
            dis = new Ukb(DATA + LANG + "/ukb.dat"); // sense disambiguator
        }
        // Extract the tokens from the line of text.
        ListWord l = tk.tokenize(line);

        // Split the tokens into distinct sentences.
        ListSentence ls = sp.split(l, false);

        // Perform morphological analysis
        mf.analyze(ls);

        // Perform part-of-speech tagging.
        tg.analyze(ls);

        // Perform named entity (NE) classificiation.
        neclass.analyze(ls);

        sen.analyze(ls);
        dis.analyze(ls);

        // Chunk parser
        parser.analyze(ls);

        // Dependency parser
        dep.analyze(ls);

        return ls;
    }

    public static void printDepTree(int depth, TreeDepnode tr) {
        TreeDepnode child = null;
        TreeDepnode fchild = null;
        Depnode childnode;
        long nch;
        int last, min;
        Boolean trob;

        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }

        System.out.print(
                tr.getInfo().getLinkRef().getInfo().getLabel() + "/" +
                        tr.getInfo().getLabel() + "/");

        Word w = tr.getInfo().getWord();

        System.out.print(
                "(" + w.getForm() + " " + w.getLemma() + " " + w.getTag());
        printSenses(w);
        System.out.print(")");

        nch = tr.numChildren();

        if (nch > 0) {
            System.out.println(" [");

            for (int i = 0; i < nch; i++) {
                child = tr.nthChildRef(i);

                if (child != null) {
                    if (!child.getInfo().isChunk()) {
                        printDepTree(depth + 1, child);
                    }
                } else {
                    System.err.println("ERROR: Unexpected NULL child.");
                }
            }

            // Print chunks (in order)
            last = 0;
            trob = true;

            // While an unprinted chunk is found, look for the one with lower
            // chunk_ord value.
            while (trob) {
                trob = false;
                min = 9999;

                for (int i = 0; i < nch; i++) {
                    child = tr.nthChildRef(i);
                    childnode = child.getInfo();

                    if (childnode.isChunk()) {
                        if ((childnode.getChunkOrd() > last) &&
                                (childnode.getChunkOrd() < min)) {
                            min = childnode.getChunkOrd();
                            fchild = child;
                            trob = true;
                        }
                    }
                }
                if (trob && (child != null)) {
                    printDepTree(depth + 1, fchild);
                }

                last = min;
            }

            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }

            System.out.print("]");
        }

        System.out.println("");
    }

    private static void printSenses(Word w) {
        String ss = w.getSensesString();

        // The senses for a FreeLing word are a list of
        // pair<string,double> (sense and page rank). From java, we
        // have to get them as a string with format
        // sense:rank/sense:rank/sense:rank
        // which will have to be splitted to obtain the info.
        //
        // Here, we just output it:
        System.out.print(" " + ss);
    }

}
