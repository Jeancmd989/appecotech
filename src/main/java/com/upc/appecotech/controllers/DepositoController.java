package com.upc.appecotech.controllers;

import com.upc.appecotech.dto.DepositoDTO;
import com.upc.appecotech.entidades.Deposito;
import com.upc.appecotech.interfaces.IDepositoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DepositoController {
    @Autowired
    private IDepositoService depositoService;

    @PostMapping("/depositos")
    public ResponseEntity<?> registrarDeposito(@RequestBody DepositoDTO depositoDTO) {
        try {
            DepositoDTO nuevoDeposito = depositoService.registrarDeposito(depositoDTO);
            return ResponseEntity.ok(nuevoDeposito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/depositos/{id}")
    public DepositoDTO actualizarDeposito(@PathVariable Long id, @RequestBody DepositoDTO depositoDTO) {
        return  depositoService.actualizarDeposito(id, depositoDTO);
    }

    @GetMapping("/depositos")
    public List<DepositoDTO> findAll(){
        return depositoService.findAll();
    }


    @GetMapping("/depositos/{id}")
    public DepositoDTO buscarPorId(@PathVariable Long id){
        return depositoService.buscarPorId(id);
    }

    @PutMapping("/depositos/{id}/validar")
    public DepositoDTO validarDeposito(@PathVariable Long id, @RequestParam boolean aprobado){
        return depositoService.validarDeposito(id, aprobado);
    }

}
