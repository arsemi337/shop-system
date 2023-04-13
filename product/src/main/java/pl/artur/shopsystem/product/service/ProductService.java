package pl.artur.shopsystem.product.service;

import order.OrderProductInputDto;
import order.OrderProductOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.artur.shopsystem.product.dto.*;

import java.util.List;

public interface ProductService {

    List<MassProductOutputDto> addProducts(List<AddProductInputDto> alterProductInputDtoList);
    Page<ProductOutputDto> fetchAllProducts(Pageable pageable);
    ProductOutputDto fetchProductById(String id);
    MassProductOutputDto updateProduct(AlterProductInputDto alterProductInputDto);
    void removeProductsList(RemoveProductInputDto removeProductInputDto);
    void removeProduct(String productId);
    List<OrderProductOutputDto> purchaseProducts(List<OrderProductInputDto> orderProductInputDtoList);
}
