package pl.sii.shopsystem.purchase.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;
import pl.sii.shopsystem.purchase.service.PurchaseService;

@RestController
@Tag(name = "Purchase")
@RequestMapping("/api/v1/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    ResponseEntity<PurchaseOutputDto> makePurchase(@RequestBody PurchaseInputDto purchaseInputDto) {
        return ResponseEntity.ok(purchaseService.makePurchase(purchaseInputDto));
    }
}