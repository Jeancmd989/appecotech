package com.upc.appecotech.dto;

import com.upc.appecotech.entidades.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private String contraseña;
}
