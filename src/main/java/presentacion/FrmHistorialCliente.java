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
        
        JButton btnCancelar = new JButton("Cancelar Pedido");
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> cancelarPedidoSeleccionado());
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
    
    private void cancelarPedidoSeleccionado() {

        int indice = listaPedidos.getSelectedIndex();
        if (indice == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido primero.");
            return;
        }

        PedidoProgramadoDTO pedido = null;

        try {
            int idCliente = Sesion.getUsuarioActual().getId();
            List<PedidoProgramadoDTO> pedidos = pedidoBO.obtenerHistorialCliente(idCliente);
            pedido = pedidos.get(indice);

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener pedido: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!"Pendiente".equalsIgnoreCase(pedido.getEstado())) {
            JOptionPane.showMessageDialog(this,
                    "Solo se pueden cancelar pedidos en estado PENDIENTE.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Deseas cancelar el pedido #" + pedido.getIdPedido() + "?",
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pedidoBO.cancelarPedido(pedido.getIdPedido());
                JOptionPane.showMessageDialog(this, "Pedido cancelado correctamente.");
                cargarHistorial(); // refresca la lista
            } catch (NegocioException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al cancelar pedido: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}