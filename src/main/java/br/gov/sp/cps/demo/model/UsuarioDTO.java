package br.gov.sp.cps.demo.model;

import java.time.LocalDate;

public class UsuarioDTO {

    private String name;
    private String password;
    private String email;
    private String cpf;
    private LocalDate birthDate;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String name, String password, String email, String cpf, LocalDate birthDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.cpf = cpf;
        this.birthDate = birthDate;
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
