package br.gov.sp.cps.demo.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

// Os nomes das colunas (name, password, birth_date, primary_key) foram mantidos
// para continuar usando os dados que já existem no banco H2.
@Entity(name = "UserTable")
public class Usuario {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key")
    private Long id;

    @Column(name = "name", nullable = false)
    private String nome;

    // Guarda somente o hash BCrypt, nunca a senha digitada
    @Column(name = "password", nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String cpf;

    @Column(name = "birth_date")
    private LocalDate dataNascimento;

    // Perfil do usuário: ADMIN ou USER. Linhas antigas recebem USER pelo DEFAULT da coluna.
    @Column(nullable = false, length = 10)
    @ColumnDefault("'USER'")
    private String tipo;

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
