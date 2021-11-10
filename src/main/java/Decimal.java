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

    public static Decimal add(Decimal d1, Decimal d2) {
        double val = d1.getVal() + d2.getVal();
        if (d1.getIsInteger() && d2.getIsInteger())
            return new Decimal((int)val);
        return new Decimal(val);
    }

    public static Decimal substract(Decimal d2, Decimal d1) {
        double val = d2.getVal() - d1.getVal();
        if (d1.getIsInteger() && d2.getIsInteger())
            return new Decimal((int)val);
        return new Decimal(val);
    }

    public static Decimal multiply(Decimal d1, Decimal d2) {
        double val = d1.getVal() * d2.getVal();
        if (d1.getIsInteger() && d2.getIsInteger())
            return new Decimal((int)val);
        return new Decimal(val);
    }

    public static Decimal divide(Decimal d2, Decimal d1) {
        if (d1.getVal() == 0)
            throw new ArithmeticException("divide by zero");
        if (d1.isInteger && d2.isInteger) {
            int v1 = (int)d1.getVal();
            int v2 = (int)d2.getVal();
            int m = v2 % v1;
            if (m == 0)
                return new Decimal(v2 / v1);
        }
        return new Decimal(d2.getVal() / d1.getVal());
    }

    public static Decimal sqrt(Decimal d1) {
        if (d1.getVal() < 0)
            throw new ArithmeticException("square root of negative value: " + d1.toString());
        return new Decimal(Math.sqrt(d1.getVal()));
    }
}
