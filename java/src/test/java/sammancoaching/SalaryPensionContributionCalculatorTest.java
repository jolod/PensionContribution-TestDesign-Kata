package sammancoaching;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/*
 * This is a property-based test more-so than an example-based test.
 * The purpose is to check that the calculator makes use of its configuration correctly,
 * not to check that the formula is correct. That is tested in `SalaryPensionContributionFormulaTest`.
 */
class SalaryPensionContributionCalculatorTest {
        /**
         * These static fields are used to configure the calculator and are completely
         * arbitrary by design. They are static constants because they are used both in
         * the test setup and the `tenureAndSeniority` test below.
         */
        public static final double BASE_PERCENTAGE = 5.0;

        public static final double MEDIUM_TENURE_PERCENTAGE = 2;
        public static final double LONG_TENURE_PERCENTAGE = 3.5;

        public static final double JUNIOR_SENIORITY_PERCENTAGE = 1.0;
        public static final double MID_SENIORITY_PERCENTAGE = 3.0;
        public static final double LEADERSHIP_TEAM_PERCENTAGE = 2.5;

        public static PensionContributionCalculator calculator() {
                /*
                 * These thresholds are arbitrary, but the parameters in
                 * `tenureAndSeniorityProvider` depend to the values set here.
                 */
                final int mediumTenureThreshold = 5;
                final int longTenureThreshold = 10;

                return new PensionContributionCalculator(
                                BASE_PERCENTAGE,
                                new TenureBonus(mediumTenureThreshold, MEDIUM_TENURE_PERCENTAGE,
                                                longTenureThreshold, LONG_TENURE_PERCENTAGE),
                                (Seniority seniority) -> switch (seniority) {
                                        case JUNIOR -> JUNIOR_SENIORITY_PERCENTAGE;
                                        case MIDLEVEL -> MID_SENIORITY_PERCENTAGE;
                                        case LEADERSHIPTEAM -> LEADERSHIP_TEAM_PERCENTAGE;
                                });
        }

        private static Stream<Arguments> tenureAndSeniorityProvider() {
                final double no_tenure_precentage = 0.0;
                final int shortYears = 2;
                final int mediumYears = 6;
                final int longYears = 11;

                return Stream.of(
                                Arguments.of(
                                                shortYears, no_tenure_precentage,
                                                Seniority.JUNIOR, JUNIOR_SENIORITY_PERCENTAGE),
                                Arguments.of(
                                                longYears, LONG_TENURE_PERCENTAGE,
                                                Seniority.MIDLEVEL, MID_SENIORITY_PERCENTAGE),
                                Arguments.of(
                                                mediumYears, MEDIUM_TENURE_PERCENTAGE,
                                                Seniority.LEADERSHIPTEAM, LEADERSHIP_TEAM_PERCENTAGE));
        }

        @ParameterizedTest
        @MethodSource("tenureAndSeniorityProvider")
        @DisplayName("The calculator should correctly take tenure and seniority levels into account")
        public void tenureAndSeniority(int tenure, double expectedTenureBonus, Seniority seniority,
                        double expectedSeniorityBonus) {
                BigDecimal annualSalary = BigDecimal.valueOf(38467.0);

                var actualContribution = calculator().calculatePensionContribution(annualSalary, tenure, seniority);
                var expectedContribution = PensionContributionCalculator.calculatePensionContribution(annualSalary,
                                BASE_PERCENTAGE, expectedTenureBonus, expectedSeniorityBonus);

                assertEquals(expectedContribution.doubleValue(), actualContribution.doubleValue(), 0.001);
        }
}
