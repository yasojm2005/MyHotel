package com.myhotel.company.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myhotel.company.domain.Departments} entity. This class is used
 * in {@link com.myhotel.company.web.rest.DepartmentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /departments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartmentsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter departmentName;

    private StringFilter deparmentType;

    private LongFilter locationsId;

    private Boolean distinct;

    public DepartmentsCriteria() {}

    public DepartmentsCriteria(DepartmentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.departmentName = other.departmentName == null ? null : other.departmentName.copy();
        this.deparmentType = other.deparmentType == null ? null : other.deparmentType.copy();
        this.locationsId = other.locationsId == null ? null : other.locationsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepartmentsCriteria copy() {
        return new DepartmentsCriteria(this);
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

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public StringFilter departmentName() {
        if (departmentName == null) {
            departmentName = new StringFilter();
        }
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
    }

    public StringFilter getDeparmentType() {
        return deparmentType;
    }

    public StringFilter deparmentType() {
        if (deparmentType == null) {
            deparmentType = new StringFilter();
        }
        return deparmentType;
    }

    public void setDeparmentType(StringFilter deparmentType) {
        this.deparmentType = deparmentType;
    }

    public LongFilter getLocationsId() {
        return locationsId;
    }

    public LongFilter locationsId() {
        if (locationsId == null) {
            locationsId = new LongFilter();
        }
        return locationsId;
    }

    public void setLocationsId(LongFilter locationsId) {
        this.locationsId = locationsId;
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
        final DepartmentsCriteria that = (DepartmentsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(departmentName, that.departmentName) &&
            Objects.equals(deparmentType, that.deparmentType) &&
            Objects.equals(locationsId, that.locationsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentName, deparmentType, locationsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (departmentName != null ? "departmentName=" + departmentName + ", " : "") +
            (deparmentType != null ? "deparmentType=" + deparmentType + ", " : "") +
            (locationsId != null ? "locationsId=" + locationsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
