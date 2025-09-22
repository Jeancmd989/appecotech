package com.upc.appecotech.Services;

import com.upc.appecotech.dto.UsuarioEventoDTO;
import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IUsuarioEventoService;
import com.upc.appecotech.repositorios.EventoRepositorio;
import com.upc.appecotech.repositorios.HistorialPuntosRepository;
import com.upc.appecotech.repositorios.UsuarioEventoRepositorio;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioEventoService implements IUsuarioEventoService {
    @Autowired
    private UsuarioEventoRepositorio usuarioEventoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private EventoRepositorio eventoRepositorio;
    @Autowired
    private HistorialPuntosRepository historialPuntosRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UsuarioEventoDTO registrarUsuarioEvento(UsuarioEventoDTO usuarioEventoDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(usuarioEventoDTO.getIdusuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioEventoDTO.getIdusuario()));

            Evento evento = eventoRepositorio.findById(usuarioEventoDTO.getIdevento())
                    .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + usuarioEventoDTO.getIdevento()));

            // Verificar si ya está inscrito
            boolean yaInscrito = usuarioEventoRepositorio.existsByIdusuario_IdAndIdevento_Id(
                    usuarioEventoDTO.getIdusuario(), usuarioEventoDTO.getIdevento());

            if (yaInscrito) {
                throw new RuntimeException("El usuario ya está inscrito en este evento");
            }

            Usuarioevento usuarioEvento = new Usuarioevento();
            usuarioEvento.setIdusuario(usuario);
            usuarioEvento.setIdevento(evento);
            usuarioEvento.setFechainscripcion(LocalDate.now());
            usuarioEvento.setAsistio(false);
            usuarioEvento.setPuntosotorgados(0);

            Usuarioevento guardado = usuarioEventoRepositorio.save(usuarioEvento);

            // Mapeo manual para evitar errores de ModelMapper
            UsuarioEventoDTO response = new UsuarioEventoDTO();
            response.setId(guardado.getId());
            response.setIdusuario(guardado.getIdusuario().getId());
            response.setIdevento(guardado.getIdevento().getId());
            response.setFechainscripcion(guardado.getFechainscripcion());
            response.setAsistio(guardado.getAsistio());
            response.setPuntosotorgados(guardado.getPuntosotorgados());

            return response;

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al registrar inscripción: " + e.getMessage());
        }
    }


    @Override
    public UsuarioEventoDTO marcarAsistencia(Long idUsuarioEvento, boolean asistio) {
        Usuarioevento usuarioevento = usuarioEventoRepositorio.findById(idUsuarioEvento)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada con ID: " + idUsuarioEvento));

        usuarioevento.setAsistio(asistio);

        if (asistio) {
            int puntos = usuarioevento.getIdevento().getPuntos();
            usuarioevento.setPuntosotorgados(puntos);

            Historialdepunto historialdepunto = new Historialdepunto();
            historialdepunto.setIdusuario(usuarioevento.getIdusuario());
            historialdepunto.setPuntosobtenidos(puntos);
            historialdepunto.setFecha(LocalDate.now());
            historialdepunto.setTipomovimiento("Evento");
            historialdepunto.setDescripcion("Participación en evento: " + usuarioevento.getIdevento().getNombre());
            historialdepunto.setPuntoscanjeados(0);

            historialPuntosRepository.save(historialdepunto);
        } else {
            usuarioevento.setPuntosotorgados(0);
        }

        Usuarioevento actualizado = usuarioEventoRepositorio.save(usuarioevento);

        // Mapeo manual porque necesitamos control específico - igual que actualizarDeposito()
        UsuarioEventoDTO response = new UsuarioEventoDTO();
        response.setId(actualizado.getId());
        response.setIdusuario(actualizado.getIdusuario().getId());
        response.setIdevento(actualizado.getIdevento().getId());
        response.setFechainscripcion(actualizado.getFechainscripcion());
        response.setAsistio(actualizado.getAsistio());
        response.setPuntosotorgados(actualizado.getPuntosotorgados());

        return response;
    }

    @Override
    public List<UsuarioEventoDTO> listarInscritosPorEvento(Long idEvento) {
        eventoRepositorio.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + idEvento));

        List<Usuarioevento> lista = usuarioEventoRepositorio.findByIdevento_Id(idEvento);

        // Intentamos ModelMapper primero, igual que findAll() en DepositoService
        return lista.stream()
                .map(usuarioevento -> {
                    // Si ModelMapper falla aquí, hacemos manual
                    UsuarioEventoDTO dto = new UsuarioEventoDTO();
                    dto.setId(usuarioevento.getId());
                    dto.setIdusuario(usuarioevento.getIdusuario().getId());
                    dto.setIdevento(usuarioevento.getIdevento().getId());
                    dto.setFechainscripcion(usuarioevento.getFechainscripcion());
                    dto.setAsistio(usuarioevento.getAsistio());
                    dto.setPuntosotorgados(usuarioevento.getPuntosotorgados());
                    return dto;
                })
                .toList();
    }

    @Override
    public UsuarioEventoDTO buscarPorId(Long id) {
        return usuarioEventoRepositorio.findById(id)
                .map(usuarioEvento -> {
                    // Mapeo manual porque tenemos relaciones
                    UsuarioEventoDTO dto = new UsuarioEventoDTO();
                    dto.setId(usuarioEvento.getId());
                    dto.setIdusuario(usuarioEvento.getIdusuario().getId());
                    dto.setIdevento(usuarioEvento.getIdevento().getId());
                    dto.setFechainscripcion(usuarioEvento.getFechainscripcion());
                    dto.setAsistio(usuarioEvento.getAsistio());
                    dto.setPuntosotorgados(usuarioEvento.getPuntosotorgados());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public List<UsuarioEventoDTO> listarTodos() {
        List<Usuarioevento> lista = usuarioEventoRepositorio.findAll();
        // Igual que findAll() en DepositoService, pero manual por las relaciones
        return lista.stream()
                .map(usuarioEvento -> {
                    UsuarioEventoDTO dto = new UsuarioEventoDTO();
                    dto.setId(usuarioEvento.getId());
                    dto.setIdusuario(usuarioEvento.getIdusuario().getId());
                    dto.setIdevento(usuarioEvento.getIdevento().getId());
                    dto.setFechainscripcion(usuarioEvento.getFechainscripcion());
                    dto.setAsistio(usuarioEvento.getAsistio());
                    dto.setPuntosotorgados(usuarioEvento.getPuntosotorgados());
                    return dto;
                })
                .toList();
    }
}
