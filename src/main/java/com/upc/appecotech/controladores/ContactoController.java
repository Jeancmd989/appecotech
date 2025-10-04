package com.upc.appecotech.controladores;


import com.upc.appecotech.dtos.ContactoDTO;
import com.upc.appecotech.interfaces.IContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ContactoController {
    @Autowired
    private IContactoService contactoService;

    @PostMapping("/contactos")
    public ResponseEntity<?> crearContacto(@RequestBody ContactoDTO contactoDTO) {
        try {
            ContactoDTO nuevoContacto = contactoService.crearContacto(contactoDTO);
            return ResponseEntity.ok(nuevoContacto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/contactos/{idContacto}/estado")
    public ContactoDTO actualizarEstado(@PathVariable Long idContacto, @RequestParam String nuevoEstado) {
        return contactoService.actualizarEstadoContacto(idContacto, nuevoEstado);
    }
}
