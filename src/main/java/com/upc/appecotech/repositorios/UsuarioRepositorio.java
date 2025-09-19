package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepositorio extends JpaRepository<Usuario, Long> {

}
