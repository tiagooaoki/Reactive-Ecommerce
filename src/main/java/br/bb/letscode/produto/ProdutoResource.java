package br.bb.letscode.produto;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.CompositeException;
import io.smallrye.mutiny.Uni;

@Path("/produto")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class ProdutoResource {
   
    private static final Logger LOGGER = Logger.getLogger(ProdutoResource.class.getName());

    public Uni<Produto> buscaProdutoValidoUni(Long id) {
        Uni<Produto> produto = Produto.findById(id);
        if (produto == null) {
            throw new WebApplicationException("Produto com o id " + id + " não existe", 404);
        }
        return produto;
    }

    @GET
    @Operation(summary = "Lista Produtos ",
            description = "Retorna uma lista de produtos. Permissão de uso para Admin")
    @RolesAllowed("admin")
    public Uni<List<Produto>> mostrarListaProduto(){
        return Produto.listAll(Sort.by("id"));
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Lista Produto por id ",
    description = "Retorna produto pelo id. Permissão de uso para User")
    @RolesAllowed("user")
    public Uni<Produto> buscaProduto(Long id) {
        return buscaProdutoValidoUni(id);
    }

    @POST
    @Operation(summary = "Cria Produto",
    description = "Cria Produto. Permissão de uso para User")
    @RolesAllowed("user")
    public Uni<Response> create(@Valid Produto produto) {
        if (produto == null || produto.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return Panache.withTransaction(produto::persist)
                    .replaceWith(Response.ok(produto).status(Status.CREATED)::build);
    }

    @DELETE
    @Operation(summary = "Deleta Produto",
    description = "Deleta Produto pelo id. Permissão de uso para Admin")
    @RolesAllowed("admin")
    @Path("{id}")
    public Uni<Response> delete(Long id) {
        return Panache.withTransaction(() -> Produto.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(Status.NO_CONTENT).build()
                        : Response.ok().status(Status.NOT_FOUND).build());
    }

    @PUT
    @Operation(summary = "Modifica Produto",
    description = "Modifica Produto pelo id. Permissão de uso para Admin")
    @RolesAllowed("admin")
    @Path("{id}")
    public Uni<Response> update(Long id,@Valid Produto produto) {

        return Panache
                .withTransaction(() -> Produto.<Produto>findById(id)
                        .onItem().ifNotNull().invoke( entity -> {
                            entity.nome = produto.nome;
                            entity.descricao = produto.descricao;
                            entity.tipo = produto.tipo;
                            entity.preco = produto.preco;
                        }))
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).status(200).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    /**
     * Create a HTTP response from an exception.
     *
     * Response Example:
     *
     * <pre>
     * HTTP/1.1 422 Unprocessable Entity
     * Content-Length: 111
     * Content-Type: application/json
     *
     * {
     *     "code": 422,
     *     "error": "Fruit name was not set on request.",
     *     "exceptionType": "javax.ws.rs.WebApplicationException"
     * }
     * </pre>
     */
    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            Throwable throwable = exception;

            int code = 500;
            if (throwable instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            // This is a Mutiny exception and it happens, for example, when we try to insert
            // a new
            // fruit but the name is already in the database
            if (throwable instanceof CompositeException) {
                throwable = ((CompositeException) throwable).getCause();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", throwable.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", throwable.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
