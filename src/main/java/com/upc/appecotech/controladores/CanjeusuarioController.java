package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.CanjeUsuarioDTO;
import com.upc.appecotech.interfaces.ICanjeusuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CanjeusuarioController {
    @Autowired
    private ICanjeusuarioService canjeusuarioService;


    @PostMapping("/canjes")
    public ResponseEntity<?> canjearProducto(@RequestBody CanjeUsuarioDTO canjeUsuarioDTO) {
        try {
            CanjeUsuarioDTO nuevoCanje = canjeusuarioService.canjearProducto(canjeUsuarioDTO);
            return ResponseEntity.ok(nuevoCanje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuarios/{idUsuario}/validar-puntos")
    public ResponseEntity<Boolean> validarPuntosUsuario(
            @PathVariable Long idUsuario,
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad) {
        try {
            boolean tienePuntos = canjeusuarioService.validarPuntosUsuario(idUsuario, idProducto, cantidad);
            return ResponseEntity.ok(tienePuntos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/canjes")
    public ResponseEntity<List<CanjeUsuarioDTO>> listarTodos(){
        return ResponseEntity.ok(canjeusuarioService.listarTodos());
    }

    @GetMapping("/canjes/{id}")
    public ResponseEntity<CanjeUsuarioDTO> buscarPorId(@PathVariable Long id){
        CanjeUsuarioDTO canje = canjeusuarioService.buscarPorId(id);
        if (canje != null) {
            return ResponseEntity.ok(canje);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/usuarios/{idUsuario}/canjes")
    public ResponseEntity<List<CanjeUsuarioDTO>> listarCanjesPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(canjeusuarioService.listarCanjesPorUsuario(idUsuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{idUsuario}/puntos")
    public ResponseEntity<Integer> obtenerPuntosDisponibles(@PathVariable Long idUsuario) {
        try {
            int puntosDisponibles = canjeusuarioService.obtenerPuntosDisponibles(idUsuario);
            return ResponseEntity.ok(puntosDisponibles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
