package com.upc.appecotech.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false, length = Integer.MAX_VALUE)
    private String nombre;

    @NotNull
    @Column(name = "apellidos", nullable = false, length = Integer.MAX_VALUE)
    private String apellidos;

    @NotNull
    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @NotNull
    @Column(name = "telefono", nullable = false, length = Integer.MAX_VALUE)
    private String telefono;

    @NotNull
    @Column(name = "direccion", nullable = false, length = Integer.MAX_VALUE)
    private String direccion;

    @NotNull
    @Column(name = "\"contraseña\"", nullable = false, length = Integer.MAX_VALUE)
    private String contraseña;

}