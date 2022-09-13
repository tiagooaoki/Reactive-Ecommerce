package br.bb.letscode.usuario;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.bb.letscode.dados_pessoais.DadosPessoais;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;


@Entity
@Cacheable
@Table(name = "usuario")
public class Usuario extends PanacheEntity{

    @Size(min = 2, max = 255, message = "Tamanho de username deve ter entre {min} e {max} caracteres")
    public String username;
    @Size(min = 3, max = 16, message = "Tamanho de password deve ter entre {min} e {max} caracteres")
    public String password;
    @Email
    @NotNull
    public String email;
    @OneToOne
    public DadosPessoais dadosPessoais;


    public Usuario() {
    }

    public Usuario(String username, String password, String email, DadosPessoais dadosPessoais) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dadosPessoais = dadosPessoais;
    }


}
