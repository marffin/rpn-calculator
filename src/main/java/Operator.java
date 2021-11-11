import java.util.Map;

public class Operator<String, Integer> implements Map.Entry<String, Integer> {
    private final String name;
    private Integer inputSize;

    public Operator(String name, Integer inputSize) {
        this.name = name;
        this.inputSize = inputSize;
    }

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public Integer getValue() {
        return inputSize;
    }

    @Override
    public Integer setValue(Integer value) {
        inputSize = value;
        return inputSize;
    }
}
