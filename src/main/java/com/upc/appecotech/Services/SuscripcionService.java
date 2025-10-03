package com.upc.appecotech.Services;

import com.upc.appecotech.dto.SuscripcionDTO;
import com.upc.appecotech.entidades.Metodopago;
import com.upc.appecotech.entidades.Suscripcion;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.interfaces.ISuscripcionService;
import com.upc.appecotech.repositorios.MetodoPagoRepositorio;
import com.upc.appecotech.repositorios.SuscripcionRepositorio;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SuscripcionService implements ISuscripcionService {
    @Autowired
    private SuscripcionRepositorio suscripcionRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private MetodoPagoRepositorio metodoPagoRepositorio;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public SuscripcionDTO crearSuscripcion(SuscripcionDTO suscripcionDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(suscripcionDTO.getIdusuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + suscripcionDTO.getIdusuario()));

            Metodopago metodoPago = metodoPagoRepositorio.findById(suscripcionDTO.getIdmetodopago())
                    .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado con ID: " + suscripcionDTO.getIdmetodopago()));

            // Validar que no tenga suscripción activa
            if (validarSuscripcionActiva(usuario.getId())) {
                throw new RuntimeException("El usuario ya tiene una suscripción activa");
            }

            // Crear nueva suscripción
            Suscripcion suscripcion = new Suscripcion();
            suscripcion.setIdusuario(usuario);
            suscripcion.setIdmetodopago(metodoPago);
            suscripcion.setTipoplan(suscripcionDTO.getTipoplan());
            suscripcion.setFechainicio(LocalDate.now());
            suscripcion.setFechafin(calcularFechaFin(suscripcionDTO.getTipoplan()));
            suscripcion.setDescripcion(generarDescripcion(suscripcionDTO.getTipoplan()));
            suscripcion.setEstado("Activa");

            Suscripcion guardada = suscripcionRepositorio.save(suscripcion);

            // Mapeo manual para la respuesta
            SuscripcionDTO response = new SuscripcionDTO();
            response.setId(guardada.getId());
            response.setIdusuario(guardada.getIdusuario().getId());
            response.setIdmetodopago(guardada.getIdmetodopago().getId());
            response.setTipoplan(guardada.getTipoplan());
            response.setFechainicio(guardada.getFechainicio());
            response.setFechafin(guardada.getFechafin());
            response.setDescripcion(guardada.getDescripcion());
            response.setEstado(guardada.getEstado());

            return response;

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al crear suscripción: " + e.getMessage());
        }
    }


    @Override
    public boolean validarSuscripcionActiva(Long idUsuario) {
        List<Suscripcion> suscripciones = suscripcionRepositorio.findByUsuarioId(idUsuario);

        return suscripciones.stream()
                .anyMatch(s -> "Activa".equals(s.getEstado()) &&
                        s.getFechafin().isAfter(LocalDate.now()));
    }

    @Override
    public SuscripcionDTO actualizarEstadoSuscripcion(Long idSuscripcion, String nuevoEstado) {
        Suscripcion suscripcion = suscripcionRepositorio.findById(idSuscripcion)
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada con ID: " + idSuscripcion));

        suscripcion.setEstado(nuevoEstado);

        // Si se cancela, mantener fecha fin original
        // Si se suspende, podrías ajustar la lógica según necesidades

        Suscripcion actualizada = suscripcionRepositorio.save(suscripcion);

        // Mapeo manual para la respuesta
        SuscripcionDTO response = new SuscripcionDTO();
        response.setId(actualizada.getId());
        response.setIdusuario(actualizada.getIdusuario().getId());
        response.setIdmetodopago(actualizada.getIdmetodopago().getId());
        response.setTipoplan(actualizada.getTipoplan());
        response.setFechainicio(actualizada.getFechainicio());
        response.setFechafin(actualizada.getFechafin());
        response.setDescripcion(actualizada.getDescripcion());
        response.setEstado(actualizada.getEstado());

        return response;
    }

    @Override
    public SuscripcionDTO renovarSuscripcion(Long idSuscripcion, String nuevoPlan) {
        Suscripcion suscripcionActual = suscripcionRepositorio.findById(idSuscripcion)
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada con ID: " + idSuscripcion));

        // Actualizar suscripción existente
        suscripcionActual.setTipoplan(nuevoPlan);
        suscripcionActual.setFechainicio(LocalDate.now());
        suscripcionActual.setFechafin(calcularFechaFin(nuevoPlan));
        suscripcionActual.setDescripcion(generarDescripcion(nuevoPlan));
        suscripcionActual.setEstado("Activa");

        Suscripcion renovada = suscripcionRepositorio.save(suscripcionActual);

        // Mapeo manual para la respuesta
        SuscripcionDTO response = new SuscripcionDTO();
        response.setId(renovada.getId());
        response.setIdusuario(renovada.getIdusuario().getId());
        response.setIdmetodopago(renovada.getIdmetodopago().getId());
        response.setTipoplan(renovada.getTipoplan());
        response.setFechainicio(renovada.getFechainicio());
        response.setFechafin(renovada.getFechafin());
        response.setDescripcion(renovada.getDescripcion());
        response.setEstado(renovada.getEstado());

        return response;
    }

    @Override
    public SuscripcionDTO buscarPorId(Long id) {
        return null;
    }

    @Override
    public List<SuscripcionDTO> listarTodas() {
        return List.of();
    }

    @Override
    public List<SuscripcionDTO> listarSuscripcionesPorUsuario(Long idUsuario) {
        return List.of();
    }

    @Override
    public SuscripcionDTO obtenerSuscripcionActivaUsuario(Long idUsuario) {
        return null;
    }

    // Métodos helper
    private LocalDate calcularFechaFin(String tipoPlan) {
        LocalDate ahora = LocalDate.now();

        return switch (tipoPlan.toLowerCase()) {
            case "basico" -> ahora.plusMonths(1);
            case "premium" -> ahora.plusMonths(6);
            case "vip" -> ahora.plusYears(1);
            default -> ahora.plusMonths(1);
        };
    }

    private String generarDescripcion(String tipoPlan) {
        return switch (tipoPlan.toLowerCase()) {
            case "basico" -> "Plan Básico - Acceso estándar a funcionalidades";
            case "premium" -> "Plan Premium - Beneficios adicionales y más ecoPuntos";
            case "vip" -> "Plan VIP - Acceso completo y beneficios exclusivos";
            default -> "Plan estándar";
        };

    }

}
