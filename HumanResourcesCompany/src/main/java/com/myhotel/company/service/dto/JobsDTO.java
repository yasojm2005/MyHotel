package com.myhotel.company.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.myhotel.company.domain.Jobs} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobsDTO implements Serializable {

    private Long id;

    private String jobTitle;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    private Set<EmployeesDTO> jobHistories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Set<EmployeesDTO> getJobHistories() {
        return jobHistories;
    }

    public void setJobHistories(Set<EmployeesDTO> jobHistories) {
        this.jobHistories = jobHistories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobsDTO)) {
            return false;
        }

        JobsDTO jobsDTO = (JobsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobsDTO{" +
            "id=" + getId() +
            ", jobTitle='" + getJobTitle() + "'" +
            ", minSalary=" + getMinSalary() +
            ", maxSalary=" + getMaxSalary() +
            ", jobHistories=" + getJobHistories() +
            "}";
    }
}
