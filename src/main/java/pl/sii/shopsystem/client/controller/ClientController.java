package pl.sii.shopsystem.client.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.shopsystem.client.dto.*;
import pl.sii.shopsystem.client.service.ClientService;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;

@RestController
@Tag(name = "Client")
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Add new client")
    ResponseEntity<ClientOutputDto> addClient(@RequestBody ClientInputDto clientInputDto) {
        return ResponseEntity.ok(clientService.addClient(clientInputDto));
    }

    @GetMapping
    ResponseEntity<ClientOutputDto> getClient(@RequestBody ClientEmailInputDto clientEmailInputDto) {
        return ResponseEntity.ok(clientService.getClient(clientEmailInputDto));
    }

    @DeleteMapping
    void removeClient(@RequestBody ClientEmailInputDto clientEmailInputDto) {
        clientService.removeClient(clientEmailInputDto);
    }

    @PutMapping
    ResponseEntity<ClientOutputDto> updateClient(@RequestBody ClientInputDto clientInputDto) {
        return ResponseEntity.ok(clientService.updateClient(clientInputDto));
    }

    @PostMapping("/purchase")
    ResponseEntity<PurchaseOutputDto> makePurchase(@RequestBody PurchaseInputDto purchaseInputDto) {
        return ResponseEntity.ok(clientService.makePurchase(purchaseInputDto));
    }
}
