package sammancoaching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Assert that the formula produces the expected results for well-formed inputs.
 */
class SalaryPensionContributionFormulaTest {
        @Test
        public void mixedPercentages() {
                BigDecimal annualSalary = BigDecimal.valueOf(1000.0);
                double basePercentage = 5.0;
                double tenureBonus = 3.0;
                double seniorityBonus = 7.0;

                var actualContribution = PensionContributionCalculator.calculatePensionContribution(annualSalary,
                                basePercentage, tenureBonus, seniorityBonus);

                assertEquals(150.0, actualContribution.doubleValue(), 0.001);
        }
}

/**
 * Assert that the formula rejects ill-formed inputs.
 */
class SalaryPensionContributionFormulaDomainTest {
        @Test
        public void negativeAnnualSalaryIsIllegal() {
                BigDecimal annualSalary = BigDecimal.valueOf(-1);
                assertThrows(IllegalArgumentException.class, () -> PensionContributionCalculator
                                .calculatePensionContribution(annualSalary, 1.0, 1.0, 1.0));
        }

        @Test
        public void negativeBaseContributionIsIllegal() {
                int baseContributionPercentage = -1;
                assertThrows(IllegalArgumentException.class,
                                () -> PensionContributionCalculator.calculatePensionContribution(BigDecimal.valueOf(37),
                                                baseContributionPercentage, 1.0, 1.0));
        }

        @Test
        public void negativeTenureBonusIsIllegal() {
                double tenureBonus = -0.1;
                assertThrows(IllegalArgumentException.class,
                                () -> PensionContributionCalculator.calculatePensionContribution(BigDecimal.valueOf(37),
                                                1.0, tenureBonus, 1.0));
        }

        @Test
        public void negativeSeniorityBonusIsIllegal() {
                double seniorityBonus = -0.8;
                assertThrows(IllegalArgumentException.class,
                                () -> PensionContributionCalculator.calculatePensionContribution(BigDecimal.valueOf(37),
                                                1.0, 1.0, seniorityBonus));
        }
}
