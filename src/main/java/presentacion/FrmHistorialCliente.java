package presentacion;

import negocio.BOs.*;
import negocio.DTOs.PedidoProgramadoDTO;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class FrmHistorialCliente extends JFrame {

    private final Color ACCENT_COLOR = new Color(63, 81, 181);
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color RED_COLOR = new Color(180, 50, 50);

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoExpressBO pedidoExpressBO;
    private final IPedidoProgramadoBO pedidoBO;

    private JList<PedidoProgramadoDTO> listaPedidos;
    private DefaultListModel<PedidoProgramadoDTO> modeloPedidos;
    private JButton btnCancelar;

    public FrmHistorialCliente(IUsuarioBO usuarioBO,
                                IProductoBO productoBO,
                                IPedidoExpressBO pedidoExpressBO,
                                IPedidoProgramadoBO pedidoBO) {
        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.pedidoExpressBO = pedidoExpressBO;
        this.pedidoBO = pedidoBO;

        configurarVentana();
        inicializarComponentes();
        cargarHistorial();
    }

    private void configurarVentana() {
        setTitle("Mi Historial de Pedidos");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 10));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel lblTitulo = new JLabel("Historial de Compras");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(ACCENT_COLOR);

        JButton btnVolver = crearBotonEstilizado("← Volver", Color.WHITE, ACCENT_COLOR, 100);
        btnVolver.addActionListener(e -> volverAlMenu());

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(btnVolver, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        JPanel containerLista = crearPanelTarjeta();
        containerLista.setLayout(new BorderLayout());
        
        modeloPedidos = new DefaultListModel<>();
        listaPedidos = new JList<>(modeloPedidos);
        listaPedidos.setCellRenderer(new PedidoRenderer());
        listaPedidos.setFixedCellHeight(70);

        JScrollPane scroll = new JScrollPane(listaPedidos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        containerLista.add(scroll, BorderLayout.CENTER);

        JPanel panelMargen = new JPanel(new BorderLayout());
        panelMargen.setOpaque(false);
        panelMargen.setBorder(new EmptyBorder(0, 25, 10, 25));
        panelMargen.add(containerLista, BorderLayout.CENTER);
        add(panelMargen, BorderLayout.CENTER);

        // --- PANEL INFERIOR (Botón Ancho) ---
        JPanel panelAcciones = new JPanel(new BorderLayout());
        panelAcciones.setOpaque(false);
        panelAcciones.setBorder(new EmptyBorder(0, 25, 20, 25));

        btnCancelar = crearBotonEstilizado("CANCELAR PEDIDO SELECCIONADO", Color.WHITE, RED_COLOR, 0);
        btnCancelar.setPreferredSize(new Dimension(0, 45)); // Altura fija, ancho relativo
        btnCancelar.addActionListener(e -> cancelarPedidoSeleccionado());
        
        panelAcciones.add(btnCancelar, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    private void volverAlMenu() {
        dispose();
        new FrmMenuCliente(usuarioBO, productoBO, pedidoExpressBO, pedidoBO).setVisible(true);
    }

    private void cargarHistorial() {
        try {
            int idCliente = Sesion.getUsuarioActual().getId();
            List<PedidoProgramadoDTO> pedidos = pedidoBO.obtenerHistorialCliente(idCliente);
            modeloPedidos.clear();
            
            if (pedidos == null || pedidos.isEmpty()) {
                PedidoProgramadoDTO vacio = new PedidoProgramadoDTO();
                vacio.setEstado("VACÍO"); 
                modeloPedidos.addElement(vacio);
                btnCancelar.setEnabled(false);
            } else {
                for (PedidoProgramadoDTO p : pedidos) {
                    modeloPedidos.addElement(p);
                }
                btnCancelar.setEnabled(true);
            }
        } catch (NegocioException e) {
            mostrarError("Error", e);
        }
    }

    private void cancelarPedidoSeleccionado() {
        PedidoProgramadoDTO pedido = listaPedidos.getSelectedValue();
        if (pedido == null || "VACÍO".equals(pedido.getEstado())) return;

        if (!"Pendiente".equalsIgnoreCase(pedido.getEstado())) {
            JOptionPane.showMessageDialog(this, "Solo se pueden cancelar pedidos en estado PENDIENTE.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar el pedido #" + pedido.getIdPedido() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pedidoBO.cancelarPedido(pedido.getIdPedido());
                cargarHistorial(); 
            } catch (NegocioException e) {
                mostrarError("Error", e);
            }
        }
    }

    private JPanel crearPanelTarjeta() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(5, 5, 5, 5)));
        return p;
    }

    private JButton crearBotonEstilizado(String texto, Color bg, Color fg, int ancho) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (ancho > 0) btn.setPreferredSize(new Dimension(ancho, 40));
        btn.setBorder(BorderFactory.createLineBorder(fg, 2));
        return btn;
    }

    private void mostrarError(String titulo, Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), titulo, JOptionPane.ERROR_MESSAGE);
    }

    private class PedidoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof PedidoProgramadoDTO) {
                PedidoProgramadoDTO p = (PedidoProgramadoDTO) value;
                if ("VACÍO".equals(p.getEstado())) {
                    JLabel lblVacio = new JLabel("No tienes pedidos registrados actualmente.");
                    lblVacio.setHorizontalAlignment(SwingConstants.CENTER);
                    lblVacio.setForeground(Color.GRAY);
                    lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                    return lblVacio;
                }
                JPanel item = new JPanel(new BorderLayout(15, 0));
                item.setBorder(new EmptyBorder(10, 15, 10, 15));
                JPanel info = new JPanel(new GridLayout(2, 1)); info.setOpaque(false);
                info.add(new JLabel("Pedido #" + p.getIdPedido()));
                JLabel f = new JLabel("Fecha: " + p.getFechaCreacion()); f.setFont(new Font("Segoe UI", 0, 12));
                info.add(f);
                JPanel status = new JPanel(new GridLayout(2, 1)); status.setOpaque(false);
                JLabel est = new JLabel(p.getEstado().toUpperCase()); est.setHorizontalAlignment(4);
                status.add(est);
                JLabel tot = new JLabel("$" + String.format("%.2f", p.getTotal())); tot.setHorizontalAlignment(4);
                status.add(tot);
                item.add(info, BorderLayout.CENTER);
                item.add(status, BorderLayout.EAST);
                item.setBackground(isSelected ? new Color(232, 234, 246) : (index % 2 == 0 ? Color.WHITE : new Color(252, 252, 252)));
                return item;
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}