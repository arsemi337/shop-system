package pl.sii.shopsystem.customer.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CustomerOutputDto(
        UUID id,
        String firstname,
        String lastname,
        String email) {
}
