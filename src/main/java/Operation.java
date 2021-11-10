public class Operation {
    private final String op;
    private final Decimal[] input;
    private final Decimal[] output;

    public Operation(Decimal[] input, Decimal[] output, String op) {
        this.input = input;
        this.output = output;
        this.op = op;
    }

    public Decimal[] getInput() {
        return input;
    }

    public Decimal[] getOutput() {
        return output;
    }

    public String getOperator() {
        return this.op;
    }
}
