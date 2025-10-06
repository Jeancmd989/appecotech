package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.ProductoDTO;
import com.upc.appecotech.entidades.Producto;

import java.util.List;

public interface IProductoService {
    ProductoDTO crearProducto(ProductoDTO productoDTO);
    ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO);
    ProductoDTO buscarPorId(Long id);
    List<ProductoDTO> listarTodos();
    List<ProductoDTO> listarPorPuntosAscendente();
    List<ProductoDTO> listarPorPuntosDescendente();
    List<ProductoDTO> listarPorRangoPuntos(Integer min, Integer max);
    void eliminarProducto(Long id);
}
