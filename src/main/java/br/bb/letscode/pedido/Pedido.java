package br.bb.letscode.pedido;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.reactive.mutiny.Mutiny;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.bb.letscode.produto.Produto;
import br.bb.letscode.produto_pedido.ProdutoPedido;
import br.bb.letscode.usuario.Usuario;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
@Table(name = "pedido")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NamedQueries(value = {
		@NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p LEFT JOIN FETCH p.produtos item LEFT JOIN FETCH item.produto"),
		@NamedQuery(name = "Pedido.getById", query = "SELECT p FROM Pedido p LEFT JOIN FETCH p.produtos item LEFT JOIN FETCH item.produto where p.id = ?1") })
public class Pedido extends PanacheEntity implements Serializable {
	private static final long serialVersionUID = -9109414221418128481L;

	public String statusPedido;

	@PositiveOrZero
	@Column(name = "pedido_total")
	public double valorPedidoTotal;

	public LocalDate data;

	@ManyToOne(fetch = FetchType.LAZY)
	public Usuario usuario;

	@Fetch(FetchMode.JOIN)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "pedido_id")
	public List<ProdutoPedido> produtos;
	//

	// public static Uni<Pedido> findByPedidoId(Long id) {
	// return find("#Pedido.getById", id).firstResult();
	// }

	// public static Uni<List<Pedido>> getAllPedidos() {
	// return find("#Pedido.findAll").list();
	// }

	public void calculaPedidoTotal() {
		valorPedidoTotal = produtos.stream().mapToDouble(ProdutoPedido::getValorTotal).sum();
	}

	public static Multi<Pedido> findAllWithJoinFetch() {
        return stream("SELECT p FROM Pedido p LEFT JOIN FETCH p.produtos");
    }

	public static Uni<Pedido> addProdutoToPedido(Long pedidoId, Long produtoId) {

		Uni<Pedido> pedidoUni = findById(pedidoId);
		Uni<List<ProdutoPedido>> listaProduto = pedidoUni
				.chain(pedido -> Mutiny.fetch(pedido.produtos)).onFailure().recoverWithNull();
		Uni<Produto> produto = Produto.findById(produtoId);
		Uni<ProdutoPedido> item = ProdutoPedido.findByPedidoIdByProdutoId(pedidoId, produtoId).toUni();

		Uni<Tuple4<Pedido, List<ProdutoPedido>, ProdutoPedido, Produto>> produtoPedidoUni = Uni.combine().all()
				.unis(pedidoUni, listaProduto, item, produto).asTuple();
		return Panache
				.withTransaction(() -> produtoPedidoUni
						.onItem().ifNotNull()
						.transform(entity -> {

							if (entity.getItem1() == null || entity.getItem4() == null
									|| entity.getItem2() == null) {
								return null;
							}

							if (entity.getItem3() == null) {
								ProdutoPedido produtoPedido = ProdutoPedido.builder()
										.pedido(entity.getItem1())
										.produto(entity.getItem4())
										.quantidadeProduto(1)
										.build();
										produtoPedido.calculaValorTotal();
								entity.getItem2().add(produtoPedido);
							} else {
								entity.getItem3().quantidadeProduto++;
								entity.getItem3().calculaValorTotal();
							}
							
							entity.getItem1().calculaPedidoTotal();
							return entity.getItem1();
						}));

	}

	public static Uni<Pedido> removeProdutoToPedido(Long pedidoId, Long produtoId) {

		Uni<Pedido> pedidoUni = findById(pedidoId);
		Uni<List<ProdutoPedido>> listaProduto = pedidoUni
				.chain(pedido -> Mutiny.fetch(pedido.produtos)).onFailure().recoverWithNull();
		Uni<Produto> produto = Produto.findById(produtoId);
		Uni<ProdutoPedido> item = ProdutoPedido.findByPedidoIdByProdutoId(pedidoId, produtoId).toUni();

		Uni<Tuple4<Pedido, List<ProdutoPedido>, ProdutoPedido, Produto>> produtoPedidoUni = Uni.combine().all()
				.unis(pedidoUni, listaProduto, item, produto).asTuple();
		return Panache
				.withTransaction(() -> produtoPedidoUni
						.onItem().ifNotNull()
						.transform(entity -> {

							if (entity.getItem1() == null || entity.getItem4() == null
									|| entity.getItem3() == null) {
								return null;
							}
							entity.getItem3().quantidadeProduto--;
							if (entity.getItem3().quantidadeProduto == 0) {
								entity.getItem2().remove(entity.getItem3());
							}
							entity.getItem3().calculaValorTotal();
							entity.getItem1().calculaPedidoTotal();
							return entity.getItem1();
						}));

	}

}
