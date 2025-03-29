package sammancoaching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TenureBonusTest {
    final double MEDIUM_PERCENTAGE = 2.0;
    final double LONG_PERCENTAGE = 3.5;
    final int MEDIUM_YEARS = 3;
    final int LONG_YEARS = 7;
    final TenureBonus tenureBonuses = new TenureBonus(MEDIUM_YEARS, MEDIUM_PERCENTAGE, LONG_YEARS, LONG_PERCENTAGE);

    @Test
    public void shortYearExamples() {
        assertEquals(0.0, tenureBonuses.lookup(0));
        assertEquals(0.0, tenureBonuses.lookup(1));
    }

    @Test
    public void mediumYearExample() {
        assertEquals(MEDIUM_PERCENTAGE, tenureBonuses.lookup(4));
    }

    @Test
    public void longYearExample() {
        assertEquals(LONG_PERCENTAGE, tenureBonuses.lookup(100));
    }

    @Test
    public void yearThresholdsAreInclusive() {
        // This test expresses a particular property, and therefore uses variables on
        // both sides of the assert.
        assertEquals(MEDIUM_PERCENTAGE, tenureBonuses.lookup(MEDIUM_YEARS));
        assertEquals(LONG_PERCENTAGE, tenureBonuses.lookup(LONG_YEARS));
    }

    @Test
    public void negativeYearIsIllegal() {
        assertThrows(IllegalArgumentException.class, () -> tenureBonuses.lookup(-1));
    }
}
