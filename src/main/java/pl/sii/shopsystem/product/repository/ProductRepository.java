package pl.sii.shopsystem.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sii.shopsystem.product.persistence.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
