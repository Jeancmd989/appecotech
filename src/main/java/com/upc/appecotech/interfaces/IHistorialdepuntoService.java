package com.upc.appecotech.interfaces;

import com.upc.appecotech.dto.HistorialPuntosDTO;
import com.upc.appecotech.entidades.Historialdepunto;

import java.util.List;

public interface IHistorialdepuntoService {
    public List<Historialdepunto> listarHistorialdepuntos();
    public List<HistorialPuntosDTO> listarHistorialdepuntosPorUsuario(Long idUsuario);
}
