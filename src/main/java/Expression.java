import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Expression {
    private final HashMap<String, Operator<String, Integer>> OPERATOR_CONF = new HashMap<String, Operator<String, Integer>>() {{
        put("+", new Operator<>("add", 2));
        put("-", new Operator<>("substract", 2));
        put("*", new Operator<>("multiply", 2));
        put("/", new Operator<>("divide", 2));
        put("sqrt", new Operator<>("sqrt", 1));
        put("clear", new Operator<>("clear", -1));  // -1 = all
    }};

    private final LinkedList<Decimal> operands = new LinkedList<>();
    private final LinkedList<Operation> operations = new LinkedList<>();

    private String currentToken;
    private int currentPosition;

    public void feed(String token, int position) throws UnsupportedOperationException {
        currentToken = token;
        currentPosition = position;
        try {
            Decimal d = Decimal.parse(token);
            operands.add(d);
            operations.add(new Operation(new Decimal[]{}, new Decimal[]{d}, null));
        } catch (NumberFormatException ex) {
            try {
                Operator<String, Integer> entry = OPERATOR_CONF.get(token);
                if (entry == null) {
                    // undo and redo or other operator that concerns with operations rather than operands
                    switch (token) {
                        case "undo":
                            undo();
                            break;
                        default:
                            throw new RuntimeException(this.log("Unsupported operator: %s"));
                    }
                } else
                    calculate(entry);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(this.log(e.getMessage()));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException(this.log("Implementation missing for operator: " + token));
            }
        }
    }

    private void calculate(Operator<String, Integer> op) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (this.operands.size() < op.getValue())
            throw new RuntimeException(this.log("insufficient parameters"));
        int inputSize = op.getValue();
        if (inputSize == -1)
            inputSize = this.operands.size();
        Decimal[] input = this.operands.subList(this.operands.size() - inputSize, this.operands.size()).toArray(new Decimal[0]).clone();
        Method opMethod = this.getClass().getDeclaredMethod(op.getKey(), Decimal[].class);
        opMethod.setAccessible(true);
        Decimal[] output = (Decimal[]) opMethod.invoke(this, new Object[]{input});
        for (int i = 0; i < input.length; i++)
            this.operands.removeLast();
        Collections.addAll(this.operands, output);
        Operation operation = new Operation(input, output, op.getKey());
        this.operations.add(operation);
    }

    private void undo() {
        if (this.operations.size() == 0)
            return;
        Operation operation = this.operations.removeLast();
        for (int i = 0; i < operation.getOutput().length; i++)
            this.operands.removeLast();
        Collections.addAll(this.operands, operation.getInput());
    }

    private Decimal[] clear(Decimal[] decimals) {
        return new Decimal[0];
    }

    private Decimal[] add(Decimal[] decimals) {
        Decimal d1 = decimals[0];
        Decimal d2 = decimals[1];
        double val = d1.getVal() + d2.getVal();
        if (d1.getIsInteger() && d2.getIsInteger())
            return new Decimal[]{new Decimal((int)val)};
        return new Decimal[]{new Decimal(val)};
    }

    private Decimal[] substract(Decimal[] decimals) {
        Decimal d1 = decimals[0];
        Decimal d2 = decimals[1];
        double val = d1.getVal() - d2.getVal();
        if (d2.getIsInteger() && d1.getIsInteger())
            return new Decimal[]{new Decimal((int)val)};
        return new Decimal[]{new Decimal(val)};
    }

    private Decimal[] multiply(Decimal[] decimals) {
        Decimal d1 = decimals[0];
        Decimal d2 = decimals[1];
        double val = d1.getVal() * d2.getVal();
        if (d1.getIsInteger() && d2.getIsInteger())
            return new Decimal[]{new Decimal((int)val)};
        return new Decimal[]{new Decimal(val)};
    }

    private Decimal[] divide(Decimal[] decimals) {
        Decimal d1 = decimals[0];
        Decimal d2 = decimals[1];
        if (d2.getVal() == 0)
            throw new ArithmeticException("divide by zero");
        if (d2.getIsInteger() && d1.getIsInteger()) {
            int v1 = (int)d2.getVal();
            int v2 = (int)d1.getVal();
            int m = v2 % v1;
            if (m == 0)
                return new Decimal[]{new Decimal(v2 / v1)};
        }
        return new Decimal[]{new Decimal(d1.getVal() / d2.getVal())};
    }

    private Decimal[] sqrt(Decimal[] decimals) {
        Decimal d1 = decimals[0];
        if (d1.getVal() < 0)
            throw new ArithmeticException("square root of negative value: " + d1.toString());
        return new Decimal[]{new Decimal(Math.sqrt(d1.getVal()))};
    }

    private String log(String msg) {
        return String.format("operator %s (position %d): %s", currentToken, currentPosition, msg);
    }

    public String formatStack() {
        StringJoiner sj = new StringJoiner(" ");
        for (Decimal d : this.operands) {
            sj.add(d.toString());
        }
        return sj.toString();
    }
}
