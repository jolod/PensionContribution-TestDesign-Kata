package sammancoaching;

public class TenureBonus {
    public final int mediumTenureYears;
    public final double mediumTenurePercentage;
    public final int longTenureYears;
    public final double longTenurePercentage;

    public TenureBonus(int mediumTenureYears, double mediumTenurePercentage, int longTenureYears,
            double longTenurePercentage) {
        this.mediumTenureYears = mediumTenureYears;
        this.mediumTenurePercentage = mediumTenurePercentage;
        this.longTenureYears = longTenureYears;
        this.longTenurePercentage = longTenurePercentage;
    }

    public double lookup(int tenure) {
        if (tenure >= longTenureYears) {
            return longTenurePercentage;
        } else if (tenure >= mediumTenureYears) {
            return mediumTenurePercentage;
        } else if (tenure >= 0) {
            return 0.0;
        } else {
            throw new IllegalArgumentException("Tenure years must be non-negative");
        }
    }
}
