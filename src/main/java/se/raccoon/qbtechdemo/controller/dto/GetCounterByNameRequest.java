package se.raccoon.qbtechdemo.controller.dto;

import org.springframework.lang.NonNull;

public record GetCounterByNameRequest(@NonNull String name) {
}
