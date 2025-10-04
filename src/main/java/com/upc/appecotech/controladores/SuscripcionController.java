package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.SuscripcionDTO;
import com.upc.appecotech.interfaces.ISuscripcionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SuscripcionController {
    @Autowired
    private ISuscripcionService suscripcionService;

    @PostMapping("/suscripciones")
    public ResponseEntity<?> crearSuscripcion(@RequestBody SuscripcionDTO suscripcionDTO) {
        try {
            SuscripcionDTO nuevaSuscripcion = suscripcionService.crearSuscripcion(suscripcionDTO);
            return ResponseEntity.ok(nuevaSuscripcion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/suscripciones/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            SuscripcionDTO actualizada = suscripcionService.actualizarEstadoSuscripcion(id, nuevoEstado);
            return ResponseEntity.ok(actualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/suscripciones/{id}/renovar")
    public ResponseEntity<?> renovarSuscripcion(@PathVariable Long id, @RequestParam String nuevoPlan) {
        try {
            SuscripcionDTO renovada = suscripcionService.renovarSuscripcion(id, nuevoPlan);
            return ResponseEntity.ok(renovada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/suscripcion-activa")
    public ResponseEntity<Boolean> validarSuscripcionActiva(@PathVariable Long idUsuario) {
        boolean tieneActiva = suscripcionService.validarSuscripcionActiva(idUsuario);
        return ResponseEntity.ok(tieneActiva);
    }

    @GetMapping("/suscripciones")
    public ResponseEntity<List<SuscripcionDTO>> listarTodas(){
        return ResponseEntity.ok(suscripcionService.listarTodas());
    }

    @GetMapping("/suscripciones/{id}")
    public ResponseEntity<SuscripcionDTO> buscarPorId(@PathVariable Long id){
        SuscripcionDTO suscripcion = suscripcionService.buscarPorId(id);
        if (suscripcion != null) {
            return ResponseEntity.ok(suscripcion);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios/{idUsuario}/suscripciones")
    public ResponseEntity<List<SuscripcionDTO>> listarSuscripcionesPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(suscripcionService.listarSuscripcionesPorUsuario(idUsuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/suscripcion-actual")
    public ResponseEntity<SuscripcionDTO> obtenerSuscripcionActual(@PathVariable Long idUsuario) {
        SuscripcionDTO suscripcion = suscripcionService.obtenerSuscripcionActivaUsuario(idUsuario);
        if (suscripcion != null) {
            return ResponseEntity.ok(suscripcion);
        }
        return ResponseEntity.notFound().build();
    }
}
