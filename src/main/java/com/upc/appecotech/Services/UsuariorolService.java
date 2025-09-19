package com.upc.appecotech.Services;

import com.upc.appecotech.entidades.Usuariorol;
import com.upc.appecotech.interfaces.IUsuariorolService;
import com.upc.appecotech.repositorios.UsuariorolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariorolService implements IUsuariorolService {
    @Autowired
    private UsuariorolRepositorio usuariorolRepositorio;

    @Override
    public List<Usuariorol> listarUsuariorols() {
        return usuariorolRepositorio.findAll();
    }


}
