package com.myhotel.company.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myhotel.company.domain.Locations} entity. This class is used
 * in {@link com.myhotel.company.web.rest.LocationsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter streetAddress;

    private StringFilter postalCode;

    private StringFilter city;

    private StringFilter province;

    private LongFilter countriesId;

    private Boolean distinct;

    public LocationsCriteria() {}

    public LocationsCriteria(LocationsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.province = other.province == null ? null : other.province.copy();
        this.countriesId = other.countriesId == null ? null : other.countriesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LocationsCriteria copy() {
        return new LocationsCriteria(this);
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

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public StringFilter streetAddress() {
        if (streetAddress == null) {
            streetAddress = new StringFilter();
        }
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            postalCode = new StringFilter();
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getProvince() {
        return province;
    }

    public StringFilter province() {
        if (province == null) {
            province = new StringFilter();
        }
        return province;
    }

    public void setProvince(StringFilter province) {
        this.province = province;
    }

    public LongFilter getCountriesId() {
        return countriesId;
    }

    public LongFilter countriesId() {
        if (countriesId == null) {
            countriesId = new LongFilter();
        }
        return countriesId;
    }

    public void setCountriesId(LongFilter countriesId) {
        this.countriesId = countriesId;
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
        final LocationsCriteria that = (LocationsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(city, that.city) &&
            Objects.equals(province, that.province) &&
            Objects.equals(countriesId, that.countriesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, streetAddress, postalCode, city, province, countriesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (province != null ? "province=" + province + ", " : "") +
            (countriesId != null ? "countriesId=" + countriesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
