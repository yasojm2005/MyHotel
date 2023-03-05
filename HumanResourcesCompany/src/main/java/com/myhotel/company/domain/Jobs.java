package com.myhotel.company.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Jobs.
 */
@Entity
@Table(name = "jobs")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Jobs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "min_salary", precision = 21, scale = 2)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 21, scale = 2)
    private BigDecimal maxSalary;

    @ManyToMany
    @JoinTable(
        name = "rel_jobs__employee",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @JsonIgnoreProperties(value = { "departments" }, allowSetters = true)
    private Set<Employees> jobHistories = new HashSet<>();


    public Long getId() {
        return this.id;
    }

    public Jobs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public Jobs jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getMinSalary() {
        return this.minSalary;
    }

    public Jobs minSalary(BigDecimal minSalary) {
        this.setMinSalary(minSalary);
        return this;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return this.maxSalary;
    }

    public Jobs maxSalary(BigDecimal maxSalary) {
        this.setMaxSalary(maxSalary);
        return this;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Set<Employees> getJobHistories() {
        return this.jobHistories;
    }

    public void setJobHistories(Set<Employees> employees) {
        this.jobHistories = employees;
    }

    public Jobs jobHistories(Set<Employees> employees) {
        this.setJobHistories(employees);
        return this;
    }

    public Jobs addJobHistory(Employees employees) {
        this.jobHistories.add(employees);
        return this;
    }

    public Jobs removeJobHistory(Employees employees) {
        this.jobHistories.remove(employees);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Jobs)) {
            return false;
        }
        return id != null && id.equals(((Jobs) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Jobs{" +
            "id=" + getId() +
            ", jobTitle='" + getJobTitle() + "'" +
            ", minSalary=" + getMinSalary() +
            ", maxSalary=" + getMaxSalary() +
            "}";
    }
}
