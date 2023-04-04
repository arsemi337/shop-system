package pl.artur.shopsystem.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.artur.shopsystem.product.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByName(String name);
    Optional<Product> findFirstByName(String name);
    List<Product> findAllByName(String name);
}
