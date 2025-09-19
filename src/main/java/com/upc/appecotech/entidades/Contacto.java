package com.upc.appecotech.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "contacto")
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcontacto", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "descripcionproblema", nullable = false, length = Integer.MAX_VALUE)
    private String descripcionproblema;

    @NotNull
    @Column(name = "tiporeclamo", nullable = false, length = Integer.MAX_VALUE)
    private String tiporeclamo;

    @NotNull
    @Column(name = "estado", nullable = false, length = Integer.MAX_VALUE)
    private String estado;

}