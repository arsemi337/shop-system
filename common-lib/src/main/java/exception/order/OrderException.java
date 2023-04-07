package exception.order;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderException extends RuntimeException {

    private final List<ProductErrorDto> errorDtoList;

    public OrderException(String errorMessage, List<ProductErrorDto> errorDtoList) {
        super(errorMessage);
        this.errorDtoList = errorDtoList;
    }
}
