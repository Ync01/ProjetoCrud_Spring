package br.gov.sp.cps.demo.model;

import jakarta.persistence.Entity;

import java.time.LocalDate;

public class user {

    private String name;
    private String password;
    private String email;
    private String cpf;
    private LocalDate birthDate;

}
