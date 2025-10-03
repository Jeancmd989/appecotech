package com.upc.appecotech.Services;

import com.upc.appecotech.dto.HistorialPuntosDTO;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.interfaces.IContactoService;
import com.upc.appecotech.interfaces.IHistorialdepuntoService;
import com.upc.appecotech.repositorios.HistorialPuntosRepository;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistorialPuntosService implements IHistorialdepuntoService {
    @Autowired
    private HistorialPuntosRepository historialPuntosRepository;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public HistorialPuntosDTO registrarMovimiento(HistorialPuntosDTO historialDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(historialDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            Historialdepunto historial = new Historialdepunto();
            historial.setIdusuario(usuario);
            historial.setPuntosobtenidos(historialDTO.getPuntosObtenidos());
            historial.setPuntoscanjeados(historialDTO.getPuntosCanjeados());
            historial.setTipomovimiento(historialDTO.getTipoMovimiento());
            historial.setDescripcion(historialDTO.getDescripcion());
            historial.setFecha(LocalDate.now());

            Historialdepunto guardado = historialPuntosRepository.save(historial);

            // Mapeo manual
            HistorialPuntosDTO response = new HistorialPuntosDTO();
            response.setId(guardado.getId());
            response.setIdUsuario(guardado.getIdusuario().getId());
            response.setPuntosObtenidos(guardado.getPuntosobtenidos());
            response.setPuntosCanjeados(guardado.getPuntoscanjeados());
            response.setTipoMovimiento(guardado.getTipomovimiento());
            response.setDescripcion(guardado.getDescripcion());
            response.setFecha(guardado.getFecha());

            return response;

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al registrar movimiento: " + e.getMessage());
        }
    }

    @Override
    public List<HistorialPuntosDTO> obtenerHistorialUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioId(idUsuario);

        return historial.stream()
                .map(h -> {
                    HistorialPuntosDTO dto = new HistorialPuntosDTO();
                    dto.setId(h.getId());
                    dto.setIdUsuario(h.getIdusuario().getId());
                    dto.setPuntosObtenidos(h.getPuntosobtenidos());
                    dto.setPuntosCanjeados(h.getPuntoscanjeados());
                    dto.setTipoMovimiento(h.getTipomovimiento());
                    dto.setDescripcion(h.getDescripcion());
                    dto.setFecha(h.getFecha());
                    return dto;
                })
                .toList();
    }

    @Override
    public List<HistorialPuntosDTO> obtenerHistorialPorTipo(Long idUsuario, String tipoMovimiento) {
        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioIdAndTipo(idUsuario, tipoMovimiento);

        return historial.stream()
                .map(h -> {
                    HistorialPuntosDTO dto = new HistorialPuntosDTO();
                    dto.setId(h.getId());
                    dto.setIdUsuario(h.getIdusuario().getId());
                    dto.setPuntosObtenidos(h.getPuntosobtenidos());
                    dto.setPuntosCanjeados(h.getPuntoscanjeados());
                    dto.setTipoMovimiento(h.getTipomovimiento());
                    dto.setDescripcion(h.getDescripcion());
                    dto.setFecha(h.getFecha());
                    return dto;
                })
                .toList();
    }

    @Override
    public Integer calcularPuntosDisponibles(Long idUsuario) {
        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioId(idUsuario);

        int puntosObtenidos = historial.stream()
                .mapToInt(Historialdepunto::getPuntosobtenidos)
                .sum();

        int puntosCanjeados = historial.stream()
                .mapToInt(Historialdepunto::getPuntoscanjeados)
                .sum();

        return puntosObtenidos - puntosCanjeados;
    }

    @Override
    public List<HistorialPuntosDTO> obtenerHistorialPorFechas(Long idUsuario, LocalDate inicio, LocalDate fin) {
        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioIdAndFechaBetween(idUsuario, inicio, fin);

        return historial.stream()
                .map(h -> {
                    HistorialPuntosDTO dto = new HistorialPuntosDTO();
                    dto.setId(h.getId());
                    dto.setIdUsuario(h.getIdusuario().getId());
                    dto.setPuntosObtenidos(h.getPuntosobtenidos());
                    dto.setPuntosCanjeados(h.getPuntoscanjeados());
                    dto.setTipoMovimiento(h.getTipomovimiento());
                    dto.setDescripcion(h.getDescripcion());
                    dto.setFecha(h.getFecha());
                    return dto;
                })
                .toList();
    }
}
