package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.ProductoDTO;
import com.upc.appecotech.interfaces.IProductoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductoController {
    @Autowired
    private IProductoService productoService;

    @PostMapping("/productos")
    public ResponseEntity<?> crearProducto(@RequestBody ProductoDTO productoDTO) {
        try {
            ProductoDTO nuevoProducto = productoService.crearProducto(productoDTO);
            return ResponseEntity.ok(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        try {
            ProductoDTO actualizado = productoService.actualizarProducto(id, productoDTO);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Producto no encontrado");
    }


    @GetMapping("/productos/ordenar/puntos-asc")
    public ResponseEntity<List<ProductoDTO>> listarPorPuntosAscendente() {
        return ResponseEntity.ok(productoService.listarPorPuntosAscendente());
    }

    @GetMapping("/productos/ordenar/puntos-desc")
    public ResponseEntity<List<ProductoDTO>> listarPorPuntosDescendente() {
        return ResponseEntity.ok(productoService.listarPorPuntosDescendente());
    }

    @GetMapping("/productos/rango")
    public ResponseEntity<List<ProductoDTO>> listarPorRango(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        return ResponseEntity.ok(productoService.listarPorRangoPuntos(min, max));
    }


    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
