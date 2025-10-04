package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.CanjeUsuarioDTO;
import com.upc.appecotech.interfaces.ICanjeusuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/usuarios/{idUsuario}/validar-puntos/{idProducto}")
    public ResponseEntity<Boolean> validarPuntosUsuario(
            @PathVariable Long idUsuario,
            @PathVariable Long idProducto,
            @RequestParam Integer cantidad) {
        boolean tienePuntos = canjeusuarioService.validarPuntosUsuario(idUsuario, idProducto, cantidad);
        return ResponseEntity.ok(tienePuntos);
    }

    @GetMapping("/canjes")
    public List<CanjeUsuarioDTO> listarTodos(){
        return canjeusuarioService.listarTodos();
    }

    @GetMapping("/canjes/{id}")
    public CanjeUsuarioDTO buscarPorId(@PathVariable Long id){
        return canjeusuarioService.buscarPorId(id);
    }

    @GetMapping("/usuarios/{idUsuario}/canjes")
    public List<CanjeUsuarioDTO> listarCanjesPorUsuario(@PathVariable Long idUsuario) {
        return canjeusuarioService.listarCanjesPorUsuario(idUsuario);
    }
}
