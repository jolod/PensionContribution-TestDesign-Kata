package sammancoaching;

public interface DatabaseAccessLayer {
    Employee getEmployeeById(int employeeId);

    double lookupValue(String namedConstant);

    int lookupInt(String namedConstant);
}
