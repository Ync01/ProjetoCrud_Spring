package br.gov.sp.cps.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity(name  = "UserTable")
public class UsuariosEntities {
    @Id
    @GeneratedValue()
    private Long primaryKey;
    private String name;
    private String password;
    private String email;
    private String cpf;
    private LocalDate birthDate;

    public UsuariosEntities() {
    }

    public UsuariosEntities(Long primaryKey, String name, String password, String email, String cpf, LocalDate birthDate) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.password = password;
        this.email = email;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public Long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
