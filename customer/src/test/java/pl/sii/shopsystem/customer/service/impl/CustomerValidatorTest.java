package pl.sii.shopsystem.customer.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sii.shopsystem.customer.dto.CustomerInputDto;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.customer.repository.CustomerRepository;

import static exception.CustomerExceptionMessages.CUSTOMER_ALREADY_EXISTS;
import static exception.CustomerExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerValidatorTest {
    @InjectMocks
    CustomerValidatorImpl underTest;
    @Mock
    CustomerRepository customerRepository;

    @ParameterizedTest
    @ValueSource(strings = {" ", "\t", "\n", "\f"})
    @NullAndEmptySource
    @DisplayName("when an consumer with blank fields is passed, an IllegalArgumentException should be thrown")
    void validateCustomerInputDto_should_throwIllegalArgumentException_when_consumerWithBlankFieldsIsPassed(String name) {
        // given
        CustomerInputDto input = CustomerInputDto.builder()
                .firstname(name)
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateCustomerInputDto(input)).withMessage(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
    }

    @Test
    @DisplayName("when an already existing consumer is passed, an IllegalArgumentException should be thrown")
    void validateCustomerExistence_should_throwIllegalArgumentException_when_alreadyExistingConsumerIsPassed() {
        // given
        String email = "jdoe@sii.pl";
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateCustomerExistence(email)).withMessage(CUSTOMER_ALREADY_EXISTS.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    private Customer addCustomerToDatabase() {
        Customer customer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        return customerRepository.save(customer);
    }
}
