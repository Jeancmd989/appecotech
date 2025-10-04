package com.upc.appecotech.servicios;

import com.upc.appecotech.entidades.Producto;
import com.upc.appecotech.interfaces.IProductoService;
import com.upc.appecotech.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService {
    @Autowired
    private ProductoRepositorio productoRepositorio;


    @Override
    public List<Producto> listarProductos() {
        return  productoRepositorio.findAll();
    }
}
