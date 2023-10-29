package io.giovannymassuia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void sum() {
        assertEquals(2, Calculator.sum(1, 1));
    }

}