package pl.sii.shopsystem.client.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ClientOutputDto(UUID id, String firstname, String lastname, String email) {
}
