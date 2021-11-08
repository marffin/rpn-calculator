import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecimalTest {

    @Test
    void getVal() {
        Decimal d1 = new Decimal(1);
        assertEquals(1, d1.getVal());

        Decimal d2 = new Decimal(0.5);
        assertEquals(0.5, d2.getVal());
    }

    @Test
    void getIsInteger() {
        Decimal d1 = new Decimal(1);
        assertTrue(d1.getIsInteger());

        Decimal d2 = new Decimal(0.5);
        assertFalse(d2.getIsInteger());
    }

    @Test
    void testToString() {
        Decimal d1 = new Decimal(1);
        assertEquals("1", d1.toString());

        Decimal d2 = new Decimal(10.53);
        assertEquals("10.53", d2.toString());

        Decimal d3 = new Decimal(1.4142135623730951);
        assertEquals("1.4142135623", d3.toString());

        Decimal d4 = new Decimal(-1);
        assertEquals("-1", d4.toString());
    }

    @Test
    void parse() {
        Decimal d1 = Decimal.parse("1");
        assertEquals(1, d1.getVal());
        assertTrue(d1.getIsInteger());

        Decimal d2 = Decimal.parse("10.53");
        assertEquals(10.53, d2.getVal());
        assertFalse(d2.getIsInteger());

        assertThrows(NumberFormatException.class, () -> Decimal.parse("abc"));
    }

    @Test
    void add() {
        Decimal d1 = Decimal.add(new Decimal(1), new Decimal(2));
        assertEquals(3, d1.getVal());
        assertTrue(d1.getIsInteger());

        Decimal d2 = Decimal.add(new Decimal(1), new Decimal(2.1));
        assertEquals(3.1, d2.getVal());
        assertFalse(d2.getIsInteger());
    }

    @Test
    void substract() {
        Decimal d1 = Decimal.substract(new Decimal(1), new Decimal(2));
        assertEquals(-1, d1.getVal());
        assertTrue(d1.getIsInteger());

        Decimal d2 = Decimal.substract(new Decimal(1), new Decimal(2.1));
        assertEquals(-1.1, d2.getVal());
        assertFalse(d2.getIsInteger());
    }

    @Test
    void multiply() {
        Decimal d1 = Decimal.multiply(new Decimal(2), new Decimal(3));
        assertEquals(6, d1.getVal());
        assertTrue(d1.getIsInteger());

        Decimal d2 = Decimal.multiply(new Decimal(3), new Decimal(2.1));
        assertEquals(6.3, d2.getVal(), 0.0000001);
        assertFalse(d2.getIsInteger());
    }

    @Test
    void divide() {
        Decimal d1 = Decimal.divide(new Decimal(3), new Decimal(2));
        assertEquals(1.5, d1.getVal());
        assertFalse(d1.getIsInteger());

        Decimal d2 = Decimal.divide(new Decimal(4), new Decimal(2));
        assertEquals(2, d2.getVal());
        assertTrue(d2.getIsInteger());

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> Decimal.divide(new Decimal(3), new Decimal(0)));
        assertEquals("divide by zero", ex.getMessage());
    }

    @Test
    void sqrt() {
        Decimal d1 = Decimal.sqrt(new Decimal(9));
        assertEquals(3.0, d1.getVal());
        assertFalse(d1.getIsInteger());

        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> Decimal.sqrt(new Decimal(-3)));
        assertEquals("square root of negative value: -3", ex.getMessage());
    }
}