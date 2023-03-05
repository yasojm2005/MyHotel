package com.myhotel.truck.service.criteria;

import com.myhotel.truck.domain.enumeration.MaintainanceType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myhotel.truck.domain.Maintenance} entity. This class is used
 * in {@link com.myhotel.truck.web.rest.MaintenanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /maintenances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MaintainanceType
     */
    public static class MaintainanceTypeFilter extends Filter<MaintainanceType> {

        public MaintainanceTypeFilter() {}

        public MaintainanceTypeFilter(MaintainanceTypeFilter filter) {
            super(filter);
        }

        @Override
        public MaintainanceTypeFilter copy() {
            return new MaintainanceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private MaintainanceTypeFilter type;

    private StringFilter description;

    private BigDecimalFilter price;

    private LongFilter vehicleId;

    private Boolean distinct;

    public MaintenanceCriteria() {}

    public MaintenanceCriteria(MaintenanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.vehicleId = other.vehicleId == null ? null : other.vehicleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MaintenanceCriteria copy() {
        return new MaintenanceCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public MaintainanceTypeFilter getType() {
        return type;
    }

    public MaintainanceTypeFilter type() {
        if (type == null) {
            type = new MaintainanceTypeFilter();
        }
        return type;
    }

    public void setType(MaintainanceTypeFilter type) {
        this.type = type;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public LongFilter getVehicleId() {
        return vehicleId;
    }

    public LongFilter vehicleId() {
        if (vehicleId == null) {
            vehicleId = new LongFilter();
        }
        return vehicleId;
    }

    public void setVehicleId(LongFilter vehicleId) {
        this.vehicleId = vehicleId;
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
        final MaintenanceCriteria that = (MaintenanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, type, description, price, vehicleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (vehicleId != null ? "vehicleId=" + vehicleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
