package com.upc.appecotech.controllers;

import com.upc.appecotech.dto.UsuarioEventoDTO;
import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IUsuarioEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioeventoController {

    @Autowired
    private IUsuarioEventoService usuarioEventoService;

    @PostMapping("/registrar-usuario-eventos")
    public ResponseEntity<?> registrarUsuarioEvento(@RequestBody UsuarioEventoDTO usuarioEventoDTO) {
        try {
            UsuarioEventoDTO nuevo = usuarioEventoService.registrarUsuarioEvento(usuarioEventoDTO);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/usuario-eventos/{idUsuarioEvento}/asistencia")
    public UsuarioEventoDTO marcarAsistencia(@PathVariable Long idUsuarioEvento, @RequestParam boolean asistio) {
        return usuarioEventoService.marcarAsistencia(idUsuarioEvento, asistio);
    }

    @GetMapping("/usuario-eventos")
    public List<UsuarioEventoDTO> listarTodos(){
        return usuarioEventoService.listarTodos();
    }

    @GetMapping("/usuario-eventos/{id}")
    public UsuarioEventoDTO buscarPorId(@PathVariable Long id){
        return usuarioEventoService.buscarPorId(id);
    }

    @GetMapping("/eventos/{idEvento}/inscritos")
    public List<UsuarioEventoDTO> listarInscritosPorEvento(@PathVariable Long idEvento) {
        return usuarioEventoService.listarInscritosPorEvento(idEvento);
    }

}
