package se.raccoon.qbtechdemo.controller.dto;

import java.util.UUID;

public record Counter(UUID id, String name, Long currentValue) {
}
