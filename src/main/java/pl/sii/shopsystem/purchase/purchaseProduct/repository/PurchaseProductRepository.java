package pl.sii.shopsystem.purchase.purchaseProduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;

import java.util.UUID;

public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, UUID> {
}
