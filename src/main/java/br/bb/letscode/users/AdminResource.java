package br.bb.letscode.users;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.security.identity.SecurityIdentity;

@Path("/api/admin")
@RolesAllowed("admin")
public class AdminResource {
    @Inject
    SecurityIdentity keycloakSecurityContext;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String admin() {
    return "admin " + keycloakSecurityContext.getPrincipal().getName();    
        
    }

}
