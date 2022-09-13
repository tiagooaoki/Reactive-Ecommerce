package br.bb.letscode.dados_pessoais;

import java.time.LocalDate;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;


@Entity
@Cacheable
@Table(name = "dados_pessoais")
public class DadosPessoais extends PanacheEntity{

    @Size(min = 2, max = 255, message = "Tamanho de nome deve ter entre {min} e {max} caracteres")
    public String nome;
    @NotNull
    @Past
    public LocalDate dataNascimento;
    @Column(unique = true)
    @CPF
    public String cpf;


    public DadosPessoais() {
    }

    public DadosPessoais(String nome, LocalDate dataNascimento, String cpf) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
    }


}
