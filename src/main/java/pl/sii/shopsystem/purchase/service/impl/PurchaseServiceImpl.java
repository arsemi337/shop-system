package pl.sii.shopsystem.purchase.service.impl;

import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sii.shopsystem.client.exception.ClientException;
import pl.sii.shopsystem.client.persistence.Client;
import pl.sii.shopsystem.client.repository.ClientRepository;
import pl.sii.shopsystem.common.TimeSupplier;
import pl.sii.shopsystem.common.Validator;
import pl.sii.shopsystem.product.persistence.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;
import pl.sii.shopsystem.purchase.persistence.Purchase;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductInputDto;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductOutputDto;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProductKey;
import pl.sii.shopsystem.purchase.purchaseProduct.repository.PurchaseProductRepository;
import pl.sii.shopsystem.purchase.repository.PurchaseRepository;
import pl.sii.shopsystem.purchase.service.PurchaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.NO_CLIENT_FOUND;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final Validator validator;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final TimeSupplier timeSupplier;

    public PurchaseServiceImpl(Validator validator,
                               ClientRepository clientRepository,
                               ProductRepository productRepository,
                               PurchaseRepository purchaseRepository,
                               PurchaseProductRepository purchaseProductRepository,
                               TimeSupplier timeSupplier) {
        this.validator = validator;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseProductRepository = purchaseProductRepository;
        this.timeSupplier = timeSupplier;
    }

    @Override
    @Transactional
    public PurchaseOutputDto makePurchase(PurchaseInputDto purchaseInputDto) {
        boolean isAnyBlank = validator.isAnyBlank(purchaseInputDto); //TODO take care of Validator class
        if (isAnyBlank) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }

        // Get purchase client
        Client client = clientRepository.findById(UUID.fromString(purchaseInputDto.userId()))
                .orElseThrow(() -> new ClientException(NO_CLIENT_FOUND.getMessage()));

        // Create a list of objects, each of which contains a desired product and its quantity
        List<ProductQuantity> productQuantities = purchaseInputDto.purchaseProducts()
                .stream()
                .map(this::createProductQuantity)
                .flatMap(Optional::stream)
                .toList();

        // Calculate total purchase cost
        BigDecimal totalCost = calculateTotalCost(productQuantities);

        // Create purchase object and save it in a database
        Purchase purchase = Purchase.builder()
                .client(client)
                .cost(totalCost)
                .dateTime(timeSupplier.get())
                .build();
        purchaseRepository.save(purchase);

        productQuantities
                .stream()
                .map(productQuantity -> PurchaseProduct.builder()
                        .purchaseProductKey(
                                PurchaseProductKey.builder()
                                        .productId(productQuantity.product().getId())
                                        .purchaseId(purchase.getId())
                                        .build())
                                .product(productQuantity.product)
                                .purchase(purchase)
                                .quantity(productQuantity.quantity)
                                .build())
                        .map(purchaseProductRepository::save);

        return PurchaseOutputDto.builder()
                .clientFirstname(client.getFirstname())
                .clientLastname(client.getLastname())
                .clientEmail(client.getEmail())
                .purchasedProducts(getPurchaseProductOutputDtoList(productQuantities))
                .totalCost(totalCost)
                .build();
    }

    private List<PurchaseProductOutputDto> getPurchaseProductOutputDtoList(List<ProductQuantity> productQuantities) {
        return productQuantities
                .stream()
                .map(productQuantity -> PurchaseProductOutputDto.builder()
                        .title(productQuantity.product.getTitle())
                        .type(productQuantity.product.getType())
                        .manufacturer(productQuantity.product.getManufacturer())
                        .price(productQuantity.product.getPrice())
                        .quantity(productQuantity.quantity)
                        .build())
                .toList();
    }

    private Optional<ProductQuantity> createProductQuantity(PurchaseProductInputDto inputDto) {
        UUID uuid = UUID.fromString(inputDto.productId());
        Product product = productRepository.findById(uuid).orElse(null);
        if (product == null || !isQuantityValid(inputDto.quantity())) {
            return Optional.empty();
        }
        ProductQuantity productQuantity = ProductQuantity.builder()
                .product(product)
                .quantity(Integer.parseInt(inputDto.quantity())) //TODO pomyśleć nad tym, że dwa razy jest parsowane to quantity
                .build();
        return Optional.of(productQuantity);
    }

    private boolean isQuantityValid(String quantity) {
        int quantityNumber;
        try {
            quantityNumber = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            return false;
        }
        return quantityNumber >= 0;
    }

    private BigDecimal calculateTotalCost(List<ProductQuantity> productQuantities) {
        BigDecimal totalCost = new BigDecimal(0);
        BigDecimal quantity;
        BigDecimal price;

        for (ProductQuantity productQuantity : productQuantities) {
            quantity = new BigDecimal(productQuantity.quantity);
            price = productQuantity.product.getPrice();

            BigDecimal productCost = price.multiply(quantity);
            totalCost = totalCost.add(productCost);
        }
        return totalCost;
    }

    @Builder
    private record ProductQuantity(Product product, int quantity) {
    }
}
