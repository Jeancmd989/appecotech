package com.upc.appecotech.Services;

import com.upc.appecotech.dto.CanjeUsuarioDTO;
import com.upc.appecotech.entidades.Canjeusuario;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.entidades.Producto;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.interfaces.ICanjeusuarioService;
import com.upc.appecotech.repositorios.CanjeUsuarioRepositorio;
import com.upc.appecotech.repositorios.HistorialPuntosRepository;
import com.upc.appecotech.repositorios.ProductoRepositorio;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CanjeusuarioService implements ICanjeusuarioService {
    @Autowired
    private CanjeUsuarioRepositorio canjeUsuarioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private HistorialPuntosRepository  historialPuntosRepository;



    @Override
    @Transactional
    public CanjeUsuarioDTO canjearProducto(CanjeUsuarioDTO canjeUsuarioDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(canjeUsuarioDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + canjeUsuarioDTO.getIdUsuario()));

            Producto producto = productoRepositorio.findById(canjeUsuarioDTO.getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + canjeUsuarioDTO.getIdProducto()));

            // Validar puntos suficientes
            if (!validarPuntosUsuario(usuario.getId(), producto.getId(), canjeUsuarioDTO.getCantidad())) {
                throw new RuntimeException("Puntos insuficientes para realizar el canje");
            }

            // Crear registro de canje
            Canjeusuario canjeUsuario = new Canjeusuario();
            canjeUsuario.setIdusuario(usuario);
            canjeUsuario.setIdproducto(producto);
            canjeUsuario.setFechacanje(LocalDate.now());
            canjeUsuario.setCantidad(canjeUsuarioDTO.getCantidad());

            Canjeusuario guardado = canjeUsuarioRepositorio.save(canjeUsuario);

            // Calcular puntos a descontar
            int puntosADescontar = producto.getPuntosrequerido() * canjeUsuarioDTO.getCantidad();

            // Registrar en historial de puntos (descuento)
            Historialdepunto historial = new Historialdepunto();
            historial.setIdusuario(usuario);
            historial.setPuntosobtenidos(0);
            historial.setPuntoscanjeados(puntosADescontar);
            historial.setTipomovimiento("Canje");
            historial.setDescripcion("Canje de producto: " + producto.getNombre() + " (Cantidad: " + canjeUsuarioDTO.getCantidad() + ")");
            historial.setFecha(LocalDate.now());

            historialPuntosRepository.save(historial);

            // Mapeo manual para la respuesta
            CanjeUsuarioDTO response = new CanjeUsuarioDTO();
            response.setIdCanjeUsuario(guardado.getId());
            response.setIdUsuario(guardado.getIdusuario().getId());
            response.setIdProducto(guardado.getIdproducto().getId());
            response.setFechaCanje(guardado.getFechacanje());
            response.setCantidad(guardado.getCantidad());

            return response;

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al realizar canje: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean validarPuntosUsuario(Long idUsuario, Long idProducto, Integer cantidad) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        // Calcular puntos totales del usuario
        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioId(idUsuario);
        int puntosObtenidos = historial.stream().mapToInt(Historialdepunto::getPuntosobtenidos).sum();
        int puntosCanjeados = historial.stream().mapToInt(Historialdepunto::getPuntoscanjeados).sum();
        int puntosDisponibles = puntosObtenidos - puntosCanjeados;

        // Calcular puntos requeridos para el canje
        int puntosRequeridos = producto.getPuntosrequerido() * cantidad;

        return puntosDisponibles >= puntosRequeridos;
    }

    @Override
    @Transactional
    public CanjeUsuarioDTO buscarPorId(Long id) {
        return canjeUsuarioRepositorio.findById(id)
                .map(canjeUsuario -> {
                    CanjeUsuarioDTO dto = new CanjeUsuarioDTO();
                    dto.setIdCanjeUsuario(canjeUsuario.getId());
                    dto.setIdUsuario(canjeUsuario.getIdusuario().getId());
                    dto.setIdProducto(canjeUsuario.getIdproducto().getId());
                    dto.setFechaCanje(canjeUsuario.getFechacanje());
                    dto.setCantidad(canjeUsuario.getCantidad());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public List<CanjeUsuarioDTO> listarTodos() {
        List<Canjeusuario> lista = canjeUsuarioRepositorio.findAll();
        return lista.stream()
                .map(canjeUsuario -> {
                    CanjeUsuarioDTO dto = new CanjeUsuarioDTO();
                    dto.setIdCanjeUsuario(canjeUsuario.getId());
                    dto.setIdUsuario(canjeUsuario.getIdusuario().getId());
                    dto.setIdProducto(canjeUsuario.getIdproducto().getId());
                    dto.setFechaCanje(canjeUsuario.getFechacanje());
                    dto.setCantidad(canjeUsuario.getCantidad());
                    return dto;
                })
                .toList();
    }

    @Override
    public List<CanjeUsuarioDTO> listarCanjesPorUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Canjeusuario> lista = canjeUsuarioRepositorio.findByUsuarioId(idUsuario);

        return lista.stream()
                .map(canjeUsuario -> {
                    CanjeUsuarioDTO dto = new CanjeUsuarioDTO();
                    dto.setIdCanjeUsuario(canjeUsuario.getId());
                    dto.setIdUsuario(canjeUsuario.getIdusuario().getId());
                    dto.setIdProducto(canjeUsuario.getIdproducto().getId());
                    dto.setFechaCanje(canjeUsuario.getFechacanje());
                    dto.setCantidad(canjeUsuario.getCantidad());
                    return dto;
                })
                .toList();
    }
}
