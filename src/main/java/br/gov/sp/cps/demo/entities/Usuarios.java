package br.gov.sp.cps.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity(name  = "UserTable")
public class Usuarios {
    @Id
    @GeneratedValue()
    private Long primaryKey;
    private String name;
    private String password;
    private String email;
    private String cpf;
    private LocalDate birthDate;

}
