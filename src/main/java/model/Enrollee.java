package model;

import com.opencsv.bean.CsvBindByName;

public class Enrollee {
    @CsvBindByName(column = "User Id")
    private String userId;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "Version")
    private Integer version;

    @CsvBindByName(column = "Insurance Company")
    private String insuranceCompany;

    public String getUserId() {
        return this.userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Integer getVersion() {
        return this.version;
    }

    public String getInsuranceCompany() {
        return this.insuranceCompany;
    }
}
