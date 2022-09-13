package br.bb.letscode.produto;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
@Table(name = "produto")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Produto extends PanacheEntity implements Serializable {
  private static final long serialVersionUID = 1081869386060246794L;

  @Size(min = 2, max = 255, message = "Tamanho de nome deve ter entre {min} e {max} caracteres")
  public String nome;
  @Size(min = 2, max = 40, message = "Tamanho de tipo deve ter entre {min} e {max} caracteres")
  public String tipo;
  @Size(min = 2, max = 255, message = "Tamanho de tipo deve ter entre {min} e {max} caracteres")
  public String descricao;
  @Positive
  public double preco;


}
