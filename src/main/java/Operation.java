public class Operation {
    private final String op;
    private final Decimal[] tops;
    private final int offset;

    public Operation(int offset, Decimal[] tops, String op) {
        this.offset = offset;
        this.tops = tops;
        this.op = op;
    }

    public String getOperator() {
        return this.op;
    }

    public int getOffset() {
        return offset;
    }

    public Decimal[] getTops() {
        return tops;
    }
}
