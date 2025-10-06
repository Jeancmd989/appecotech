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

public class EventoDTO {
    private Long id;
    private String nombre;
    private LocalDate fecha;
    private String lugar;
    private String descripcion;
    private Integer puntos;
}
