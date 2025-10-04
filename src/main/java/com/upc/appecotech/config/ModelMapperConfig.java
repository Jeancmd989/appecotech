package com.upc.appecotech.config;

import com.upc.appecotech.dtos.*;
import com.upc.appecotech.entidades.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){ ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // ========== USUARIOEVENTO ==========
        // Si tu DTO tiene: private Long id;
        mapper.createTypeMap(Usuarioevento.class, UsuarioEventoDTO.class)
                .addMapping(Usuarioevento::getId, UsuarioEventoDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), UsuarioEventoDTO::setIdusuario)
                .addMapping(src -> src.getIdevento().getId(), UsuarioEventoDTO::setIdevento);

        // ========== CANJEUSUARIO ==========
        // Si tu DTO tiene: private Long id;
        mapper.createTypeMap(Canjeusuario.class, CanjeUsuarioDTO.class)
                .addMapping(Canjeusuario::getId, CanjeUsuarioDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), CanjeUsuarioDTO::setIdUsuario)
                .addMapping(src -> src.getIdproducto().getId(), CanjeUsuarioDTO::setIdProducto);

        // ========== FEEDBACK ==========
        // Si tu DTO tiene: private Long id;
        mapper.createTypeMap(Feedback.class, FeedbackDTO.class)
                .addMapping(Feedback::getId, FeedbackDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), FeedbackDTO::setIdUsuario)
                .addMapping(src -> src.getIdevento().getId(), FeedbackDTO::setIdEvento);

        // ========== SUSCRIPCION ==========
        // Si tu DTO tiene: private Long id;
        mapper.createTypeMap(Suscripcion.class, SuscripcionDTO.class)
                .addMapping(Suscripcion::getId, SuscripcionDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), SuscripcionDTO::setIdusuario)
                .addMapping(src -> src.getIdmetodopago().getId(), SuscripcionDTO::setIdmetodopago);

        // ========== CONTACTO ==========
        // Si tu DTO tiene: private Long id;
        mapper.createTypeMap(Contacto.class, ContactoDTO.class)
                .addMapping(Contacto::getId, ContactoDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), ContactoDTO::setIdUsuario);

        // ========== DEPOSITO ==========
        // Si tu DTO tiene: private Long id;
        mapper.createTypeMap(Deposito.class, DepositoDTO.class)
                .addMapping(Deposito::getId, DepositoDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), DepositoDTO::setIdUsuario);

        // ========== HISTORIALDEPUNTOS ==========
        // Si tu DTO tiene: private Long id;
        mapper.createTypeMap(Historialdepunto.class, HistorialPuntosDTO.class)
                .addMapping(Historialdepunto::getId, HistorialPuntosDTO::setId)
                .addMapping(src -> src.getIdusuario().getId(), HistorialPuntosDTO::setIdUsuario);


        return mapper;
    }
}
