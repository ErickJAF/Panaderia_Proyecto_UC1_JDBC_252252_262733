package presentacion;

import negocio.BOs.IPedidoProgramadoBO;
import negocio.BOs.Sesion;
import negocio.DTOs.PedidoProgramadoDTO;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FrmHistorialCliente extends JFrame {

    private final IPedidoProgramadoBO pedidoBO;
    private JList<String> listaPedidos;

    public FrmHistorialCliente(IPedidoProgramadoBO pedidoBO) {
        this.pedidoBO = pedidoBO;
        inicializar();
        cargarHistorial();
    }

    private void inicializar() {

        setTitle("Historial de Mis Pedidos");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listaPedidos = new JList<>();
        JScrollPane scroll = new JScrollPane(listaPedidos);

        add(scroll, BorderLayout.CENTER);
    }

    private void cargarHistorial() {

        try {

            int idCliente = Sesion.getUsuarioActual().getId();

            List<PedidoProgramadoDTO> pedidos =
                    pedidoBO.obtenerHistorialCliente(idCliente);

            DefaultListModel<String> modelo = new DefaultListModel<>();

            if (pedidos.isEmpty()) {
                modelo.addElement("No tienes pedidos registrados.");
            }

            for (PedidoProgramadoDTO p : pedidos) {

                modelo.addElement(
                        "Pedido #" + p.getIdPedido()
                        + " | Fecha: " + p.getFechaCreacion()
                        + " | Estado: " + p.getEstado()
                        + " | Total: $" + String.format("%.2f", p.getTotal())
                );
            }

            listaPedidos.setModel(modelo);

        } catch (NegocioException e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}