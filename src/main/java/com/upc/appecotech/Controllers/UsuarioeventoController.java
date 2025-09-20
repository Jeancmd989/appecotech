package com.upc.appecotech.controllers;

import com.upc.appecotech.dto.UsuarioEventoDTO;
import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IUsuarioEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioeventoController {

    @Autowired
    private IUsuarioEventoService usuarioEventoService;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioEventoDTO> registrarUsuarioEvento(@RequestParam Long idUsuario, @RequestParam Long idEvento){
        UsuarioEventoDTO usuarioEventoDTO = usuarioEventoService.resgistrarUsuarioEvento(idUsuario, idEvento);
        return ResponseEntity.ok(usuarioEventoDTO);
    }


    @PutMapping("/{idUsuarioEvento}/asistencia")
    public ResponseEntity<UsuarioEventoDTO> marcarAsistencia(@PathVariable Long idUsuarioEvento, @RequestParam Boolean asistio) {
        UsuarioEventoDTO dto = usuarioEventoService.marcarAsistencia(idUsuarioEvento, asistio);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<UsuarioEventoDTO>> listarInscritosPorEvento(@PathVariable Long idEvento) {
        List<UsuarioEventoDTO> lista = usuarioEventoService.listarInscritosPorEvento(idEvento);
        return ResponseEntity.ok(lista);
    }

}
