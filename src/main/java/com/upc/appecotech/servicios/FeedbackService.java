package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.FeedbackDTO;
import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Feedback;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IFeedbackService;
import com.upc.appecotech.repositorios.EventoRepositorio;
import com.upc.appecotech.repositorios.FeedbackRepositorio;
import com.upc.appecotech.repositorios.UsuarioEventoRepositorio;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    @Autowired
    private FeedbackRepositorio feedbackRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private EventoRepositorio eventoRepositorio;
    @Autowired
    private UsuarioEventoRepositorio usuarioEventoRepositorio;

    @Override
    public FeedbackDTO crearFeedback(FeedbackDTO feedbackDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(feedbackDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + feedbackDTO.getIdUsuario()));

            Evento evento = eventoRepositorio.findById(feedbackDTO.getIdEvento())
                    .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + feedbackDTO.getIdEvento()));

            // Validar que el usuario asistió al evento
            if (!validarAsistenciaEvento(usuario.getId(), evento.getId())) {
                throw new RuntimeException("El usuario no asistió a este evento");
            }

            // Validar que no tenga feedback previo para este evento
            boolean yaExisteFeedback = feedbackRepositorio.existsByUsuarioIdAndEventoId(usuario.getId(), evento.getId());
            if (yaExisteFeedback) {
                throw new RuntimeException("El usuario ya dejó feedback para este evento");
            }

            // Validar puntuación
            if (feedbackDTO.getPuntuacion() < 1 || feedbackDTO.getPuntuacion() > 5) {
                throw new RuntimeException("La puntuación debe estar entre 1 y 5");
            }

            // Validar comentario no vacío
            if (feedbackDTO.getComentario() == null || feedbackDTO.getComentario().trim().isEmpty()) {
                throw new RuntimeException("El comentario no puede estar vacío");
            }

            // Crear feedback
            Feedback feedback = new Feedback();
            feedback.setIdusuario(usuario);
            feedback.setIdevento(evento);
            feedback.setComentario(feedbackDTO.getComentario());
            feedback.setPuntuacion(feedbackDTO.getPuntuacion());
            feedback.setFecha(LocalDate.now());

            Feedback guardado = feedbackRepositorio.save(feedback);

            // Mapeo manual para la respuesta
            FeedbackDTO response = new FeedbackDTO();
            response.setIdFeedback(guardado.getId());
            response.setIdUsuario(guardado.getIdusuario().getId());
            response.setIdEvento(guardado.getIdevento().getId());
            response.setComentario(guardado.getComentario());
            response.setPuntuacion(guardado.getPuntuacion());
            response.setFecha(guardado.getFecha());

            return response;

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al crear feedback: " + e.getMessage());
        }
    }

    @Override
    public boolean validarAsistenciaEvento(Long idUsuario, Long idEvento) {
        List<Usuarioevento> inscripciones = usuarioEventoRepositorio.findByUsuarioIdAndEventoId(idUsuario, idEvento);

        return inscripciones.stream()
                .anyMatch(ue -> ue.getAsistio() != null && ue.getAsistio());
    }

    @Override
    public FeedbackDTO actualizarFeedback(Long idFeedback, FeedbackDTO feedbackDTO) {
        Feedback feedbackExistente = feedbackRepositorio.findById(idFeedback)
                .orElseThrow(() -> new EntityNotFoundException("Feedback no encontrado con ID: " + idFeedback));

        // Validar puntuación si se actualiza
        if (feedbackDTO.getPuntuacion() != null) {
            if (feedbackDTO.getPuntuacion() < 1 || feedbackDTO.getPuntuacion() > 5) {
                throw new RuntimeException("La puntuación debe estar entre 1 y 5");
            }
            feedbackExistente.setPuntuacion(feedbackDTO.getPuntuacion());
        }

        // Validar comentario si se actualiza
        if (feedbackDTO.getComentario() != null && !feedbackDTO.getComentario().trim().isEmpty()) {
            feedbackExistente.setComentario(feedbackDTO.getComentario());
        }

        Feedback actualizado = feedbackRepositorio.save(feedbackExistente);

        // Mapeo manual para la respuesta
        FeedbackDTO response = new FeedbackDTO();
        response.setIdFeedback(actualizado.getId());
        response.setIdUsuario(actualizado.getIdusuario().getId());
        response.setIdEvento(actualizado.getIdevento().getId());
        response.setComentario(actualizado.getComentario());
        response.setPuntuacion(actualizado.getPuntuacion());
        response.setFecha(actualizado.getFecha());

        return response;
    }

    @Override
    public void eliminarFeedback(Long idFeedback) {
        if (!feedbackRepositorio.existsById(idFeedback)) {
            throw new EntityNotFoundException("Feedback no encontrado con ID: " + idFeedback);
        }
        feedbackRepositorio.deleteById(idFeedback);
    }

    @Override
    public FeedbackDTO buscarPorId(Long id) {
        return null;
    }

    @Override
    public List<FeedbackDTO> listarTodos() {
        return List.of();
    }

    @Override
    public List<FeedbackDTO> listarFeedbacksPorEvento(Long idEvento) {
        return List.of();
    }

    @Override
    public List<FeedbackDTO> listarFeedbacksPorUsuario(Long idUsuario) {
        return List.of();
    }

    @Override
    public Double calcularPromedioEvento(Long idEvento) {
        return 0.0;
    }
}
