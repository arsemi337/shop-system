package pl.sii.shopsystem.purchase.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sii.shopsystem.client.exception.ClientException;
import pl.sii.shopsystem.client.persistence.Client;
import pl.sii.shopsystem.client.repository.ClientRepository;
import pl.sii.shopsystem.client.service.impl.ProductQuantity;
import pl.sii.shopsystem.common.TimeSupplier;
import pl.sii.shopsystem.product.persistence.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;
import pl.sii.shopsystem.purchase.exception.PurchaseException;
import pl.sii.shopsystem.purchase.persistence.Purchase;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductInputDto;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;
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
    private final PurchaseParser parser;
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
        this.parser = new PurchaseParser();
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
        List<ProductQuantity> productQuantities = purchaseMapper.mapToProductQuantities(
                purchaseInputDto,
                this::createProductQuantity);

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
        List<PurchaseProduct> purchaseProducts = purchaseMapper.mapToPurchaseProducts(productQuantities, purchase);
        purchaseProducts.forEach(purchaseProductRepository::save);

        return purchaseMapper.mapToPurchaseOutputDto(
                client,
                purchaseMapper.mapToPurchaseProductOutputDtoList(productQuantities),
                totalCost);
    }

    private Client getClientById(PurchaseInputDto purchaseInputDto) {
        UUID clientId = parser.parseUUID(purchaseInputDto.clientId());
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientException(NO_CLIENT_FOUND.getMessage()));
    }

    private BigDecimal calculateTotalCost(List<ProductQuantity> productQuantities) {
        BigDecimal totalCost = new BigDecimal(0);
        BigDecimal quantity;
        BigDecimal price;

        for (ProductQuantity productQuantity : productQuantities) {
            quantity = new BigDecimal(productQuantity.quantity());
            price = productQuantity.product().getPrice();

            BigDecimal productCost = price.multiply(quantity);
            totalCost = totalCost.add(productCost);
        }
        return totalCost;
    }

    private Optional<ProductQuantity> createProductQuantity(PurchaseProductInputDto inputDto) {
        UUID uuid = parser.parseUUID(inputDto.productId());
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new PurchaseException(PRODUCT_NOT_FOUND.getMessage()));

        int quantity = parser.parseQuantity(inputDto.quantity());
        validator.validateQuantity(quantity);

        ProductQuantity productQuantity = ProductQuantity.builder()
                .product(product)
                .quantity(quantity)
                .build();
        return Optional.of(productQuantity);
    }
}