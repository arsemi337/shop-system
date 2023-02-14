package pl.sii.shopsystem.client.dto;

import lombok.Builder;

@Builder
public record ClientEmailInputDto(
        String email) {
}
