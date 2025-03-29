package sammancoaching;

import java.math.BigDecimal;

enum Seniority {
    LEADERSHIPTEAM,
    MIDLEVEL,
    JUNIOR;
}

class Employee {
    private final BigDecimal annualSalary;
    private final int tenure;
    private final Seniority seniority;

    public Employee(BigDecimal annualSalary, int tenure, Seniority seniority) {
        this.annualSalary = annualSalary;
        this.tenure = tenure;
        this.seniority = seniority;
    }

    public BigDecimal getAnnualSalary() {
        return annualSalary;
    }

    public int getTenure() {
        return tenure;
    }

    public Seniority getSeniority() {
        return seniority;
    }
}
