package com.upc.appecotech.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UsuariorolId implements Serializable {
    private static final long serialVersionUID = 4210336979580247357L;
    @NotNull
    @Column(name = "idusuario", nullable = false)
    private Long idusuario;

    @NotNull
    @Column(name = "idrol", nullable = false)
    private Long idrol;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UsuariorolId entity = (UsuariorolId) o;
        return Objects.equals(this.idrol, entity.idrol) &&
                Objects.equals(this.idusuario, entity.idusuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idrol, idusuario);
    }

}