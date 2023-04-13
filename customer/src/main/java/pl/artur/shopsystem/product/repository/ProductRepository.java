package pl.artur.shopsystem.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.artur.shopsystem.order.model.ProductOrderSummary;
import pl.artur.shopsystem.product.model.Product;
import product.model.Genre;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    long countByTypeAndIsDeleted(Genre type, boolean isDeleted);
    Page<Product> findAllByNameAndIsDeleted(String name, boolean isDeleted, Pageable pageable);
    Optional<Product> findByIdAndIsDeleted(UUID id, boolean isDeleted);
    @Query("""
            SELECT new pl.artur.shopsystem.order.model.ProductOrderSummary(p.name, p.type, p.manufacturer, count(p.name))
            FROM Product AS p
            WHERE p.order IS NOT NULL
            GROUP BY p.name, p.type, p.manufacturer
            """)
    Page<ProductOrderSummary> findProductOrderSummary(Pageable pageable);
}
