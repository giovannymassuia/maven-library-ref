package io.giovannymassuia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void sum() {
        assertEquals(2, Calculator.sum(1, 1));
    }

    @Test
    void sub() {
        assertEquals(0, Calculator.sub(1, 1));
    }

    @Test
    void mult() {
        assertEquals(1, Calculator.mult(1, 1));
    }
}