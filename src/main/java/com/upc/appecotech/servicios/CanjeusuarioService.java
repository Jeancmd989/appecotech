package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.CanjeUsuarioDTO;
import com.upc.appecotech.entidades.Canjeusuario;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.entidades.Producto;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.interfaces.ICanjeusuarioService;
import com.upc.appecotech.repositorios.CanjeUsuarioRepositorio;
import com.upc.appecotech.repositorios.HistorialPuntosRepository;
import com.upc.appecotech.repositorios.ProductoRepositorio;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public CanjeUsuarioDTO canjearProducto(CanjeUsuarioDTO canjeUsuarioDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(canjeUsuarioDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + canjeUsuarioDTO.getIdUsuario()));

            Producto producto = productoRepositorio.findById(canjeUsuarioDTO.getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + canjeUsuarioDTO.getIdProducto()));

            if (!validarPuntosUsuario(usuario.getId(), producto.getId(), canjeUsuarioDTO.getCantidad())) {
                throw new RuntimeException("Puntos insuficientes para realizar el canje");
            }

            Canjeusuario canjeUsuario = new Canjeusuario();
            canjeUsuario.setIdusuario(usuario);
            canjeUsuario.setIdproducto(producto);
            canjeUsuario.setFechacanje(LocalDate.now());
            canjeUsuario.setCantidad(canjeUsuarioDTO.getCantidad());

            Canjeusuario guardado = canjeUsuarioRepositorio.save(canjeUsuario);


            int puntosADescontar = producto.getPuntosrequerido() * canjeUsuarioDTO.getCantidad();


            Historialdepunto historial = new Historialdepunto();
            historial.setIdusuario(usuario);
            historial.setPuntosobtenidos(0);
            historial.setPuntoscanjeados(puntosADescontar);
            historial.setTipomovimiento("Canje");
            historial.setDescripcion("Canje de producto: " + producto.getNombre() + " (Cantidad: " + canjeUsuarioDTO.getCantidad() + ")");
            historial.setFecha(LocalDate.now());

            historialPuntosRepository.save(historial);

            return modelMapper.map(guardado, CanjeUsuarioDTO.class);

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

        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioId(idUsuario);
        int puntosObtenidos = historial.stream().mapToInt(Historialdepunto::getPuntosobtenidos).sum();
        int puntosCanjeados = historial.stream().mapToInt(Historialdepunto::getPuntoscanjeados).sum();
        int puntosDisponibles = puntosObtenidos - puntosCanjeados;

        int puntosRequeridos = producto.getPuntosrequerido() * cantidad;

        return puntosDisponibles >= puntosRequeridos;
    }

    @Override
    @Transactional
    public CanjeUsuarioDTO buscarPorId(Long id) {
        return canjeUsuarioRepositorio.findById(id)
                .map(canjeUsuario -> modelMapper.map(canjeUsuario, CanjeUsuarioDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional
    public List<CanjeUsuarioDTO> listarTodos() {
        List<Canjeusuario> lista = canjeUsuarioRepositorio.findAll();
        return lista.stream()
                .map(canjeUsuario -> modelMapper.map(canjeUsuario, CanjeUsuarioDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<CanjeUsuarioDTO> listarCanjesPorUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Canjeusuario> lista = canjeUsuarioRepositorio.findByUsuarioId(idUsuario);

        return lista.stream()
                .map(canjeUsuario -> modelMapper.map(canjeUsuario, CanjeUsuarioDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public int obtenerPuntosDisponibles(Long idUsuario) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        List<Historialdepunto> historial = historialPuntosRepository.findByUsuarioId(idUsuario);

        int puntosObtenidos = historial.stream().mapToInt(Historialdepunto::getPuntosobtenidos).sum();
        int puntosCanjeados = historial.stream().mapToInt(Historialdepunto::getPuntoscanjeados).sum();

        return puntosObtenidos - puntosCanjeados;
    }
}
