package pl.sii.shopsystem.order.orderProduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;

import java.util.UUID;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
}
