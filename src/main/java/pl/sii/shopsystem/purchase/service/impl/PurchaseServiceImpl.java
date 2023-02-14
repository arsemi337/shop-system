package pl.sii.shopsystem.purchase.service.impl;

import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sii.shopsystem.client.exception.ClientException;
import pl.sii.shopsystem.client.persistence.Client;
import pl.sii.shopsystem.client.repository.ClientRepository;
import pl.sii.shopsystem.common.TimeSupplier;
import pl.sii.shopsystem.product.persistence.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;
import pl.sii.shopsystem.purchase.exception.PurchaseException;
import pl.sii.shopsystem.purchase.persistence.Purchase;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductInputDto;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductOutputDto;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProductKey;
import pl.sii.shopsystem.purchase.purchaseProduct.repository.PurchaseProductRepository;
import pl.sii.shopsystem.purchase.repository.PurchaseRepository;
import pl.sii.shopsystem.purchase.service.PurchaseService;
import pl.sii.shopsystem.purchase.service.PurchaseValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.NO_CLIENT_FOUND;
import static pl.sii.shopsystem.purchase.exception.PurchaseExceptionMessages.*;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseValidator validator;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final TimeSupplier timeSupplier;
    private final PurchaseMapper purchaseMapper;

    public PurchaseServiceImpl(PurchaseValidator validator,
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
        this.purchaseMapper = new PurchaseMapper();
    }

    @Override
    @Transactional
    public PurchaseOutputDto makePurchase(PurchaseInputDto purchaseInputDto) {
        validator.validatePurchaseInputDto(purchaseInputDto);

        // Get purchase client
        Client client = getClientById(purchaseInputDto);

        // Create a list of objects, each of which contains a desired product and its quantity
        List<ProductQuantity> productQuantities = getProductQuantities(purchaseInputDto);

        // Calculate total purchase cost
        BigDecimal totalCost = calculateTotalCost(productQuantities);

        // Create purchase object and save it in a database
        Purchase purchase = Purchase.builder()
                .client(client)
                .cost(totalCost)
                .dateTime(timeSupplier.get())
                .build();
        purchaseRepository.save(purchase);

        // Create PurchaseProduct objects and save them in a database
        List<PurchaseProduct> purchaseProducts = getPurchaseProducts(productQuantities, purchase);
        purchaseProducts.forEach(purchaseProductRepository::save);

        return purchaseMapper.mapToPurchaseOutputDto(
                client,
                getPurchaseProductOutputDtoList(productQuantities),
                totalCost);
    }

    private Client getClientById(PurchaseInputDto purchaseInputDto) {
        return clientRepository.findById(UUID.fromString(purchaseInputDto.clientId()))
                .orElseThrow(() -> new ClientException(NO_CLIENT_FOUND.getMessage()));
    }

    private List<ProductQuantity> getProductQuantities(PurchaseInputDto purchaseInputDto) {
        return purchaseInputDto.purchaseProducts()
                .stream()
                .map(this::createProductQuantity)
                .flatMap(Optional::stream)
                .toList();
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

    private List<PurchaseProduct> getPurchaseProducts(List<ProductQuantity> productQuantities, Purchase purchase) {
        return productQuantities
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
                .toList();
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
        UUID uuid = parseUUID(inputDto);
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new PurchaseException(PRODUCT_NOT_FOUND.getMessage()));

        int quantity = parseQuantity(inputDto.quantity());
        validateQuantity(quantity);

        ProductQuantity productQuantity = ProductQuantity.builder()
                .product(product)
                .quantity(quantity)
                .build();
        return Optional.of(productQuantity);
    }

    private UUID parseUUID(PurchaseProductInputDto inputDto) {
        try {
            return UUID.fromString(inputDto.productId());
        } catch (IllegalArgumentException e) {
            throw new PurchaseException(PRODUCT_ID_HAS_INVALID_FORM.getMessage() + inputDto.productId());
        }
    }

    private int parseQuantity(String quantity) {
        int quantityNumber;
        try {
            quantityNumber = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new PurchaseException(PRODUCT_QUANTITY_IS_NOT_NUMBER.getMessage() + quantity);
        }
        return quantityNumber;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new PurchaseException(PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO.getMessage() + quantity);
        }
    }

    @Builder
    private record ProductQuantity(Product product, int quantity) {
    }
}
