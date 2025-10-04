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


            return modelMapper.map(guardado, UsuarioEventoDTO.class);

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


        return modelMapper.map(actualizado, UsuarioEventoDTO.class);
    }

    @Override
    @Transactional
    public List<UsuarioEventoDTO> listarInscritosPorEvento(Long idEvento) {
        eventoRepositorio.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + idEvento));

        List<Usuarioevento> lista = usuarioEventoRepositorio.findByIdevento_Id(idEvento);

        return lista.stream()
                .map(usuarioevento -> modelMapper.map(usuarioevento, UsuarioEventoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public UsuarioEventoDTO buscarPorId(Long id) {
        return usuarioEventoRepositorio.findById(id)
                .map(usuarioEvento -> modelMapper.map(usuarioEvento, UsuarioEventoDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional
    public List<UsuarioEventoDTO> listarTodos() {
        List<Usuarioevento> lista = usuarioEventoRepositorio.findAll();
        return lista.stream()
                .map(usuarioEvento -> modelMapper.map(usuarioEvento, UsuarioEventoDTO.class))
                .toList();
    }
}
