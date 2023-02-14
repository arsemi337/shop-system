package pl.sii.shopsystem.client.service;

import pl.sii.shopsystem.client.dto.*;

public interface ClientService {
    ClientOutputDto addClient(ClientInputDto clientInputDto);

    ClientOutputDto getClient(ClientEmailInputDto clientEmailInputDto);

    void removeClient(ClientEmailInputDto clientEmailInputDto);

    ClientOutputDto updateClient(ClientInputDto clientInputDto);
}
