package br.bb.letscode.produto_pedido;

import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import br.bb.letscode.pedido.Pedido;
import br.bb.letscode.produto.Produto;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Multi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
@Table(name = "produto_pedido",indexes = {
        @Index(name = "produto_id_index", columnList = "produto_id"),
        @Index(name = "pedido_id_index", columnList = "pedido_id"),
},
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"produto_id", "pedido_id"})
        })
@NamedQueries(value = { 
    @NamedQuery (name = "ProdutoPedido.getByPedidoId",
        query = "SELECT c FROM ProdutoPedido c JOIN FETCH c.produto where c.pedido.id = ?1"),
    @NamedQuery(name = "ProdutoPedido.getByProdutoId",
        query = "SELECT c FROM ProdutoPedido c JOIN FETCH c.pedido where c.produto.id = ?1")})
@Getter
public class ProdutoPedido extends PanacheEntityBase {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    
    @Column(name = "valor_total", precision = 21, scale = 2)
    public double valorTotal;

    public Integer quantidadeProduto;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Produto produto;
  
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    public Pedido pedido;
    
    // public static Multi<ProdutoPedido> getPedidoByProdutoQuery(Long pedidoid) {
    //     return stream("#ProdutoPedido.getByPedidoId", pedidoid);
    // }

    // public static Multi<ProdutoPedido> getProdutosByPedidoQuery(Long produtoid) {
    //     return stream("#ProdutoPedido.getByProdutoId", produtoid);
    // }

    public static Multi<ProdutoPedido> findByPedidoIdByProdutoId(Long pedidoid, Long produtoid) {
        return stream("pedido.id = ?1 and produto.id = ?2", pedidoid, produtoid);
    }

    public double calculaValorTotal(){
        double precoProduto = produto.preco;
        if(precoProduto == 0 || quantidadeProduto == 0){
            return 0;
        }
        return valorTotal = precoProduto*quantidadeProduto;

    }

    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }
}
