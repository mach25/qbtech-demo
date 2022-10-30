package se.raccoon.qbtechdemo.service.counter;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import se.raccoon.qbtechdemo.exceptions.NotFoundException;
import se.raccoon.qbtechdemo.repository.CounterRepository;
import se.raccoon.qbtechdemo.repository.entities.CounterEntity;
import se.raccoon.qbtechdemo.service.counter.dto.Counter;

import java.util.UUID;

@Service
public class CounterServiceImpl implements CounterService {

    private final CounterRepository counterRepository;

    @Autowired
    public CounterServiceImpl(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Override
    public Mono<Long> increment(UUID id) {
        return Mono.fromCallable(() -> counterRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("counter not found"))
                ).publishOn(Schedulers.boundedElastic())
                .flatMap(entity -> Mono.fromCallable(() -> {
                    entity.setCurrentValue(entity.getCurrentValue() + 1);
                    counterRepository.save(entity);
                    return entity.getCurrentValue();
                }).publishOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<Counter> getByName(String name) {
        return Mono.fromCallable(() -> counterRepository
                        .findByName(name)
                        .orElseThrow(() -> new NotFoundException("counter not found"))
                ).publishOn(Schedulers.boundedElastic())
                .map(entity -> new Counter(entity.getId(), entity.getName(), entity.getCurrentValue()));
    }

    @Override
    public Flux<Counter> getAll() {
        return Mono.fromCallable(counterRepository::findAll)
                .publishOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable).map(entity -> new Counter(entity.getId(), entity.getName(), entity.getCurrentValue()));
    }

    @Override
    public Mono<Counter> create(String name) {
        var entity = new CounterEntity(UUID.randomUUID(), name, 1L);
        return Mono.fromCallable(() -> {
                    var current = counterRepository
                            .findByName(name);
                    if (current.isPresent()) {
                        throw new IllegalArgumentException("Counter name must be unique");
                    }
                    counterRepository.save(entity);
                    return new Counter(entity.getId(), entity.getName(), entity.getCurrentValue());
                })
                .publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(counterRepository::removeAllCounters)
                .publishOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<Void> deleteByName(String name) {
        return Mono.fromCallable(() -> counterRepository
                        .findByName(name).orElse(null)
                ).publishOn(Schedulers.boundedElastic())
                .flatMap(entity -> Mono.fromRunnable(() -> counterRepository.delete(entity)).publishOn(Schedulers.boundedElastic())).then();
    }
}
