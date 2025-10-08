package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.EventoDTO;
import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.interfaces.IEventoService;
import com.upc.appecotech.repositorios.EventoRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoService implements IEventoService {
    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private ModelMapper  modelMapper;


    @Override
    public EventoDTO crearEvento(EventoDTO eventoDTO) {
        if (eventoDTO.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha del evento no puede ser anterior a hoy");
        }

        Evento evento = modelMapper.map(eventoDTO, Evento.class);
        Evento guardado = eventoRepositorio.save(evento);

        return modelMapper.map(guardado, EventoDTO.class);
    }

    @Override
    public EventoDTO actualizarEvento(Long id, EventoDTO eventoDTO) {
        return eventoRepositorio.findById(id)
                .map(eventoExistente -> {
                    if (eventoDTO.getNombre() != null) {
                        eventoExistente.setNombre(eventoDTO.getNombre());
                    }
                    if (eventoDTO.getFecha() != null) {
                        eventoExistente.setFecha(eventoDTO.getFecha());
                    }
                    if (eventoDTO.getLugar() != null) {
                        eventoExistente.setLugar(eventoDTO.getLugar());
                    }
                    if (eventoDTO.getDescripcion() != null) {
                        eventoExistente.setDescripcion(eventoDTO.getDescripcion());
                    }
                    if (eventoDTO.getPuntos() != null) {
                        eventoExistente.setPuntos(eventoDTO.getPuntos());
                    }

                    Evento actualizado = eventoRepositorio.save(eventoExistente);
                    return modelMapper.map(actualizado, EventoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
    }

    @Override
    public EventoDTO buscarPorId(Long id) {
        return eventoRepositorio.findById(id)
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .orElse(null);
    }

    @Override
    public List<EventoDTO> listarTodos() {
        List<Evento> eventos = eventoRepositorio.findAll();
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    public List<EventoDTO> listarEventosProximos() {
        List<Evento> eventos = eventoRepositorio.findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate.now());
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    public List<EventoDTO> listarEventosPasados() {
        List<Evento> eventos = eventoRepositorio.findByFechaLessThanOrderByFechaDesc(LocalDate.now());
        return eventos.stream()
                .map(evento -> modelMapper.map(evento, EventoDTO.class))
                .toList();
    }

    @Override
    public void eliminarEvento(Long id) {
        Evento evento = eventoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));


        eventoRepositorio.deleteById(id);

    }
}
