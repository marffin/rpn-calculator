import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {

    @Test
    void feed() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        assertEquals("5 3", expr.formatStack());
        expr.feed("undo", 5);
        assertEquals("5", expr.formatStack());
    }

    @Test
    void feedAdd() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        expr.feed("9", 5);
        expr.feed("+", 7);
        assertEquals("5 12", expr.formatStack());
        expr.feed("+", 9);
        assertEquals("17", expr.formatStack());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> expr.feed("+", 11));
        assertEquals("operator + (position 11): insufficient parameters", ex.getMessage());
    }

    @Test
    void feedSubstract() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        expr.feed("9", 5);
        expr.feed("-", 7);
        assertEquals("5 -6", expr.formatStack());
        expr.feed("-", 9);
        assertEquals("11", expr.formatStack());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> expr.feed("-", 11));
        assertEquals("operator - (position 11): insufficient parameters", ex.getMessage());
    }

    @Test
    void feedMultiply() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        expr.feed("9", 5);
        expr.feed("*", 7);
        assertEquals("5 27", expr.formatStack());
        expr.feed("*", 9);
        assertEquals("135", expr.formatStack());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> expr.feed("*", 11));
        assertEquals("operator * (position 11): insufficient parameters", ex.getMessage());
    }

    @Test
    void feedDivide() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("9", 3);
        expr.feed("3", 5);
        assertEquals("5 9 3", expr.formatStack());
        expr.feed("/", 7);
        assertEquals("5 3", expr.formatStack());
        expr.feed("/", 9);
        assertEquals("1.6666666666", expr.formatStack());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> expr.feed("/", 11));
        assertEquals("operator / (position 11): insufficient parameters", ex.getMessage());
        expr.feed("0", 1);
        RuntimeException ex1 = assertThrows(RuntimeException.class, () -> expr.feed("/", 3));
        assertEquals("operator / (position 3): divide by zero", ex1.getMessage());
        assertEquals("1.6666666666 0", expr.formatStack());
    }

    @Test
    void feedSqrt() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        expr.feed("9", 5);
        expr.feed("sqrt", 7);
        assertEquals("5 3 3.0", expr.formatStack());
        Expression expr1 = new Expression();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> expr1.feed("sqrt", 1));
        assertEquals("operator sqrt (position 1): insufficient parameters", ex.getMessage());
        expr1.feed("-1", 1);
        assertEquals("-1", expr1.formatStack());
        RuntimeException ex1 = assertThrows(RuntimeException.class, () -> expr1.feed("sqrt", 3));
        assertEquals("operator sqrt (position 3): square root of negative value: -1", ex1.getMessage());
        assertEquals("-1", expr1.formatStack());
    }

    @Test
    void feedClear() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        expr.feed("9", 5);
        expr.feed("clear", 7);
        assertEquals("", expr.formatStack());
    }

    @Test
    void feedUndo() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        expr.feed("undo", 5);
        assertEquals("5", expr.formatStack());
        expr.feed("3", 10);
        expr.feed("9", 12);
        expr.feed("sqrt", 14);
        assertEquals("5 3 3.0", expr.formatStack());
        expr.feed("undo", 19);
        assertEquals("5 3 9", expr.formatStack());
    }

    @Test
    void feedMoreUndos() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        expr.feed("9", 5);
        expr.feed("sqrt", 7);
        assertEquals("5 3 3.0", expr.formatStack());
        expr.feed("undo", 12);
        assertEquals("5 3 9", expr.formatStack());
        expr.feed("undo", 17);
        assertEquals("5 3", expr.formatStack());
    }

    @Test
    void feedUnsupported() {
        Expression expr = new Expression();
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, () -> expr.feed("abc", 1));
        assertEquals("operator abc (position 1): unsupported operator: abc", ex.getMessage());
    }

    @Test
    void formatStack() {
        Expression expr = new Expression();
        expr.feed("5", 1);
        expr.feed("3", 3);
        expr.feed("9", 5);
        assertEquals("5 3 9", expr.formatStack());
    }
}