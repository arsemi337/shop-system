package pl.artur.shopsystem.customer.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.artur.shopsystem.customer.dto.CustomerOutputDto;
import pl.artur.shopsystem.customer.model.Customer;
import pl.artur.shopsystem.customer.dto.CustomerInputDto;
import supplier.TimeSupplier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerMapperTest {

    @InjectMocks
    CustomerMapper underTest;
    @Mock
    TimeSupplier timeSupplier;

    @Test
    @DisplayName("when CustomerInputData object passed, a Customer object should be returned")
    void mapToCustomer_should_mapToCustomer_when_CustomerInputDtoIsPassed() {
        // given
        CustomerInputDto input = CustomerInputDto.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@email.pl")
                .build();
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2010, 1, 1, 1, 10, 30));

        // when
        Customer customer = underTest.mapToCustomer(input);

        // then
        assertEquals(customer.getFirstname(), input.firstname());
        assertEquals(customer.getLastname(), input.lastname());
        assertEquals(customer.getEmail(), input.email());
        assertEquals(customer.getCreationTime(), timeSupplier.get());
    }

    @Test
    @DisplayName("when Customer object passed, a CustomerOutputDto object should be returned")
    void mapToCustomerOutputDto_should_mapToCustomerOutputDto_when_CustomerIsPassed() {
        // given
        UUID uuid = UUID.randomUUID();
        Customer customer = Customer.builder()
                .id(uuid)
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@email.pl")
                .build();

        // when
        CustomerOutputDto output = underTest.mapToCustomerOutputDto(customer);

        // then
        assertEquals(output.id(), customer.getId());
        assertEquals(output.firstname(), customer.getFirstname());
        assertEquals(output.lastname(), customer.getLastname());
        assertEquals(output.email(), customer.getEmail());
    }
}
