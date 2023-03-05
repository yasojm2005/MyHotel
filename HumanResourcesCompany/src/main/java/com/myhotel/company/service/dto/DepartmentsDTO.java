package com.myhotel.company.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myhotel.company.domain.Departments} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartmentsDTO implements Serializable {

    private Long id;

    private String departmentName;

    private String deparmentType;

    private LocationsDTO locations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDeparmentType() {
        return deparmentType;
    }

    public void setDeparmentType(String deparmentType) {
        this.deparmentType = deparmentType;
    }

    public LocationsDTO getLocations() {
        return locations;
    }

    public void setLocations(LocationsDTO locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentsDTO)) {
            return false;
        }

        DepartmentsDTO departmentsDTO = (DepartmentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departmentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentsDTO{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", deparmentType='" + getDeparmentType() + "'" +
            ", locations=" + getLocations() +
            "}";
    }
}
