package se.raccoon.qbtechdemo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.raccoon.qbtechdemo.repository.entities.CounterEntity;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface CounterRepository extends CrudRepository<CounterEntity, UUID> {
    Optional<CounterEntity> findByName(String name);

    /**
     * Native query here because of some issues with hibernate, h2 and entity definition.
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM counters", nativeQuery = true)
    void removeAllCounters();
}
