package pl.sii.shopsystem.client.service.impl;

import org.springframework.stereotype.Service;
import pl.sii.shopsystem.client.dto.*;
import pl.sii.shopsystem.client.exception.ClientException;
import pl.sii.shopsystem.client.persistence.*;
import pl.sii.shopsystem.client.repository.ClientRepository;
import pl.sii.shopsystem.client.service.ClientService;
import pl.sii.shopsystem.client.service.ClientValidator;

import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.*;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientValidator validator;
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientValidator validator,
                             ClientRepository clientRepository) {
        this.validator = validator;
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientOutputDto addClient(ClientInputDto clientInputDto) {
        validator.validateClientInputDto(clientInputDto);
        validator.validateClientExistence(clientInputDto.email());

        Client client = clientRepository.save(
                Client.builder()
                        .firstname(clientInputDto.firstname())
                        .lastname(clientInputDto.lastname())
                        .email(clientInputDto.email())
                        .build());

        return ClientOutputDto.builder()
                .id(client.getId())
                .firstname(client.getFirstname())
                .lastname(client.getLastname())
                .email(client.getEmail())
                .build();
    }

    @Override
    public ClientOutputDto getClient(ClientEmailInputDto clientEmailInputDto) {
        validator.validateClientEmailInputDto(clientEmailInputDto);

        Client client = getClientByEmail(clientEmailInputDto.email());

        return ClientOutputDto.builder()
                .id(client.getId())
                .firstname(client.getFirstname())
                .lastname(client.getLastname())
                .email(client.getEmail())
                .build();
    }

    @Override
    public void removeClient(ClientEmailInputDto clientEmailInputDto) {
        validator.validateClientEmailInputDto(clientEmailInputDto);

        Client client = getClientByEmail(clientEmailInputDto.email());
        clientRepository.deleteById(client.getId());
    }

    @Override
    public ClientOutputDto updateClient(ClientInputDto clientInputDto) {
        validator.validateClientInputDto(clientInputDto);

        Client client = getClientByEmail(clientInputDto.email());

        validator.validateEmailChange(client.getEmail(), clientInputDto.email());

        client.setFirstname(client.getFirstname());
        client.setLastname(client.getLastname());
        client.setEmail(client.getEmail());

        clientRepository.save(client);

        return ClientOutputDto.builder()
                .id(client.getId())
                .firstname(client.getFirstname())
                .lastname(client.getLastname())
                .email(client.getEmail())
                .build();
    }

    private Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientException(NO_CLIENT_FOUND.getMessage()));
    }
}
