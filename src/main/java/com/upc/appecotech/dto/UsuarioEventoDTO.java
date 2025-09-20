package com.upc.appecotech.dto;

import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioEventoDTO {

    private Long id;
    private Usuario idusuario;
    private Evento idevento;
    private LocalDate fechainscripcion;
    private Boolean asistio = false;
    private Integer puntosotorgados;

}
