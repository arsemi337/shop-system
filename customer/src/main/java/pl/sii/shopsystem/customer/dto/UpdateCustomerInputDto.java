package pl.sii.shopsystem.customer.dto;

public record UpdateCustomerInputDto(
        String id,
        String newFirstname,
        String newLastname,
        String newEmail
) {
}
