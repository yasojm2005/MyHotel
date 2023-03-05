package com.myhotel.company.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myhotel.company.domain.Countries} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountriesDTO implements Serializable {

    private Long id;

    private String countryName;

    private RegionDTO region;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public RegionDTO getRegion() {
        return region;
    }

    public void setRegion(RegionDTO region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountriesDTO)) {
            return false;
        }

        CountriesDTO countriesDTO = (CountriesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countriesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountriesDTO{" +
            "id=" + getId() +
            ", countryName='" + getCountryName() + "'" +
            ", region=" + getRegion() +
            "}";
    }
}
