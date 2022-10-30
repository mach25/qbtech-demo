package se.raccoon.qbtechdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.raccoon.qbtechdemo.controller.dto.*;
import se.raccoon.qbtechdemo.exceptions.NotFoundException;
import se.raccoon.qbtechdemo.service.counter.CounterService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/counters")
public class CounterController {

    private static final Logger LOG = LoggerFactory.getLogger(CounterController.class);

    private final CounterService counterService;

    @Autowired
    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @RequestMapping(path = "create", method = RequestMethod.POST)
    public Mono<CreateCounterResponse> createCounter(@RequestBody @NonNull final CreateCounterRequest createCounterRequest) {
        if (createCounterRequest.name().isEmpty()) {
            return Mono.error(new IllegalArgumentException("counter name can not be empty"));
        }
        return counterService.create(createCounterRequest.name()).map(c -> new CreateCounterResponse(new Counter(c.id(), c.name(), c.currentValue())));
    }

    @RequestMapping(path = "increment/{id}", method = RequestMethod.POST)
    public Mono<IncrementResponse> incrementCounter(@PathVariable(name = "id") final UUID id) {
        LOG.info("incrementing {}", id);
        return counterService
                .increment(id)
                .map(IncrementResponse::new);
    }

    @RequestMapping(path = "by-name", method = RequestMethod.POST)
    public Mono<CounterResponse> getValueByName(@RequestBody @NonNull final GetCounterByNameRequest getCounterByNameRequest) {
        if (getCounterByNameRequest.name().isEmpty()) {
            return Mono.error(new IllegalArgumentException("counter name can not be empty"));
        }
        return counterService.getByName(getCounterByNameRequest.name())
                .map(c -> new CounterResponse(new Counter(c.id(), c.name(), c.currentValue())));
    }

    @RequestMapping(path = "by-name", method = RequestMethod.DELETE)
    public Mono<Void> deleteValueByName(@RequestBody @NonNull final GetCounterByNameRequest getCounterByNameRequest) {
        if (getCounterByNameRequest.name().isEmpty()) {
            return Mono.error(new IllegalArgumentException("counter name can not be empty"));
        }
        return counterService.deleteByName(getCounterByNameRequest.name());
    }

    @RequestMapping(path = "all", method = RequestMethod.GET)
    public Flux<Counter> getAllCounters() {
        return counterService.getAll().map(c -> new Counter(c.id(), c.name(), c.currentValue()));
    }

    @RequestMapping(path = "all", method = RequestMethod.DELETE)
    public Mono<Void> deleteAllCounters() {
        return counterService.deleteAll();
    }

    @ResponseStatus(
            value = HttpStatus.NOT_FOUND,
            reason = "Not Found")
    @ExceptionHandler(NotFoundException.class)
    public void notFoundHandler() {
        // ignore
    }

    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Illegal arguments")
    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentHandler() {
        // ignore
    }
}
