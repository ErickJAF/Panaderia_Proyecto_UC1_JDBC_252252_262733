package persistencia.DAOs;

import java.time.LocalDateTime;
import java.util.List;
import negocio.DTOs.PedidoExpressDTO;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Isaias
 */
public interface IPedidoExpressDAO {

    // Insertar pedido completo
    void insertar(PedidoExpressDTO pedido, int idEmpleado)
            throws PersistenciaException;

    // Actualizar estado del pedido
    void actualizarEstado(int idPedido, String estado)
            throws PersistenciaException;

    // Guardar fecha cuando pasa a Listo
    void actualizarFechaListo(int idPedido, LocalDateTime fecha)
            throws PersistenciaException;

    // Buscar pedido por ID
    PedidoExpressDTO buscarPorId(int idPedido)
            throws PersistenciaException;

    // Obtener pedidos pendientes
    List<PedidoExpressDTO> obtenerPendientes()
            throws PersistenciaException;

    // Validar PIN
    boolean validarPin(int idPedido, String pinIngresado)
            throws PersistenciaException;

    // Obtener siguiente folio consecutivo
    int obtenerSiguienteFolio()
            throws PersistenciaException;

    // Obtener empleado disponible automáticamente
    int obtenerEmpleadoDisponible()
            throws PersistenciaException;
}