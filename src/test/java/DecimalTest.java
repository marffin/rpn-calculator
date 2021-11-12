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
}