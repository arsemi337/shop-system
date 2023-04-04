package pl.artur.shopsystem.customer.dto;

import lombok.Builder;

@Builder
public record UpdateCustomerInputDto(
        String id,
        String newFirstname,
        String newLastname,
        String newEmail
) {
}
