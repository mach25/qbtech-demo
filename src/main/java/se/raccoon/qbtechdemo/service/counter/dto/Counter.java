package se.raccoon.qbtechdemo.service.counter.dto;

import java.util.UUID;

public record Counter(UUID id, String name, Long currentValue) {
}
