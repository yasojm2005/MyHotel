package com.myhotel.truck.service.dto;

import com.myhotel.truck.domain.enumeration.TruckType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myhotel.truck.domain.Truck} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TruckDTO implements Serializable {

    private Long id;

    private TruckType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TruckType getType() {
        return type;
    }

    public void setType(TruckType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TruckDTO)) {
            return false;
        }

        TruckDTO truckDTO = (TruckDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, truckDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TruckDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
