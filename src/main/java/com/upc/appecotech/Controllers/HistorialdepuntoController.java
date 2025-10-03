package com.upc.appecotech.controllers;

import com.upc.appecotech.dto.HistorialPuntosDTO;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.interfaces.IHistorialdepuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HistorialdepuntoController {
    @Autowired
    private IHistorialdepuntoService historialPuntosService;

    @GetMapping("/usuarios/{idUsuario}/historial-puntos")
    public List<HistorialPuntosDTO> obtenerHistorial(@PathVariable Long idUsuario) {
        return historialPuntosService.obtenerHistorialUsuario(idUsuario);
    }

    @GetMapping("/usuarios/{idUsuario}/historial-puntos/tipo/{tipo}")
    public List<HistorialPuntosDTO> obtenerHistorialPorTipo(
            @PathVariable Long idUsuario,
            @PathVariable String tipo) {
        return historialPuntosService.obtenerHistorialPorTipo(idUsuario, tipo);
    }

    @GetMapping("/usuarios/{idUsuario}/puntos-disponibles")
    public ResponseEntity<Integer> obtenerPuntosDisponibles(@PathVariable Long idUsuario) {
        Integer puntos = historialPuntosService.calcularPuntosDisponibles(idUsuario);
        return ResponseEntity.ok(puntos);
    }

    @GetMapping("/usuarios/{idUsuario}/historial-puntos/rango")
    public List<HistorialPuntosDTO> obtenerHistorialPorFechas(
            @PathVariable Long idUsuario,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {
        return historialPuntosService.obtenerHistorialPorFechas(idUsuario, inicio, fin);
    }

}
