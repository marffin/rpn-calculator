import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Decimal {
    private final double val;
    private final boolean isInteger;

    public Decimal(int i) {
        this.val = i;
        this.isInteger = true;
    }

    public Decimal(double d) {
        this.val = d;
        this.isInteger = false;
    }

    public double getVal() {
        return this.val;
    }

    public boolean getIsInteger() {
        return this.isInteger;
    }

    public String toString() {
        if (this.isInteger)
            return String.valueOf((int)this.val);
        DecimalFormat df = new DecimalFormat("#.##########");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(this.val);
    }

    public static Decimal parse(String token) throws NumberFormatException {
        try {
            int val =  Integer.parseInt(token);
            return new Decimal(val);
        } catch (NumberFormatException ex1) {
            double val = Double.parseDouble(token);
            return new Decimal(val);
        }
    }
}
