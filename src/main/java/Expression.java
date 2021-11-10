import java.util.Collections;
import java.util.LinkedList;
import java.util.StringJoiner;

public class Expression {
    private static final String UNDO = "undo";
    private static final String CLEAR = "clear";
    private static final String SQRT = "sqrt";
    private static final String DIVIDE = "/";
    private static final String MULTIPLY = "*";
    private static final String SUBSTRACT = "-";
    private static final String ADD = "+";

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
            switch (token) {
                case ADD:
                    this.add();
                    break;
                case SUBSTRACT:
                    this.substract();
                    break;
                case MULTIPLY:
                    this.multiply();
                    break;
                case DIVIDE:
                    this.divide();
                    break;
                case SQRT:
                    this.sqrt();
                    break;
                case CLEAR:
                    this.clear();
                    break;
                case UNDO:
                    this.undo();
                    break;
                default:
                    throw new UnsupportedOperationException(this.log("unsupported operator: " + token));
            }
        }
    }

    private void undo() {
        if (this.operations.size() == 0)
            return;
        Operation operation = this.operations.removeLast();
        for (int i = 0; i < operation.getOutput().length; i++)
            this.operands.removeLast();
        Collections.addAll(this.operands, operation.getInput());
    }

    private void clear() {
        Operation operation = new Operation(this.operands.toArray(new Decimal[0]), new Decimal[]{}, CLEAR);
        this.operations.add(operation);
        this.operands.clear();
    }

    private void sqrt() {
        if (this.operands.size() < 1)
            throw new RuntimeException(this.log("insufficient parameters"));
        Decimal d1 = this.operands.removeLast();
        Decimal d2;
        try {
            d2 = Decimal.sqrt(d1);
        } catch (ArithmeticException ex) {
            this.operands.add(d1);
            throw new RuntimeException(this.log(ex.getMessage()));
        }
        Operation operation = new Operation(new Decimal[]{d1}, new Decimal[]{d2}, SQRT);
        this.operands.add(d2);
        this.operations.add(operation);
    }

    private void divide() {
        if (this.operands.size() < 2)
            throw new RuntimeException(this.log("insufficient parameters"));
        Decimal d1 = this.operands.removeLast();
        Decimal d2 = this.operands.removeLast();
        Decimal d3;
        try {
            d3 = Decimal.divide(d2, d1);
        } catch (ArithmeticException ex) {
            this.operands.add(d2);
            this.operands.add(d1);
            throw new RuntimeException(this.log(ex.getMessage()));
        }
        Operation operation = new Operation(new Decimal[]{d2, d1}, new Decimal[]{d3}, DIVIDE);
        this.operands.add(d3);
        this.operations.add(operation);
    }

    private void multiply() {
        if (this.operands.size() < 2)
            throw new RuntimeException(this.log("insufficient parameters"));
        Decimal d1 = this.operands.removeLast();
        Decimal d2 = this.operands.removeLast();
        Decimal d3 = Decimal.multiply(d1, d2);
        Operation operation = new Operation(new Decimal[]{d2, d1}, new Decimal[]{d3}, MULTIPLY);
        this.operands.add(d3);
        this.operations.add(operation);
    }

    private void substract() {
        if (this.operands.size() < 2)
            throw new RuntimeException(this.log("insufficient parameters"));
        Decimal d1 = this.operands.removeLast();
        Decimal d2 = this.operands.removeLast();
        Decimal d3 = Decimal.substract(d2, d1);
        Operation operation = new Operation(new Decimal[]{d2, d1}, new Decimal[]{d3}, SUBSTRACT);
        this.operands.add(d3);
        this.operations.add(operation);
    }

    private void add() {
        if (this.operands.size() < 2)
            throw new RuntimeException(this.log("insufficient parameters"));
        Decimal d1 = this.operands.removeLast();
        Decimal d2 = this.operands.removeLast();
        Decimal d3 = Decimal.add(d1, d2);
        Operation operation = new Operation(new Decimal[]{d2, d1}, new Decimal[]{d3}, ADD);
        this.operands.add(d3);
        this.operations.add(operation);
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
