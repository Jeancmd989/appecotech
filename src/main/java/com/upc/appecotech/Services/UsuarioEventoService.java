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
    public List<Usuarioevento> listarUsuarioeventos() {
        return usuarioEventoRepositorio.findAll();
    }

    @Override
    public UsuarioEventoDTO resgistrarUsuarioEvento(Long idUsuario, Long idEvento) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Evento evento = eventoRepositorio.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        if (usuarioEventoRepositorio.existsByUsuarioIdUsuarioAndEventoIdEvento(idUsuario , idEvento)){
            throw new IllegalArgumentException("El usuario ya esta incrito en este evento");
        }

        Usuarioevento usuarioevento = new Usuarioevento();
        usuarioevento.setIdusuario(usuario);
        usuarioevento.setIdevento(evento);
        usuarioevento.setPuntosotorgados(0);
        usuarioevento.setFechainscripcion(LocalDate.now());
        usuarioevento.setAsistio(null);



        Usuarioevento guardado = usuarioEventoRepositorio.save(usuarioevento);

        return modelMapper.map(guardado, UsuarioEventoDTO.class);
    }


    @Override
    public UsuarioEventoDTO marcarAsistencia(Long idUsuarioEvento, boolean asistio) {
        Usuarioevento usuarioevento = usuarioEventoRepositorio.findById(idUsuarioEvento)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuarioevento.setAsistio(asistio);

        if (asistio){
            int puntos = usuarioevento.getIdevento().getPuntos();
            usuarioevento.setPuntosotorgados(puntos);

            Historialdepunto historialdepunto = new Historialdepunto();
            historialdepunto.setIdusuario(usuarioevento.getIdusuario());
            historialdepunto.setPuntosobtenidos(puntos);
            historialdepunto.setFecha(LocalDate.now());
            historialdepunto.setTipomovimiento("Evento");
            historialdepunto.setDescripcion("Participacion en evento: " + usuarioevento.getIdevento().getNombre());
            historialdepunto.setPuntoscanjeados(0);

            historialPuntosRepository.save(historialdepunto);
        }else{
            usuarioevento.setPuntosotorgados(0);

        }

        Usuarioevento actualizado = usuarioEventoRepositorio.save(usuarioevento);

        return modelMapper.map(actualizado, UsuarioEventoDTO.class);
    }

    @Override
    public List<UsuarioEventoDTO> listarInscritosPorEvento(Long idEvento) {
        Evento evento = eventoRepositorio.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        List<Usuarioevento> lista = usuarioEventoRepositorio.findByIdEvento(evento);

        return lista.stream()
                .map(usuarioevento -> modelMapper.map(usuarioevento, UsuarioEventoDTO.class))
                .toList();
    }
}
