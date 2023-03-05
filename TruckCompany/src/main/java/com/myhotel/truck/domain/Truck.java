package com.myhotel.truck.domain;

import com.myhotel.truck.domain.enumeration.TruckType;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Truck.
 */
@Entity
@Table(name = "truck")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Truck extends Vehicle {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TruckType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Truck id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TruckType getType() {
        return this.type;
    }

    public Truck type(TruckType type) {
        this.setType(type);
        return this;
    }

    public void setType(TruckType type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Truck)) {
            return false;
        }
        return id != null && id.equals(((Truck) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Truck{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
