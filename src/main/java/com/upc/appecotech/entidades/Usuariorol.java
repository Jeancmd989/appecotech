package com.upc.appecotech.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuariorol")
public class Usuariorol {
    @EmbeddedId
    private UsuariorolId id;

    @MapsId("idusuario")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @MapsId("idrol")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idrol", nullable = false)
    private Rol idrol;

}