package com.upc.appecotech.Services;

import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IUsuarioEventoService;
import com.upc.appecotech.repositorios.UsuarioEventoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioEventoService implements IUsuarioEventoService {
    @Autowired
    private UsuarioEventoRepositorio usuarioEventoRepositorio;


    @Override
    public List<Usuarioevento> listarUsuarioeventos() {
        return usuarioEventoRepositorio.findAll();
    }

    @Override
    public Usuarioevento registrarUsuarioEvento(Usuarioevento usuarioevento) {
        return usuarioEventoRepositorio.save(usuarioevento);
    }
}
