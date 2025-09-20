package com.upc.appecotech.Services;

import com.upc.appecotech.dto.HistorialPuntosDTO;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.interfaces.IContactoService;
import com.upc.appecotech.interfaces.IHistorialdepuntoService;
import com.upc.appecotech.repositorios.HistorialPuntosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialPuntosService implements IHistorialdepuntoService {
    @Autowired
    private HistorialPuntosRepository historialPuntosRepository;


    @Override
    public List<Historialdepunto> listarHistorialdepuntos() {
        return  historialPuntosRepository.findAll();
    }

    @Override
    public List<HistorialPuntosDTO> listarHistorialdepuntosPorUsuario(Long idUsuario) {
        return List.of();
    }
}
