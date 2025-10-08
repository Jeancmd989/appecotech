package com.upc.appecotech.entidades;

import com.upc.appecotech.security.entidades.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "suscripcion")
public class Suscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsuscripcion", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idmetodopago", nullable = false)
    private Metodopago idmetodopago;

    @NotNull
    @Column(name = "tipoplan", nullable = false, length = Integer.MAX_VALUE)
    private String tipoplan;

    @NotNull
    @Column(name = "fechainicio", nullable = false)
    private LocalDate fechainicio;

    @NotNull
    @Column(name = "fechafin", nullable = false)
    private LocalDate fechafin;

    @NotNull
    @Column(name = "descripcion", nullable = false, length = Integer.MAX_VALUE)
    private String descripcion;

    @NotNull
    @Column(name = "estado", nullable = false, length = Integer.MAX_VALUE)
    private String estado;

}