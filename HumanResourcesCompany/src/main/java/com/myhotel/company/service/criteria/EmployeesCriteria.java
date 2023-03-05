package com.myhotel.company.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myhotel.company.domain.Employees} entity. This class is used
 * in {@link com.myhotel.company.web.rest.EmployeesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phoneNumber;

    private LocalDateFilter hireDate;

    private BigDecimalFilter salary;

    private BigDecimalFilter commisionPCT;

    private LongFilter departmentsId;

    private Boolean distinct;

    public EmployeesCriteria() {}

    public EmployeesCriteria(EmployeesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.hireDate = other.hireDate == null ? null : other.hireDate.copy();
        this.salary = other.salary == null ? null : other.salary.copy();
        this.commisionPCT = other.commisionPCT == null ? null : other.commisionPCT.copy();
        this.departmentsId = other.departmentsId == null ? null : other.departmentsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeesCriteria copy() {
        return new EmployeesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateFilter getHireDate() {
        return hireDate;
    }

    public LocalDateFilter hireDate() {
        if (hireDate == null) {
            hireDate = new LocalDateFilter();
        }
        return hireDate;
    }

    public void setHireDate(LocalDateFilter hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimalFilter getSalary() {
        return salary;
    }

    public BigDecimalFilter salary() {
        if (salary == null) {
            salary = new BigDecimalFilter();
        }
        return salary;
    }

    public void setSalary(BigDecimalFilter salary) {
        this.salary = salary;
    }

    public BigDecimalFilter getCommisionPCT() {
        return commisionPCT;
    }

    public BigDecimalFilter commisionPCT() {
        if (commisionPCT == null) {
            commisionPCT = new BigDecimalFilter();
        }
        return commisionPCT;
    }

    public void setCommisionPCT(BigDecimalFilter commisionPCT) {
        this.commisionPCT = commisionPCT;
    }

    public LongFilter getDepartmentsId() {
        return departmentsId;
    }

    public LongFilter departmentsId() {
        if (departmentsId == null) {
            departmentsId = new LongFilter();
        }
        return departmentsId;
    }

    public void setDepartmentsId(LongFilter departmentsId) {
        this.departmentsId = departmentsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeesCriteria that = (EmployeesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(hireDate, that.hireDate) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(commisionPCT, that.commisionPCT) &&
            Objects.equals(departmentsId, that.departmentsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, hireDate, salary, commisionPCT, departmentsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (hireDate != null ? "hireDate=" + hireDate + ", " : "") +
            (salary != null ? "salary=" + salary + ", " : "") +
            (commisionPCT != null ? "commisionPCT=" + commisionPCT + ", " : "") +
            (departmentsId != null ? "departmentsId=" + departmentsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
