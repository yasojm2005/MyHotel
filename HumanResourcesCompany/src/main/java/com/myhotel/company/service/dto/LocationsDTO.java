package com.myhotel.company.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myhotel.company.domain.Locations} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationsDTO implements Serializable {

    private Long id;

    private String streetAddress;

    private String postalCode;

    private String city;

    private String province;

    private CountriesDTO countries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public CountriesDTO getCountries() {
        return countries;
    }

    public void setCountries(CountriesDTO countries) {
        this.countries = countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationsDTO)) {
            return false;
        }

        LocationsDTO locationsDTO = (LocationsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationsDTO{" +
            "id=" + getId() +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", province='" + getProvince() + "'" +
            ", countries=" + getCountries() +
            "}";
    }
}
