package pl.sii.shopsystem.client.dto;

import lombok.Builder;

@Builder
public record ClientInputDto(String firstname, String lastname, String email) {
}
