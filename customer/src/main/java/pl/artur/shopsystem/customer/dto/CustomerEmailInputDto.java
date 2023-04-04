package pl.artur.shopsystem.customer.dto;

import lombok.Builder;

@Builder
public record CustomerEmailInputDto(
        String email) {
}
