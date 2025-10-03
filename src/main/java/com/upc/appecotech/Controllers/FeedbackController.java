package com.upc.appecotech.controllers;


import com.upc.appecotech.dto.FeedbackDTO;
import com.upc.appecotech.entidades.Feedback;
import com.upc.appecotech.interfaces.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedbackController {
    @Autowired
    private IFeedbackService feedbackService;

    @PostMapping("/feedbacks")
    public ResponseEntity<?> crearFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        try {
            FeedbackDTO nuevoFeedback = feedbackService.crearFeedback(feedbackDTO);
            return ResponseEntity.ok(nuevoFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/feedbacks/{idFeedback}")
    public FeedbackDTO actualizarFeedback(@PathVariable Long idFeedback, @RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.actualizarFeedback(idFeedback, feedbackDTO);
    }

    @DeleteMapping("/feedbacks/{idFeedback}")
    public ResponseEntity<?> eliminarFeedback(@PathVariable Long idFeedback) {
        try {
            feedbackService.eliminarFeedback(idFeedback);
            return ResponseEntity.ok("Feedback eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuarios/{idUsuario}/eventos/{idEvento}/validar-asistencia")
    public ResponseEntity<Boolean> validarAsistencia(@PathVariable Long idUsuario, @PathVariable Long idEvento) {
        boolean asistio = feedbackService.validarAsistenciaEvento(idUsuario, idEvento);
        return ResponseEntity.ok(asistio);
    }

}
