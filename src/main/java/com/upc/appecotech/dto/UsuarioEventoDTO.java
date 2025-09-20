package com.upc.appecotech.dto;

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
    private Long idUsuarioEvento;
    private Long idEvento;
    private Long idUsuario;
    private LocalDate fechaIncripcion;
    private boolean asistio;
    private Integer puntosOtorgados;

}
