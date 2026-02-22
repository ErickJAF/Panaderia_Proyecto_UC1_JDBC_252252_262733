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
import negocio.DTOs.HistorialEstadoDTO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.HistorialEstado;
import persistencia.excepciones.PersistenciaException;

public class HistorialEstadoDAO implements IHistorialEstadoDAO {

    private final IConexionBD conexionBD;
    private static final Logger LOG = Logger.getLogger(HistorialEstadoDAO.class.getName());

    public HistorialEstadoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public List<HistorialEstado> obtenerHistorialPorPedido(int idPedido) throws PersistenciaException {

        String sql = """
            SELECT id_historial, id_pedido, estado_anterior, estado_nuevo, fecha_cambio
            FROM HISTORIAL_ESTADO
            WHERE id_pedido = ?
            ORDER BY fecha_cambio ASC
        """;

        List<HistorialEstado> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerHistorial(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al obtener historial del pedido", ex);
            throw new PersistenciaException("Error al obtener historial del pedido", ex);
        }
    }

    private HistorialEstado extraerHistorial(ResultSet rs) throws SQLException {

        HistorialEstado h = new HistorialEstado();

        h.setIdHistorial(rs.getInt("id_historial"));
        h.setIdPedido(rs.getInt("id_pedido"));
        h.setEstadoAnterior(rs.getString("estado_anterior"));
        h.setEstadoNuevo(rs.getString("estado_nuevo"));
        h.setFechaCambio(rs.getTimestamp("fecha_cambio").toLocalDateTime());

        return h;
    }

    @Override
    public List<HistorialEstadoDTO> obtenerPedidosPorEstado(String estado) throws PersistenciaException {

        String sql = """
            SELECT h.id_historial, h.id_pedido, h.estado_anterior, h.estado_nuevo,
                   h.fecha_cambio, c.nombre_completo,
                   t.numero AS telefono,
                   p.total,
                   e.folio
            FROM HISTORIAL_ESTADO h
            JOIN PEDIDO p ON h.id_pedido = p.id_pedido
            LEFT JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            LEFT JOIN CLIENTE c ON pr.id_cliente = c.id_cliente
            LEFT JOIN TELEFONO t ON c.id_cliente = t.id_cliente
            LEFT JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE h.estado_nuevo = ?
            ORDER BY h.fecha_cambio ASC
        """;

        return ejecutarConsultaConParametro(sql, estado);
    }

    @Override
    public List<HistorialEstadoDTO> buscarPorTelefono(String telefono) throws PersistenciaException {

        String sql = """
            SELECT h.id_historial, h.id_pedido, h.estado_anterior, h.estado_nuevo,
                   h.fecha_cambio, c.nombre_completo,
                   t.numero AS telefono,
                   p.total,
                   e.folio
            FROM HISTORIAL_ESTADO h
            JOIN PEDIDO p ON h.id_pedido = p.id_pedido
            JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            JOIN CLIENTE c ON pr.id_cliente = c.id_cliente
            JOIN TELEFONO t ON c.id_cliente = t.id_cliente
            LEFT JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE t.numero = ?
            ORDER BY h.fecha_cambio ASC
        """;

        return ejecutarConsultaConParametro(sql, telefono);
    }

    @Override
    public List<HistorialEstadoDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws PersistenciaException {

        String sql = """
            SELECT h.id_historial, h.id_pedido, h.estado_anterior, h.estado_nuevo,
                   h.fecha_cambio, c.nombre_completo,
                   t.numero AS telefono,
                   p.total,
                   e.folio
            FROM HISTORIAL_ESTADO h
            JOIN PEDIDO p ON h.id_pedido = p.id_pedido
            LEFT JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            LEFT JOIN CLIENTE c ON pr.id_cliente = c.id_cliente
            LEFT JOIN TELEFONO t ON c.id_cliente = t.id_cliente
            LEFT JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE DATE(h.fecha_cambio) BETWEEN ? AND ?
            ORDER BY h.fecha_cambio ASC
        """;

        List<HistorialEstadoDTO> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(inicio));
            ps.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerHistorialDTO(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al buscar historial por rango de fechas", ex);
            throw new PersistenciaException("Error al buscar historial por rango de fechas", ex);
        }
    }

    @Override
    public List<HistorialEstadoDTO> buscarPorFolio(int folio) throws PersistenciaException {

        String sql = """
            SELECT h.id_historial, h.id_pedido, h.estado_anterior, h.estado_nuevo,
                   h.fecha_cambio, p.total,
                   NULL AS nombre_completo,
                   NULL AS telefono,
                   e.folio
            FROM HISTORIAL_ESTADO h
            JOIN PEDIDO p ON h.id_pedido = p.id_pedido
            JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE e.folio = ?
            ORDER BY h.fecha_cambio ASC
        """;

        return ejecutarConsultaConParametro(sql, folio);
    }

    private List<HistorialEstadoDTO> ejecutarConsultaConParametro(String sql, Object parametro)
            throws PersistenciaException {

        List<HistorialEstadoDTO> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parametro instanceof String) {
                ps.setString(1, (String) parametro);
            } else if (parametro instanceof Integer) {
                ps.setInt(1, (Integer) parametro);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerHistorialDTO(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en consulta de historial", ex);
            throw new PersistenciaException("Error en consulta de historial", ex);
        }
    }

    private HistorialEstadoDTO extraerHistorialDTO(ResultSet rs) throws SQLException {

        HistorialEstadoDTO dto = new HistorialEstadoDTO();

        dto.setIdHistorial(rs.getInt("id_historial"));
        dto.setIdPedido(rs.getInt("id_pedido"));
        dto.setEstadoAnterior(rs.getString("estado_anterior"));
        dto.setEstadoNuevo(rs.getString("estado_nuevo"));
        dto.setFechaCambio(rs.getTimestamp("fecha_cambio").toLocalDateTime());

        dto.setNombreCliente(rs.getString("nombre_completo"));
        dto.setTelefonoCliente(rs.getString("telefono"));
        dto.setTotal(rs.getDouble("total"));

        int folio = rs.getInt("folio");
        dto.setFolio(rs.wasNull() ? null : folio);

        return dto;
    }
}