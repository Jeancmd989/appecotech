package com.upc.appecotech.dtos;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor


public class SuscripcionDTO {
    private Long id;
    private Long idusuario;
    private Long idmetodopago;
    private String tipoplan;
    private LocalDate fechainicio;
    private LocalDate fechafin;
    private String descripcion;
    private String estado;
}
