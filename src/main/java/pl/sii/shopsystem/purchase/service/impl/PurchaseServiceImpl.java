package pl.sii.shopsystem.purchase.service.impl;

import org.springframework.transaction.annotation.Transactional;
import pl.sii.shopsystem.client.exception.ClientException;
import pl.sii.shopsystem.client.persistence.*;
import pl.sii.shopsystem.client.repository.ClientRepository;
import pl.sii.shopsystem.product.persistence.Product;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;
import pl.sii.shopsystem.purchase.persistence.Purchase;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProductKey;
import pl.sii.shopsystem.purchase.purchaseProduct.repository.PurchaseProductRepository;
import pl.sii.shopsystem.purchase.repository.PurchaseRepository;
import pl.sii.shopsystem.purchase.service.PurchaseService;
import pl.sii.shopsystem.utils.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.NO_CLIENT_FOUND;

public class PurchaseServiceImpl implements PurchaseService {

    private final Validator validator;
    private final ClientRepository clientRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProductRepository purchaseProductRepository;

    public PurchaseServiceImpl(Validator validator,
                             ClientRepository clientRepository,
                             PurchaseRepository purchaseRepository,
                             PurchaseProductRepository purchaseProductRepository) {
        this.validator = validator;
        this.clientRepository = clientRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseProductRepository = purchaseProductRepository;
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
