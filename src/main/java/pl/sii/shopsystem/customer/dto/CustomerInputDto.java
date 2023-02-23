package pl.sii.shopsystem.customer.dto;

import lombok.Builder;

@Builder
public record CustomerInputDto(
        String firstname,
        String lastname,
        String email) {
}
