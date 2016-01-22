package Mesure;

/**
 * Created with IntelliJ IDEA.
 * User: dave
 * Date: 6/03/14
 * Time: 12:36
 * To change this template use File | Settings | File Templates.
 */
public class Mesure {
    public String Lemma;
    public Double _positive;
    public Double _negative;
    public Double _neutral;

    public Mesure() {
        _positive = _negative = _neutral = 0.0;
    }

    public Mesure(String line) {
        String[] buffer = line.split(",");
        try {
            Lemma = buffer[0].substring(14, buffer[0].length() - 1);
            setPositive(Double.parseDouble(buffer[1].split("=")[1]));
            setNegative(Double.parseDouble(buffer[2].split("=")[1]));
            setNeutral(Double.parseDouble(buffer[3].split("=")[1].substring(0, buffer[3].split("=")[1].length() - 1)));

        } catch (Exception e){
              e.printStackTrace();
        }
        }

    public double getPositive() {
        return (Double.max(0, _positive - _negative) / _maxp);
    }

    public void setPositive(Double value) {
        _positive = value;
    }

    public double getNegative() {
        return (Double.max(0, _negative - _positive) / _maxn);
    }

    public void setNegative(Double value) {
        _negative = value;
    }

    public double getObjectiveCalc() {
        return 1.0 - Math.abs(getPositive() - getNegative());
    }

    public double getNeutral() {
        return (_neutral / _maxu);
    }

    public void setNeutral(Double value) {
        _neutral = value;
    }

    @Override
    public String toString() {
        return "Mesure{" +
                "Lemma='" + Lemma.replace(',',' ') + '\'' +
                ", _positive=" + _positive +
                ", _negative=" + _negative +
                ", _neutral=" + _neutral +
                '}';
    }

    public static double _maxn = 1;
    public static double _maxp = 1;
    public static double _maxu = 1;
}
