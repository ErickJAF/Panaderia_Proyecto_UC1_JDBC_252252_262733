/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.DTOs.PedidoEntregaDTO;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class PedidoDAO implements IPedidoDAO{

    private final IConexionBD conexionBD;
    private static final Logger LOG = Logger.getLogger(PedidoDAO.class.getName());

    public PedidoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public List<PedidoEntregaDTO> obtenerPedidosPorEstado(String estado) throws PersistenciaException {

        String sql = """
            SELECT p.id_pedido, p.fecha_creacion, p.estado, p.total,
                   c.nombre_completo,
                   t.numero AS telefono,
                   e.folio
            FROM PEDIDO p
            LEFT JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            LEFT JOIN CLIENTE c ON pr.id_cliente = c.id_cliente
            LEFT JOIN TELEFONO t ON c.id_cliente = t.id_cliente
            LEFT JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE p.estado = ?
            ORDER BY p.fecha_creacion ASC
            """;

        List<PedidoEntregaDTO> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerDTO(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al obtener pedidos por estado", ex);
            throw new PersistenciaException("Error al obtener pedidos por estado", ex);
        }
    }

    @Override
    public List<PedidoEntregaDTO> buscarPorTelefono(String telefono) throws PersistenciaException {

        String sql = """
            SELECT p.id_pedido, p.fecha_creacion, p.estado, p.total,
                   c.nombre_completo,
                   t.numero AS telefono,
                   e.folio
            FROM PEDIDO p
            JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            JOIN CLIENTE c ON pr.id_cliente = c.id_cliente
            JOIN TELEFONO t ON c.id_cliente = t.id_cliente
            LEFT JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE t.numero = ?
            ORDER BY p.fecha_creacion ASC
            """;

        List<PedidoEntregaDTO> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, telefono);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerDTO(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al buscar por teléfono", ex);
            throw new PersistenciaException("Error al buscar por teléfono", ex);
        }
    }

    @Override
    public List<PedidoEntregaDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws PersistenciaException {

        String sql = """
            SELECT p.id_pedido, p.fecha_creacion, p.estado, p.total,
                   c.nombre_completo,
                   t.numero AS telefono,
                   e.folio
            FROM PEDIDO p
            LEFT JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            LEFT JOIN CLIENTE c ON pr.id_cliente = c.id_cliente
            LEFT JOIN TELEFONO t ON c.id_cliente = t.id_cliente
            LEFT JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE DATE(p.fecha_creacion) BETWEEN ? AND ?
            ORDER BY p.fecha_creacion ASC
            """;

        List<PedidoEntregaDTO> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(inicio));
            ps.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerDTO(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al buscar por rango de fechas", ex);
            throw new PersistenciaException("Error al buscar por rango de fechas", ex);
        }
    }

    private PedidoEntregaDTO extraerDTO(ResultSet rs) throws SQLException {

        PedidoEntregaDTO dto = new PedidoEntregaDTO();

        dto.setIdPedido(rs.getInt("id_pedido"));
        dto.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        dto.setEstado(rs.getString("estado"));
        dto.setTotal(rs.getDouble("total"));
        dto.setNombreCliente(rs.getString("nombre_completo"));
        dto.setTelefono(rs.getString("telefono"));

        int folio = rs.getInt("folio");
        dto.setFolio(rs.wasNull() ? null : folio);

        return dto;
    }

    @Override
    public List<PedidoEntregaDTO> buscarPorFolio(int folio) throws PersistenciaException {
        String sql = """
            SELECT p.id_pedido, p.fecha_creacion, p.estado, p.total,
                   c.nombre_completo,
                   t.numero AS telefono,
                   e.folio
            FROM PEDIDO p
            LEFT JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            LEFT JOIN CLIENTE c ON pr.id_cliente = c.id_cliente
            LEFT JOIN TELEFONO t ON c.id_cliente = t.id_cliente
            LEFT JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE e.folio = ?
            """;

        List<PedidoEntregaDTO> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, folio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerDTO(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al buscar por folio", ex);
            throw new PersistenciaException("Error al buscar por folio", ex);
        }
    }
}
