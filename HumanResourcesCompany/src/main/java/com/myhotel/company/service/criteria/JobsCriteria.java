package com.myhotel.company.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myhotel.company.domain.Jobs} entity. This class is used
 * in {@link com.myhotel.company.web.rest.JobsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter jobTitle;

    private BigDecimalFilter minSalary;

    private BigDecimalFilter maxSalary;

    private LongFilter jobHistoryId;

    private Boolean distinct;

    public JobsCriteria() {}

    public JobsCriteria(JobsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.jobTitle = other.jobTitle == null ? null : other.jobTitle.copy();
        this.minSalary = other.minSalary == null ? null : other.minSalary.copy();
        this.maxSalary = other.maxSalary == null ? null : other.maxSalary.copy();
        this.jobHistoryId = other.jobHistoryId == null ? null : other.jobHistoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public JobsCriteria copy() {
        return new JobsCriteria(this);
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

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            jobTitle = new StringFilter();
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimalFilter getMinSalary() {
        return minSalary;
    }

    public BigDecimalFilter minSalary() {
        if (minSalary == null) {
            minSalary = new BigDecimalFilter();
        }
        return minSalary;
    }

    public void setMinSalary(BigDecimalFilter minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimalFilter getMaxSalary() {
        return maxSalary;
    }

    public BigDecimalFilter maxSalary() {
        if (maxSalary == null) {
            maxSalary = new BigDecimalFilter();
        }
        return maxSalary;
    }

    public void setMaxSalary(BigDecimalFilter maxSalary) {
        this.maxSalary = maxSalary;
    }

    public LongFilter getJobHistoryId() {
        return jobHistoryId;
    }

    public LongFilter jobHistoryId() {
        if (jobHistoryId == null) {
            jobHistoryId = new LongFilter();
        }
        return jobHistoryId;
    }

    public void setJobHistoryId(LongFilter jobHistoryId) {
        this.jobHistoryId = jobHistoryId;
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
        final JobsCriteria that = (JobsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(minSalary, that.minSalary) &&
            Objects.equals(maxSalary, that.maxSalary) &&
            Objects.equals(jobHistoryId, that.jobHistoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobTitle, minSalary, maxSalary, jobHistoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (jobTitle != null ? "jobTitle=" + jobTitle + ", " : "") +
            (minSalary != null ? "minSalary=" + minSalary + ", " : "") +
            (maxSalary != null ? "maxSalary=" + maxSalary + ", " : "") +
            (jobHistoryId != null ? "jobHistoryId=" + jobHistoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
