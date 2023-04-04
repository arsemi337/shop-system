package pl.artur.shopsystem.product.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import pl.artur.shopsystem.product.model.Product;
import product.model.Genre;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    long countByType(Genre type);
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Product> findAllByTitle(String title);
}
