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

public class CanjeUsuarioDTO {
    private Long id;
    private Long idUsuario;
    private Long idProducto;
    private LocalDate fechacanje;
    private Integer cantidad;
}
