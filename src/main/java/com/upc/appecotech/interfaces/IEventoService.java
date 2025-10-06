package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.EventoDTO;


import java.util.List;

public interface IEventoService {
    EventoDTO crearEvento(EventoDTO eventoDTO);
    EventoDTO actualizarEvento(Long id, EventoDTO eventoDTO);
    EventoDTO buscarPorId(Long id);
    List<EventoDTO> listarTodos();
    List<EventoDTO> listarEventosProximos();
    List<EventoDTO> listarEventosPasados();
    void eliminarEvento(Long id);
}
