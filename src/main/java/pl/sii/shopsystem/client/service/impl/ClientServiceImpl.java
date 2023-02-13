package pl.sii.shopsystem.client.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sii.shopsystem.client.dto.*;
import pl.sii.shopsystem.client.exception.ClientException;
import pl.sii.shopsystem.client.persistence.*;
import pl.sii.shopsystem.client.repository.ClientRepository;
import pl.sii.shopsystem.purchase.repository.PurchaseRepository;
import pl.sii.shopsystem.client.service.ClientService;
import pl.sii.shopsystem.client.service.Validator;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;
import pl.sii.shopsystem.purchase.persistence.Purchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.*;

@Service
public class ClientServiceImpl implements ClientService {

    private final Validator validator;
    private final ClientRepository clientRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProductRepository purchaseProductRepository;

    public ClientServiceImpl(Validator validator,
                             ClientRepository clientRepository,
                             PurchaseRepository purchaseRepository,
                             PurchaseProductRepository purchaseProductRepository) {
        this.validator = validator;
        this.clientRepository = clientRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseProductRepository = purchaseProductRepository;
    }

    @Override
    public ClientOutputDto addClient(ClientInputDto clientInputDto) {
        boolean isAnyBlank = validator.isAnyBlank(clientInputDto);
        if (isAnyBlank) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
        boolean doesExist = clientRepository.existsByEmail(clientInputDto.email());
        if (doesExist) {
            throw new ClientException(CLIENT_ALREADY_EXISTS.getMessage());
        }

        Client client = clientRepository.save(
                Client.builder()
                        .firstname(clientInputDto.firstname())
                        .lastname(clientInputDto.lastname())
                        .email(clientInputDto.email())
                        .build());

        return ClientOutputDto.builder()
                .id(client.getId())
                .firstname(client.getFirstname())
                .lastname(client.getLastname())
                .email(client.getEmail())
                .build();
    }

    @Override
    public ClientOutputDto getClient(ClientEmailInputDto clientEmailInputDto) {
        boolean isAnyBlank = validator.isAnyBlank(clientEmailInputDto);
        if (isAnyBlank) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }

        Client client = clientRepository.findByEmail(clientEmailInputDto.email())
                .orElseThrow(() -> new ClientException(NO_CLIENT_FOUND.getMessage()));

        return ClientOutputDto.builder()
                .id(client.getId())
                .firstname(client.getFirstname())
                .lastname(client.getLastname())
                .email(client.getEmail())
                .build();
    }

    @Override
    public void removeClient(ClientEmailInputDto clientEmailInputDto) {
        boolean isAnyBlank = validator.isAnyBlank(clientEmailInputDto);
        if (isAnyBlank) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }

        Client client = clientRepository.findByEmail(clientEmailInputDto.email())
                .orElseThrow(() -> new ClientException(NO_CLIENT_FOUND.getMessage()));
        clientRepository.deleteById(client.getId());
    }

    @Override
    public ClientOutputDto updateClient(ClientInputDto clientInputDto) {
        boolean isAnyBlank = validator.isAnyBlank(clientInputDto);
        if (isAnyBlank) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }

        Client client = clientRepository.findByEmail(clientInputDto.email())
                .orElseThrow(() -> new ClientException(NO_CLIENT_FOUND.getMessage()));

        if (!client.getEmail().equals(clientInputDto.email())
                && clientRepository.existsByEmail(client.getEmail())) {
            throw new ClientException(EMAIL_ALREADY_USED.getMessage());
        }


        client.setFirstname(client.getFirstname());
        client.setLastname(client.getLastname());
        client.setEmail(client.getEmail());

        clientRepository.save(client);

        return ClientOutputDto.builder()
                .id(client.getId())
                .firstname(client.getFirstname())
                .lastname(client.getLastname())
                .email(client.getEmail())
                .build();
    }

    @Override
    @Transactional
    public PurchaseOutputDto makePurchase(PurchaseInputDto purchaseInputDto) {
        boolean isAnyBlank = validator.isAnyBlank(purchaseInputDto);
        if (isAnyBlank) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }

        Client client = clientRepository.findById(UUID.fromString(purchaseInputDto.userId()))
                .orElseThrow(() -> new ClientException(NO_CLIENT_FOUND.getMessage()));

        Product product = Product.builder()
                .type("smartphone")
                .title("pixel")
                .manufacturer("google")
                .price("10")
                .build();

        Purchase purchase = Purchase.builder()
                .client(client)
                .cost(new BigDecimal(10))
                .dateTime(LocalDateTime.now())
                .build();

        PurchaseProduct purchaseProduct = PurchaseProduct.builder()
                .purchaseProductKey(new PurchaseProductKey(product.getId(), purchase.getId()))
                .product(product)
                .purchase(purchase)
                .quantity(5)
                .build();

        purchaseRepository.save(purchase);
        purchaseProductRepository.save(purchaseProduct);

        return PurchaseOutputDto.builder()
                .userFirstname(client.getFirstname())
                .userLastname(client.getLastname())
                .userEmail(client.getEmail())
                .productType(product.getType())
                .productTitle(product.getTitle())
                .productManufacturer(product.getManufacturer())
                .productPrice(product.getPrice())
                .build();
    }
}
