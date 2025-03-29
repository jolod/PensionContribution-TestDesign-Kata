package sammancoaching;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class PensionContributionCalculator {
    private final double baseContributionPercentage;
    private final TenureBonus tenureBonus;
    private final Function<Seniority, Double> seniorityBonus;

    public PensionContributionCalculator(double baseContributionPercentage, TenureBonus tenureBonus,
            Function<Seniority, Double> seniorityBonus) {
        this.baseContributionPercentage = baseContributionPercentage;
        this.tenureBonus = tenureBonus;
        this.seniorityBonus = seniorityBonus;
    }

    public static PensionContributionCalculator fromConfig(DatabaseAccessLayer databaseAccessLayer) {
        double baseContributionPercentage = databaseAccessLayer.lookupValue("BASE_CONTRIBUTION_RATE");

        int mediumTenureYears = databaseAccessLayer.lookupInt("MEDIUM_TENURE_YEARS");
        int longTenureYears = databaseAccessLayer.lookupInt("LONG_TENURE_YEARS");
        double mediumTenurePercentage = databaseAccessLayer.lookupValue("MEDIUM_TENURE_BONUS_PERCENTAGE");
        double longTenurePercentage = databaseAccessLayer.lookupValue("LONG_TENURE_BONUS_PERCENTAGE");
        TenureBonus tenureBonus = new TenureBonus(mediumTenureYears, mediumTenurePercentage, longTenureYears,
                longTenurePercentage);

        final double juniorBonus = databaseAccessLayer.lookupValue("JUNIOR_SENIORITY_BONUS_PERCENTAGE");
        final double midLevelBonus = databaseAccessLayer.lookupValue("MIDLEVEL_SENIORITY_BONUS_PERCENTAGE");
        final double leadershipTeamBonus = databaseAccessLayer.lookupValue("LEADERSHIPTEAM_SENIORITY_BONUS_PERCENTAGE");
        Function<Seniority, Double> seniorityBonuses = (Seniority seniority) -> switch (seniority) {
            case JUNIOR -> juniorBonus;
            case MIDLEVEL -> midLevelBonus;
            case LEADERSHIPTEAM -> leadershipTeamBonus;
        };

        return new PensionContributionCalculator(baseContributionPercentage, tenureBonus, seniorityBonuses);
    }

    public BigDecimal calculatePensionContribution(DatabaseAccessLayer databaseAccessLayer, int employeeId) {
        Employee employee = databaseAccessLayer.getEmployeeById(employeeId);

        return calculatePensionContribution(employee.getAnnualSalary(), employee.getTenure(), employee.getSeniority());
    }

    public BigDecimal calculatePensionContribution(BigDecimal annualSalary, int tenureYears, Seniority seniority) {
        double tenureBonus = this.tenureBonus.lookup(tenureYears);
        double seniorityBonus = this.seniorityBonus.apply(seniority);

        return calculatePensionContribution(annualSalary, baseContributionPercentage, tenureBonus, seniorityBonus);
    }

    static BigDecimal calculatePensionContribution(BigDecimal annualSalary, double baseContributionPercentage,
            double tenureBonus, double seniorityBonus) {
        if (annualSalary.compareTo(BigDecimal.ZERO) < 0 || baseContributionPercentage < 0 || tenureBonus < 0
                || seniorityBonus < 0) {
            throw new IllegalArgumentException("Values must be non-negative");
        }

        double totalContributionPercentage = baseContributionPercentage + tenureBonus + seniorityBonus;

        return annualSalary
                .multiply(BigDecimal.valueOf(totalContributionPercentage))
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP);
    }
}
