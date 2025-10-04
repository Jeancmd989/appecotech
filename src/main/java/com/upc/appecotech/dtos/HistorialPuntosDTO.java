package com.upc.appecotech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HistorialPuntosDTO {
    private Long id;
    private Long idUsuario;
    private Integer puntosObtenidos;
    private String tipoMovimiento;
    private String descripcion;
    private Integer puntosCanjeados;
    private LocalDate fecha;
}
