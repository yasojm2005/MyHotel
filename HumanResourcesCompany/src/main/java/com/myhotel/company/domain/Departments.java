package com.myhotel.company.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Departments.
 */
@Entity
@Table(name = "departments")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Departments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "deparment_type")
    private String deparmentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "countries" }, allowSetters = true)
    private Locations locations;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public Departments departmentName(String departmentName) {
        this.setDepartmentName(departmentName);
        return this;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDeparmentType() {
        return this.deparmentType;
    }

    public Departments deparmentType(String deparmentType) {
        this.setDeparmentType(deparmentType);
        return this;
    }

    public void setDeparmentType(String deparmentType) {
        this.deparmentType = deparmentType;
    }

    public Locations getLocations() {
        return this.locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public Departments locations(Locations locations) {
        this.setLocations(locations);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departments)) {
            return false;
        }
        return id != null && id.equals(((Departments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departments{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", deparmentType='" + getDeparmentType() + "'" +
            "}";
    }
}
