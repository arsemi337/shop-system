package pl.sii.shopsystem.customer.dto;

import lombok.Builder;

@Builder
public record CustomerEmailInputDto(
        String email) {
}
