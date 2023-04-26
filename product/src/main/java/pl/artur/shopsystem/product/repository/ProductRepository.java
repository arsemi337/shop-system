package pl.artur.shopsystem.product.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import pl.artur.shopsystem.product.model.Product;
import product.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Page<Product> findAllByIsDeleted(Pageable pageable, boolean isDeleted);
    Optional<Product> findByIdAndIsDeleted(UUID id, boolean isDeleted);
    boolean existsByNameAndIsDeleted(String name, boolean isDeleted);
    Optional<Product> findFirstByNameAndIsDeleted(String name, boolean isDeleted);
    List<Product> findAllByNameAndIsDeleted(String name, boolean isDeleted);
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Page<Product> findAllByNameAndIsDeleted(String name, boolean isDeleted, Pageable pageable);
    long countByNameAndIsDeleted(String name, boolean isDeleted);
    long countByTypeAndIsDeleted(Genre type, boolean isDeleted);
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    void deleteById(UUID id);
}
