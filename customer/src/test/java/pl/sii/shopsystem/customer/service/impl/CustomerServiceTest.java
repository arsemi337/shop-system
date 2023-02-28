package pl.sii.shopsystem.customer.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sii.shopsystem.customer.dto.CustomerEmailInputDto;
import pl.sii.shopsystem.customer.dto.CustomerInputDto;
import pl.sii.shopsystem.customer.dto.CustomerOutputDto;
import pl.sii.shopsystem.customer.dto.UpdateCustomerInputDto;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.customer.repository.CustomerRepository;
import pl.sii.shopsystem.customer.service.CustomerValidator;
import supplier.TimeSupplier;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static exception.CustomerExceptionMessages.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    CustomerServiceImpl underTest;
    @Mock
    CustomerValidator validator;
    @Mock
    TimeSupplier timeSupplier;
    @Mock
    CustomerRepository customerRepository;

    @Test
    @DisplayName("when customer data is passed, a newly created customer is returned")
    void addCustomer_should_createCustomer_when_validDataIsPassed() {
        // given
        CustomerInputDto input = CustomerInputDto.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2010, 1, 1, 1, 10, 30));
        when(customerRepository.save(any(Customer.class))).thenAnswer(answer -> answer.getArgument(0));

        // when
        CustomerOutputDto customerOutput = underTest.addCustomer(input);

        // then
        assertEquals(input.firstname(), customerOutput.firstname());
        assertEquals(input.lastname(), customerOutput.lastname());
        assertEquals(input.email(), customerOutput.email());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("when a proper email is passed, a proper customer is fetched")
    void getCustomer_should_fetchCustomer_when_validEmailIsPassed() {
        // given
        String email = "jdoe@sii.pl";
        UUID uuid = UUID.randomUUID();
        CustomerEmailInputDto input = CustomerEmailInputDto.builder()
                .email(email)
                .build();
        Customer customer = Customer.builder()
                .id(uuid)
                .firstname("John")
                .lastname("Doe")
                .email(email)
                .build();
        when(customerRepository.findByEmail(input.email())).thenReturn(Optional.of(customer));

        // when
        CustomerOutputDto customerOutput = underTest.getCustomer(input);

        // then
        assertEquals(customerOutput.id(), uuid);
        assertEquals(customerOutput.firstname(), customer.getFirstname());
        assertEquals(customerOutput.lastname(), customer.getLastname());
        assertEquals(customerOutput.email(), customer.getEmail());
        verify(customerRepository).findByEmail(email);
    }

    @Test
    @DisplayName("when an email of non existing customer is passed, a NoSuchElementException exception should be thrown")
    void getCustomer_should_throwNoSuchElementException_when_emailOfNonExistingCustomerIsPassed() {
        // given
        String email = "jdoe@sii.pl";
        CustomerEmailInputDto input = CustomerEmailInputDto.builder()
                .email(email)
                .build();
        when(customerRepository.findByEmail(input.email())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> underTest.getCustomer(input)).withMessage(NO_CUSTOMER_BY_EMAIL_FOUND.getMessage());
        verify(customerRepository).findByEmail(email);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("when a proper email is passed, a proper customer is removed")
    void removeCustomer_should_removeCustomer_when_validEmailIsPassed() {
        // given
        String email = "jdoe@sii.pl";
        UUID uuid = UUID.randomUUID();
        CustomerEmailInputDto input = CustomerEmailInputDto.builder()
                .email(email)
                .build();
        Customer customer = Customer.builder()
                .id(uuid)
                .firstname("John")
                .lastname("Doe")
                .email(email)
                .build();
        when(customerRepository.findByEmail(input.email())).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).deleteById(uuid);

        // when
        underTest.removeCustomer(input);

        // then
        verify(customerRepository).findByEmail(email);
        verify(customerRepository).deleteById(uuid);
    }

    @Test
    @DisplayName("when an email of non existing customer is passed, a NoSuchElementException exception should be thrown")
    void removeCustomer_should_throwNoSuchElementException_when_emailOfNonExistingCustomerIsPassed() {
        // given
        String email = "jdoe@sii.pl";
        CustomerEmailInputDto input = CustomerEmailInputDto.builder()
                .email(email)
                .build();
        when(customerRepository.findByEmail(input.email())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> underTest.removeCustomer(input)).withMessage(NO_CUSTOMER_BY_EMAIL_FOUND.getMessage());
        verify(customerRepository).findByEmail(email);
        verify(customerRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("when customer data is passed, an updated customer should be returned")
    void updateCustomer_should_updateCustomer_when_validDataIsPassed() {
        // given
        UUID uuid = UUID.randomUUID();
        UpdateCustomerInputDto input = UpdateCustomerInputDto.builder()
                .id(uuid.toString())
                .newFirstname("Mark")
                .newLastname("Wellington")
                .newEmail("mwellington@sii.pl")
                .build();
        Customer customer = Customer.builder()
                .id(uuid)
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        when(customerRepository.findById(uuid)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // when
        CustomerOutputDto output = underTest.updateCustomer(input);

        // then
        assertEquals(output.id().toString(), input.id());
        assertEquals(output.firstname(), input.newFirstname());
        assertEquals(output.lastname(), input.newLastname());
        assertEquals(output.email(), input.newEmail());
        verify(customerRepository).findById(uuid);
        verify(customerRepository).save(customer);
    }

    @Test
    @DisplayName("when an id of non existing customer is passed, a NoSuchElementException exception should be thrown")
    void updateCustomer_should_throwNoSuchElementException_when_emailOfNonExistingCustomerIsPassed() {
        // given
        UUID uuid = UUID.randomUUID();
        UpdateCustomerInputDto input = UpdateCustomerInputDto.builder()
                .id(uuid.toString())
                .newFirstname("Mark")
                .newLastname("Wellington")
                .newEmail("mwellington@sii.pl")
                .build();
        when(customerRepository.findById(uuid)).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> underTest.updateCustomer(input)).withMessage(NO_CUSTOMER_BY_ID_FOUND.getMessage());
        verify(customerRepository).findById(uuid);
        verify(customerRepository, never()).save(any(Customer.class));
    }
}