package pl.sii.shopsystem.client.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, UUID> {
}
