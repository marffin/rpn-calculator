import java.util.function.Function;

public class Operator {
    private final Function<Decimal[], Decimal[]> func;
    private final Integer inputSize;
    private final String name;

    public Operator(String name, Function<Decimal[], Decimal[]> func, Integer inputSize) {
        this.name = name;
        this.func = func;
        this.inputSize = inputSize;
    }

    public Function<Decimal[], Decimal[]> getFunc() {
        return func;
    }

    public Integer getInputSize() {
        return inputSize;
    }

    public String getName() {
        return name;
    }
}
