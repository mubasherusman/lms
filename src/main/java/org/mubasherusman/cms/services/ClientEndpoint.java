package org.mubasherusman.cms.services;

import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.exception.EndpointException;
import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;
import org.mubasherusman.cms.data.Client;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Endpoint
@RolesAllowed("ADMIN")
public class ClientEndpoint {

    private final ClientService service;

    public ClientEndpoint(ClientService service) {
        this.service = service;
    }

    public Page<Client> list(Pageable page) {
        return service.list(page);
    }

    public Optional<Client> get(Long id) {
        return service.get(id);
    }

    public Client update(Client entity) {
        try {
            return service.update(entity);
        } catch (OptimisticLockingFailureException e) {
            throw new EndpointException("Somebody else has updated the data while you were making changes.");
        }
    }

    public void delete(Long id) {
        service.delete(id);
    }

    public int count() {
        return service.count();
    }

}
