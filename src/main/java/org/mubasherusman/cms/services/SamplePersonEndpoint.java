package org.mubasherusman.cms.services;

import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.exception.EndpointException;
import jakarta.annotation.security.PermitAll;
import java.util.Optional;
import org.mubasherusman.cms.data.SamplePerson;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Endpoint
@PermitAll
public class SamplePersonEndpoint {

    private final SamplePersonService service;

    public SamplePersonEndpoint(SamplePersonService service) {
        this.service = service;
    }

    public Page<SamplePerson> list(Pageable page) {
        return service.list(page);
    }

    public Optional<SamplePerson> get(Long id) {
        return service.get(id);
    }

    public SamplePerson update(SamplePerson entity) {
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
