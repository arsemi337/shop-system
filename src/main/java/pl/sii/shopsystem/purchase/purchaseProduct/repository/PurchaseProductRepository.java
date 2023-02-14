package pl.sii.shopsystem.purchase.purchaseProduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;

import java.util.UUID;

@Repository
public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, UUID> {
}
