package com.upc.appecotech.controladores;


import com.upc.appecotech.dtos.EventoDTO;
import com.upc.appecotech.interfaces.IEventoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventoController {
    @Autowired
    private IEventoService eventoService;



    @PostMapping("/eventos")
    public ResponseEntity<?> crearEvento(@RequestBody EventoDTO eventoDTO) {
        try {
            EventoDTO nuevoEvento = eventoService.crearEvento(eventoDTO);
            return ResponseEntity.ok(nuevoEvento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/eventos/{id}")
    public ResponseEntity<?> actualizarEvento(@PathVariable Long id, @RequestBody EventoDTO eventoDTO) {
        try {
            EventoDTO actualizado = eventoService.actualizarEvento(id, eventoDTO);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/eventos")
    public ResponseEntity<List<EventoDTO>> listarTodos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }

    @GetMapping("/eventos/proximos")
    public ResponseEntity<List<EventoDTO>> listarEventosProximos() {
        return ResponseEntity.ok(eventoService.listarEventosProximos());
    }

    @GetMapping("/eventos/pasados")
    public ResponseEntity<List<EventoDTO>> listarEventosPasados() {
        return ResponseEntity.ok(eventoService.listarEventosPasados());
    }


    @GetMapping("/eventos/{id}")
    public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Long id) {
        EventoDTO evento = eventoService.buscarPorId(id);
        if (evento != null) {
            return ResponseEntity.ok(evento);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<?> eliminarEvento(@PathVariable Long id) {
        try {
            eventoService.eliminarEvento(id);
            return ResponseEntity.ok("Evento eliminado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
