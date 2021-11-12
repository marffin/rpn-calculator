import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Expression {
    private final Map<String, Operator> operators = new HashMap<>();

    private final LinkedList<Decimal> operands = new LinkedList<>();
    private final LinkedList<Operation> operations = new LinkedList<>();

    private String currentToken;
    private int currentPosition;

    public Expression() {
        operators.put("+", new Operator("add", this::add, 2));
        operators.put("-", new Operator("subtract", this::subtract, 2));
        operators.put("*", new Operator("multiply", this::multiply, 2));
        operators.put("/", new Operator("divide", this::divide, 2));
        operators.put("sqrt", new Operator("sqrt", this::sqrt, 1));
        operators.put("clear", new Operator("clear", this::clear, -1));
    }

    public void feed(String token, int position) throws UnsupportedOperationException {
        currentToken = token;
        currentPosition = position;
        try {
            Decimal d = Decimal.parse(token);
            operands.add(d);
            operations.add(new Operation(new Decimal[]{}, new Decimal[]{d}, null));
        } catch (NumberFormatException ex) {
            try {
                if (operators.containsKey(token)) {
                    Operator op = operators.get(token);
                    calculate(op);
                } else {
                    // undo and redo or other operator that concerns with operations rather than operands
                    switch (token) {
                        case "undo":
                            undo();
                            break;
                        default:
                            throw new UnsupportedOperationException(this.log("unsupported operator"));
                    }
                }
            } catch (InvocationTargetException e) {
                throw new RuntimeException(this.log(e.getMessage()));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException(this.log("Implementation missing for operator: " + token));
            }
        }
    }

    private void calculate(Operator op) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        int inputSize = op.getInputSize();
        if (this.operands.size() < inputSize)
            throw new RuntimeException(this.log("insufficient parameters"));
        if (inputSize == -1)
            inputSize = this.operands.size();
        Decimal[] input = this.operands.subList(this.operands.size() - inputSize, this.operands.size()).toArray(new Decimal[0]).clone();
        Decimal[] output = op.getFunc().apply(input);
        for (int i = 0; i < input.length; i++)
            this.operands.removeLast();
        Collections.addAll(this.operands, output);
        Operation operation = new Operation(input, output, op.getName());
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

    private Decimal[] subtract(Decimal[] decimals) {
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
            throw new ArithmeticException(this.log("divide by zero"));
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
            throw new ArithmeticException(this.log("square root of negative value: " + d1.toString()));
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
