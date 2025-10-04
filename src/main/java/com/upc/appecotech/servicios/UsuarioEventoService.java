package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.CanjeUsuarioDTO;
import com.upc.appecotech.dtos.UsuarioEventoDTO;
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
import jakarta.transaction.Transactional;
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

    // Lo hicimos de forma manual porque con ModelMapper tuvimos problemas
    // en esta tabla con relaciones más complejas, a diferencia de otras
    // entidades


    @Override
    @Transactional
    public UsuarioEventoDTO registrarUsuarioEvento(UsuarioEventoDTO usuarioEventoDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(usuarioEventoDTO.getIdusuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioEventoDTO.getIdusuario()));

            Evento evento = eventoRepositorio.findById(usuarioEventoDTO.getIdevento())
                    .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + usuarioEventoDTO.getIdevento()));

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
    @Transactional
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

        return lista.stream()
                .map(usuarioevento -> {
                    // Si ModelMapper falla aquí, hacemos manual
                    UsuarioEventoDTO usuarioEventoDTO = new UsuarioEventoDTO();
                    usuarioEventoDTO.setId(usuarioevento.getId());
                    usuarioEventoDTO.setIdusuario(usuarioevento.getIdusuario().getId());
                    usuarioEventoDTO.setIdevento(usuarioevento.getIdevento().getId());
                    usuarioEventoDTO.setFechainscripcion(usuarioevento.getFechainscripcion());
                    usuarioEventoDTO.setAsistio(usuarioevento.getAsistio());
                    usuarioEventoDTO.setPuntosotorgados(usuarioevento.getPuntosotorgados());
                    return usuarioEventoDTO;
                })
                .toList();
    }

    @Override
    public UsuarioEventoDTO buscarPorId(Long id) {
        return usuarioEventoRepositorio.findById(id)
                .map(usuarioEvento -> {
                    UsuarioEventoDTO usuarioEventoDTO = new UsuarioEventoDTO();
                    usuarioEventoDTO.setId(usuarioEvento.getId());
                    usuarioEventoDTO.setIdusuario(usuarioEvento.getIdusuario().getId());
                    usuarioEventoDTO.setIdevento(usuarioEvento.getIdevento().getId());
                    usuarioEventoDTO.setFechainscripcion(usuarioEvento.getFechainscripcion());
                    usuarioEventoDTO.setAsistio(usuarioEvento.getAsistio());
                    usuarioEventoDTO.setPuntosotorgados(usuarioEvento.getPuntosotorgados());
                    return usuarioEventoDTO;
                })
                .orElse(null);
    }

    @Override
    public List<UsuarioEventoDTO> listarTodos() {
        List<Usuarioevento> lista = usuarioEventoRepositorio.findAll();
        return lista.stream()
                .map(usuarioEvento -> {
                    UsuarioEventoDTO usuarioEventoDTO = new UsuarioEventoDTO();
                    usuarioEventoDTO.setId(usuarioEvento.getId());
                    usuarioEventoDTO.setIdusuario(usuarioEvento.getIdusuario().getId());
                    usuarioEventoDTO.setIdevento(usuarioEvento.getIdevento().getId());
                    usuarioEventoDTO.setFechainscripcion(usuarioEvento.getFechainscripcion());
                    usuarioEventoDTO.setAsistio(usuarioEvento.getAsistio());
                    usuarioEventoDTO.setPuntosotorgados(usuarioEvento.getPuntosotorgados());
                    return usuarioEventoDTO;
                })
                .toList();
    }
}
