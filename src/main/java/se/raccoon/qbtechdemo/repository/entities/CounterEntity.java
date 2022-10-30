package se.raccoon.qbtechdemo.repository.entities;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "counters",
indexes = {
        @Index(name = "idx_name", columnList = "name", unique = true)
})
public class CounterEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 52325L;
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "currentValue")
    private Long currentValue;

    public CounterEntity() {
    }

    public CounterEntity(UUID id, String name, Long currentValue) {
        this.id = id;
        this.name = name;
        this.currentValue = currentValue;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterEntity that = (CounterEntity) o;
        return id.equals(that.id) && name.equals(that.name) && currentValue.equals(that.currentValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, currentValue);
    }

    @Override
    public String toString() {
        return "CounterEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentValue=" + currentValue +
                '}';
    }
}
