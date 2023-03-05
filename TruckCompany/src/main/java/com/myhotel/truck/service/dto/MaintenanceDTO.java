package com.myhotel.truck.service.dto;

import com.myhotel.truck.domain.enumeration.MaintainanceType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.myhotel.truck.domain.Maintenance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private MaintainanceType type;

    private String description;

    private BigDecimal price;

    private VehicleDTO vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MaintainanceType getType() {
        return type;
    }

    public void setType(MaintainanceType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintenanceDTO)) {
            return false;
        }

        MaintenanceDTO maintenanceDTO = (MaintenanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maintenanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", vehicle=" + getVehicle() +
            "}";
    }
}
