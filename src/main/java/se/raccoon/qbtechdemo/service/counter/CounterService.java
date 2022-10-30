package se.raccoon.qbtechdemo.service.counter;

import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.raccoon.qbtechdemo.service.counter.dto.Counter;

import java.util.UUID;

public interface CounterService {
    Mono<Long> increment(UUID id);
    Mono<Counter> getByName(@NonNull final  String name);
    Flux<Counter> getAll();
    Mono<Counter> create(String name);
    Mono<Void> deleteAll();

    Mono<Void> deleteByName(String name);
}
