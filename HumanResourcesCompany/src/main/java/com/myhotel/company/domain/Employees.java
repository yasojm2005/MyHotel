package com.myhotel.company.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myhotel.company.domain.proyections.EmployeeBySegmentDTO;
import com.myhotel.company.domain.proyections.EmployeeInDeparmentBySegment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Employees.
 */
@Entity
@NamedNativeQuery(
    name = "quantityOfEmploymentInDepartmentBySegment",
    query =
        "SELECT d.department_name as department,\n" +
            "  SUM(CASE WHEN e.SALARY < 3500 THEN 1 ELSE 0 END) AS segmento_a,\n" +
            "  SUM(CASE WHEN e.SALARY >= 3500 AND e.SALARY < 8000 THEN 1 ELSE 0 END) AS segmneto_b,\n" +
            "  SUM(CASE WHEN e.SALARY >= 8000 THEN 1 ELSE 0 END) AS segmento_c \n" +
            "FROM employees e\n" +
            "JOIN departments d ON e.departments_id = d.id\n" +
            "GROUP BY d.department_name;",
    resultSetMapping = "employeeInDepartment_by_Segment"
)
@SqlResultSetMapping(
    name = "employeeInDepartment_by_Segment",
    classes = @ConstructorResult(
        targetClass = EmployeeInDeparmentBySegment.class,
        columns = {
            @ColumnResult(name = "department", type = String.class),
            @ColumnResult(name = "segmento_a", type = Integer.class),
            @ColumnResult(name = "segmento_b", type = String.class),
            @ColumnResult(name = "segmento_c", type = Integer.class)
        }
    )
)

////////////////////////////////////////////////////////////////////////////////////////////////
@NamedNativeQuery(
    name = "quantityOfEmploymentBySegment",
    query =
        "SELECT \n" +
            "  CASE \n" +
            "    WHEN SALARY < 3500 THEN 'SEGMENTO A'\n" +
            "    WHEN SALARY >= 3500 AND SALARY < 8000 THEN 'SEGMENTO B'\n" +
            "    WHEN SALARY >= 8000 THEN 'SEGMENTO C'\n" +
            "  END AS segmento,\n" +
            "  COUNT(*) AS cantidad\n" +
            "FROM \n" +
            "  employees\n" +
            "GROUP BY \n" +
            "  CASE \n" +
            "    WHEN SALARY < 3500 THEN 'SEGMENTO A'\n" +
            "    WHEN SALARY >= 3500 AND SALARY < 8000 THEN 'SEGMENTO B'\n" +
            "    WHEN SALARY >= 8000 THEN 'SEGMENTO C'\n" +
            "  END;",
    resultSetMapping = "employee_by_Segment"
)
@SqlResultSetMapping(
    name = "employee_by_Segment",
    classes = @ConstructorResult(
        targetClass = EmployeeBySegmentDTO.class,
        columns = {
            @ColumnResult(name = "segmento", type = String.class),
            @ColumnResult(name = "cantidad", type = Integer.class)
        }
    )
)
@Table(name = "employees")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employees implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "salary", precision = 21, scale = 2)
    private BigDecimal salary;

    @Column(name = "commision_pct", precision = 21, scale = 2)
    private BigDecimal commisionPCT;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locations" }, allowSetters = true)
    private Departments departments;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employees id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employees firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employees lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Employees email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Employees phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getHireDate() {
        return this.hireDate;
    }

    public Employees hireDate(LocalDate hireDate) {
        this.setHireDate(hireDate);
        return this;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getSalary() {
        return this.salary;
    }

    public Employees salary(BigDecimal salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getCommisionPCT() {
        return this.commisionPCT;
    }

    public Employees commisionPCT(BigDecimal commisionPCT) {
        this.setCommisionPCT(commisionPCT);
        return this;
    }

    public void setCommisionPCT(BigDecimal commisionPCT) {
        this.commisionPCT = commisionPCT;
    }

    public Departments getDepartments() {
        return this.departments;
    }

    public void setDepartments(Departments departments) {
        this.departments = departments;
    }

    public Employees departments(Departments departments) {
        this.setDepartments(departments);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employees)) {
            return false;
        }
        return id != null && id.equals(((Employees) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employees{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", salary=" + getSalary() +
            ", commisionPCT=" + getCommisionPCT() +
            "}";
    }
}
