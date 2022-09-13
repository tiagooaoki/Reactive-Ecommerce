package br.bb.letscode.pedido;

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
import javax.ws.rs.PathParam;
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


@Path("/pedido")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class PedidoResource {

    private static final Logger LOGGER = Logger.getLogger(PedidoResource.class.getName());

    public Uni<Pedido> buscaPedidoValidoUni(Long id) {
        Uni<Pedido> pedido = Pedido.findById(id);
        if (pedido == null) {
            throw new WebApplicationException("Pedido com o id " + id + " não existe", 404);
        }
        return pedido;
    }

    @GET
    @Operation(summary = "Lista Pedidos ",
            description = "Retorna uma lista de pedidos. Permissão de uso para Admin")
    @RolesAllowed("admin")
    public Uni<List<Pedido>> mostrarListaPedido(){
        return Pedido.listAll(Sort.by("id"));
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Lista Pedido por id ",
    description = "Retorna pedido pelo id. Permissão de uso para User")
    @RolesAllowed("user")
    public Uni<Pedido> buscaPedido(Long id) {
        return buscaPedidoValidoUni(id);
    }

    @POST
    @Operation(summary = "Cria pedido",
    description = "Cria pedido. Permissão de uso para User")
    @RolesAllowed("user")
    public Uni<Response> create(@Valid Pedido pedido) {
        if (pedido == null || pedido.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return Panache.withTransaction(pedido::persist)
                    .replaceWith(Response.ok(pedido).status(Status.CREATED)::build);
    }

    @DELETE
    @Operation(summary = "Deleta pedido",
    description = "Deleta pedido pelo id. Permissão de uso para Admin")
    @RolesAllowed("admin")
    @Path("{id}")
    public Uni<Response> delete(Long id) {
        return Panache.withTransaction(() -> Pedido.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(Status.NO_CONTENT).build()
                        : Response.ok().status(Status.NOT_FOUND).build());
    }

    @PUT
    @Operation(summary = "Modifica pedido",
    description = "Modifica pedido pelo id. Permissão de uso para Admin")
    @RolesAllowed("admin")
    @Path("{id}")
    public Uni<Response> update(Long id,@Valid Pedido pedido) {

        return Panache
                .withTransaction(() -> Pedido.<Pedido>findById(id)
                        .onItem().ifNotNull().invoke( entity -> entity.data = pedido.data).invoke(entity -> entity.statusPedido = pedido.statusPedido).invoke(entity -> entity.valorPedidoTotal = pedido.valorPedidoTotal)
                        .invoke(entity -> entity.usuario = pedido.usuario))
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).status(200).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }


    
    @PUT
    @Operation(summary = "Adiciona produto ao pedido",
    description = "Adiciona produto (id) ao pedido pelo id. Permissão de uso para User")
    @RolesAllowed("user")
    @Path("{pedidoId}/add/{produtoId}")
    public Uni<Response> addProduct(@PathParam("pedidoId") Long pedidoId, @PathParam("produtoId") Long produtoId) {

        return Pedido.addProdutoToPedido(pedidoId, produtoId)
            .onItem().ifNotNull().transform(entity -> Response.ok(entity).status(200).build())
            .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @PUT
    @Operation(summary = "Remove produto do pedido",
    description = "Remove produto (id) do pedido pelo id. Permissão de uso para User")
    @RolesAllowed("user")
    @Path("{pedidoId}/remove/{produtoId}")
    public Uni<Response> removeProduct(@PathParam("pedidoId") Long pedidoId, @PathParam("produtoId") Long produtoId) {

        return Pedido.removeProdutoToPedido(pedidoId, produtoId)
            .onItem().ifNotNull().transform(entity -> Response.ok(entity).status(200).build())
            .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    // @PUT
    // @Path("{pedidoId}/remove/{produtoId}")
    // public Uni<Response> removeProduct(@PathParam("pedidoId") Long pedidoId, @PathParam("produtoId") Long produtoId) {
    //     Uni<Pedido> pedidoUni = buscaPedido(pedidoId);
    //     Uni<List<ProdutoPedido>> listaProduto = pedidoUni
    //     .chain(pedido -> Mutiny.fetch(pedido.produtos)).onFailure().recoverWithNull();
    //     Uni<Produto> produto = Produto.findById(produtoId);
    //     Uni<Tuple3<List<ProdutoPedido>, Pedido, Produto>> produtoPedidoUni = Uni.combine().all().unis(listaProduto,pedidoUni,produto).asTuple();

        
    //     return Panache
    //             .withTransaction(() -> produtoPedidoUni.onItem().ifNotNull().transform(entity ->{
    //                 if(entity.getItem3() == null || entity.getItem2() == null || entity.getItem1() == null){
    //                     return Response.ok().status(Status.NOT_FOUND).build();
    //                 }
    //                 entity.getItem1().remove(entity.getItem3());
    //                 return Response.ok(entity).status(200).build();
    //             }));
    // }


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
