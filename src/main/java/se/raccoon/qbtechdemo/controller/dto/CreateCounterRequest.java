package se.raccoon.qbtechdemo.controller.dto;

import org.springframework.lang.NonNull;

public record CreateCounterRequest(@NonNull String name) {
}
