package com.myhotel.truck.service.criteria;

import com.myhotel.truck.domain.enumeration.TruckType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myhotel.truck.domain.Truck} entity. This class is used
 * in {@link com.myhotel.truck.web.rest.TruckResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trucks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TruckCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TruckType
     */
    public static class TruckTypeFilter extends Filter<TruckType> {

        public TruckTypeFilter() {}

        public TruckTypeFilter(TruckTypeFilter filter) {
            super(filter);
        }

        @Override
        public TruckTypeFilter copy() {
            return new TruckTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TruckTypeFilter type;

    private Boolean distinct;

    public TruckCriteria() {}

    public TruckCriteria(TruckCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TruckCriteria copy() {
        return new TruckCriteria(this);
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

    public TruckTypeFilter getType() {
        return type;
    }

    public TruckTypeFilter type() {
        if (type == null) {
            type = new TruckTypeFilter();
        }
        return type;
    }

    public void setType(TruckTypeFilter type) {
        this.type = type;
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
        final TruckCriteria that = (TruckCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TruckCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
