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

public class FeedbackDTO {
    private Long idFeedback;
    private Long idEvento;
    private Long idUsuario;
    private String comentario;
    private LocalDate fecha;
    private Integer puntuacion;
}
