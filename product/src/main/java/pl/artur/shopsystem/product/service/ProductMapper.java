package pl.artur.shopsystem.product.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.artur.shopsystem.product.dto.AddProductInputDto;
import pl.artur.shopsystem.product.dto.MassProductOutputDto;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.model.Product;

import java.util.Map;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product mapToProduct(AddProductInputDto addProductInputDto);

    ProductOutputDto mapToProductOutputDto(Product product);

    @Mapping(target = "name", expression = "java(productToProductCountEntry.getKey().getName())")
    @Mapping(target = "type", expression = "java(productToProductCountEntry.getKey().getType())")
    @Mapping(target = "manufacturer", expression = "java(productToProductCountEntry.getKey().getManufacturer())")
    @Mapping(target = "price", expression = "java(productToProductCountEntry.getKey().getPrice())")
    @Mapping(target = "number", expression = "java(productToProductCountEntry.getValue())")
    MassProductOutputDto mapToMassProductOutputDto(Map.Entry<Product, Integer> productToProductCountEntry);
}
