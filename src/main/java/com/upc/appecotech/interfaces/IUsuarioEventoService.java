package com.upc.appecotech.interfaces;

import com.upc.appecotech.entidades.Usuarioevento;

import java.util.List;

public interface IUsuarioEventoService {
    public List<Usuarioevento> listarUsuarioeventos();
    public Usuarioevento registrarUsuarioEvento(Usuarioevento usuarioevento);
}
