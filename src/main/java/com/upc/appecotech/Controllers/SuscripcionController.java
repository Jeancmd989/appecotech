package com.upc.appecotech.controllers;

import com.upc.appecotech.dto.SuscripcionDTO;
import com.upc.appecotech.entidades.Suscripcion;
import com.upc.appecotech.interfaces.ISuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SuscripcionController {
    @Autowired
    private ISuscripcionService suscripcionService;

    @PostMapping("/suscribirse")
    public ResponseEntity<?> crearSuscripcion(@RequestBody SuscripcionDTO suscripcionDTO) {
        try {
            SuscripcionDTO nuevaSuscripcion = suscripcionService.crearSuscripcion(suscripcionDTO);
            return ResponseEntity.ok(nuevaSuscripcion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/suscripciones/{idSuscripcion}/estado")
    public SuscripcionDTO actualizarEstado(@PathVariable Long idSuscripcion, @RequestParam String nuevoEstado) {
        return suscripcionService.actualizarEstadoSuscripcion(idSuscripcion, nuevoEstado);
    }

    @PutMapping("/suscripciones/{idSuscripcion}/renovar")
    public SuscripcionDTO renovarSuscripcion(@PathVariable Long idSuscripcion, @RequestParam String nuevoPlan) {
        return suscripcionService.renovarSuscripcion(idSuscripcion, nuevoPlan);
    }

    @GetMapping("/usuarios/{idUsuario}/suscripcion-activa")
    public ResponseEntity<Boolean> validarSuscripcionActiva(@PathVariable Long idUsuario) {
        boolean tieneActiva = suscripcionService.validarSuscripcionActiva(idUsuario);
        return ResponseEntity.ok(tieneActiva);
    }
}
