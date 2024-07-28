package org.mubasherusman.cms.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import java.util.Optional;
import org.mubasherusman.cms.data.User;
import org.mubasherusman.cms.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
@AnonymousAllowed
public class UserEndpoint {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    public Optional<User> getAuthenticatedUser() {
        return authenticatedUser.get();
    }
}
