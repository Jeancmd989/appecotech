package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.ProductoDTO;
import com.upc.appecotech.entidades.Producto;
import com.upc.appecotech.interfaces.IProductoService;
import com.upc.appecotech.repositorios.ProductoRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductoService implements IProductoService {
    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = modelMapper.map(productoDTO, Producto.class);
        producto.setFecha(LocalDate.now());

        Producto guardado = productoRepositorio.save(producto);
        return modelMapper.map(guardado, ProductoDTO.class);
    }

    @Override
    @Transactional
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        return productoRepositorio.findById(id)
                .map(productoExistente -> {
                    if (productoDTO.getNombre() != null) {
                        productoExistente.setNombre(productoDTO.getNombre());
                    }
                    if (productoDTO.getDescripcion() != null) {
                        productoExistente.setDescripcion(productoDTO.getDescripcion());
                    }
                    if (productoDTO.getPuntosrequerido() != null) {
                        productoExistente.setPuntosrequerido(productoDTO.getPuntosrequerido());
                    }

                    Producto actualizado = productoRepositorio.save(productoExistente);
                    return modelMapper.map(actualizado, ProductoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public ProductoDTO buscarPorId(Long id) {
        return productoRepositorio.findById(id)
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarTodos() {
        List<Producto> productos = productoRepositorio.findAll();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorPuntosAscendente() {
        List<Producto> productos = productoRepositorio.findAllByOrderByPuntosrequeridoAsc();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorPuntosDescendente() {
        List<Producto> productos = productoRepositorio.findAllByOrderByPuntosrequeridoDesc();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorRangoPuntos(Integer min, Integer max) {
        List<Producto> productos = productoRepositorio.findByPuntosrequeridoBetween(min, max);
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepositorio.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepositorio.deleteById(id);
    }
}
