package pl.sii.shopsystem.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sii.shopsystem.purchase.persistence.Purchase;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
}
