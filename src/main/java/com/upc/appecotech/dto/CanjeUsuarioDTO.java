package com.upc.appecotech.dto;

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
    private Long idCanjeUsuario;
    private Long idUsuario;
    private Long idProducto;
    private LocalDate fechaCanje;
    private Integer cantidad;
}
