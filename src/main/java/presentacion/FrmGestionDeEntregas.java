package presentacion;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import negocio.BOs.*;
import negocio.DTOs.*;
import negocio.excepciones.NegocioException;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.DAOs.*;

public class FrmGestionDeEntregas extends JFrame {

    private final Color ACCENT_COLOR = new Color(63, 81, 181);
    private final Color BG_COLOR = new Color(245, 247, 250);

    private IPedidoBO pedidoBO;
    private IPedidoExpressBO pedidoExpressBO;
    private IPedidoProgramadoBO pedidoProgramadoBO;

    private JList<PedidoEntregaDTO> listaPedidos;
    private DefaultListModel<PedidoEntregaDTO> modelPedidos;
    private JTextArea detallesPedido;
    private JLabel lblTipoPedido;
    private JComboBox<String> comboEstado;
    private JTextField txtCliente, txtTelefono, txtFolio;
    private JSpinner spinnerFecha;
    private JButton btnCambiarEstado;

    public FrmGestionDeEntregas() {
        configurarVentana();
        inicializarBOs();
        inicializarComponentes();
        cargarPedidosPorEstado("Pendiente");
    }

    private void configurarVentana() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ignored) {}
        }
        setTitle("Dashboard de Entregas | Sistema de Gestión");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
    }

   private void inicializarBOs() {

    IConexionBD conexionBD = new ConexionBD();

    pedidoBO = new PedidoBO(new PedidoDAO(conexionBD));
    pedidoExpressBO = new PedidoExpressBO(new PedidoExpressDAO(conexionBD));
    ICuponDAO cuponDAO = new CuponDAO(new ConexionBD());

    IPedidoProgramadoDAO pedidoProgramadoDAO =
            new PedidoProgramadoDAO(conexionBD);

    IDetallePedidoDAO detallePedidoDAO =
            new DetallePedidoDAO(conexionBD);

    pedidoProgramadoBO =
            new PedidoProgramadoBO(pedidoProgramadoDAO, detallePedidoDAO, cuponDAO);

    new HistorialEstadoBO(new HistorialEstadoDAO(conexionBD));
}

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JSplitPane split = new JSplitPane();
        split.setDividerLocation(420);
        split.setBorder(new EmptyBorder(10, 10, 10, 10));
        split.setOpaque(false);
        add(split, BorderLayout.CENTER);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));
        panelIzquierdo.setOpaque(false);

        JPanel cardFiltros = crearPanelTarjeta();
        cardFiltros.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.BOTH;

        configurarFilaFiltro(cardFiltros, gbc, 0, "Estado:", comboEstado = new JComboBox<>(new String[]{"Pendiente", "Listo", "Entregado", "Cancelado", "No Entregado"}), null);

        comboEstado.addActionListener(e -> {
            String estadoSeleccionado = (String) comboEstado.getSelectedItem();
            if (estadoSeleccionado != null) {
                cargarPedidosPorEstado(estadoSeleccionado);
            }
        });

        txtCliente = new JTextField();
        configurarFilaFiltro(cardFiltros, gbc, 1, "Cliente:", txtCliente, crearBotonIcono("👤"));

        txtTelefono = new JTextField();
        configurarFilaFiltro(cardFiltros, gbc, 2, "Tel:", txtTelefono, crearBotonIcono("🔍"));

        txtFolio = new JTextField();
        configurarFilaFiltro(cardFiltros, gbc, 3, "Folio:", txtFolio, crearBotonIcono("🔖"));

        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd"));
        configurarFilaFiltro(cardFiltros, gbc, 4, "Fecha:", spinnerFecha, crearBotonIcono("📅"));

        panelIzquierdo.add(cardFiltros, BorderLayout.NORTH);

        modelPedidos = new DefaultListModel<>();
        listaPedidos = new JList<>(modelPedidos);
        listaPedidos.setCellRenderer(new PedidoListRenderer());
        listaPedidos.setFixedCellHeight(60);
        listaPedidos.addListSelectionListener(e -> mostrarDetallesPedido(listaPedidos.getSelectedValue()));

        JScrollPane scrollLista = new JScrollPane(listaPedidos);
        scrollLista.setBorder(new LineBorder(new Color(220, 220, 220)));
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);

        split.setLeftComponent(panelIzquierdo);

        JPanel panelDerecho = crearPanelTarjeta();
        panelDerecho.setLayout(new BorderLayout(15, 15));

        lblTipoPedido = new JLabel("Seleccione un pedido");
        lblTipoPedido.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTipoPedido.setForeground(ACCENT_COLOR);
        panelDerecho.add(lblTipoPedido, BorderLayout.NORTH);

        detallesPedido = new JTextArea();
        detallesPedido.setEditable(false);
        detallesPedido.setFont(new Font("Consolas", Font.PLAIN, 14));
        detallesPedido.setBackground(new Color(250, 250, 250));
        JScrollPane scrollDetalles = new JScrollPane(detallesPedido);
        panelDerecho.add(scrollDetalles, BorderLayout.CENTER);

        btnCambiarEstado = new JButton("ACTUALIZAR ESTADO DEL PEDIDO");
        btnCambiarEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCambiarEstado.setBackground(Color.WHITE);
        btnCambiarEstado.setForeground(ACCENT_COLOR);
        btnCambiarEstado.setPreferredSize(new Dimension(0, 50));
        btnCambiarEstado.setFocusPainted(false);
        btnCambiarEstado.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelDerecho.add(btnCambiarEstado, BorderLayout.SOUTH);

        split.setRightComponent(panelDerecho);
        btnCambiarEstado.addActionListener(e -> cambiarEstadoPedido());
    }

    private void configurarFilaFiltro(JPanel panel, GridBagConstraints gbc, int fila, String texto, JComponent comp, JButton boton) {
        gbc.gridy = fila;
        gbc.gridx = 0; gbc.weightx = 0;
        panel.add(new JLabel(texto), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(comp, gbc);

        if (boton != null) {
            gbc.gridx = 2; gbc.weightx = 0;
            panel.add(boton, gbc);

            if (texto.contains("Cliente")) boton.addActionListener(e -> filtrarPorCliente());
            if (texto.contains("Tel")) boton.addActionListener(e -> filtrarPorTelefono());
            if (texto.contains("Folio")) boton.addActionListener(e -> filtrarPorFolio());
            if (texto.contains("Fecha")) boton.addActionListener(e -> filtrarPorFecha());
        } else {
            gbc.gridwidth = 2;
        }
        gbc.gridwidth = 1;
    }

    private JPanel crearPanelTarjeta() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));
        return p;
    }

    private JButton crearBotonIcono(String icon) {
        JButton b = new JButton(icon);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private class PedidoListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel itemPanel = new JPanel(new BorderLayout(15, 0));
            itemPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

            if (value instanceof PedidoEntregaDTO) {
                PedidoEntregaDTO p = (PedidoEntregaDTO) value;
                JPanel infoPanel = new JPanel(new GridLayout(2, 1, 2, 2));
                infoPanel.setOpaque(false);

                JLabel lblPrincipal = new JLabel();
                JLabel lblSecundario = new JLabel();
                lblPrincipal.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblSecundario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblSecundario.setForeground(new Color(100, 100, 100));

                if (p.getNombreCliente() == null || p.getNombreCliente().isEmpty()) {
                    lblPrincipal.setText("EXPRESS - Folio: " + p.getFolio());
                    lblSecundario.setText("Estado: " + p.getEstado());
                    lblPrincipal.setForeground(new Color(230, 81, 0));
                } else {
                    lblPrincipal.setText(p.getNombreCliente());
                    lblSecundario.setText(p.getTelefono() + "  |  " + p.getEstado());
                    lblPrincipal.setForeground(new Color(21, 101, 192));
                }

                infoPanel.add(lblPrincipal);
                infoPanel.add(lblSecundario);

                JLabel lblMonto = new JLabel("$" + String.format("%.2f", p.getTotal()));
                lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 15));
                lblMonto.setForeground(new Color(46, 125, 50));

                itemPanel.add(infoPanel, BorderLayout.CENTER);
                itemPanel.add(lblMonto, BorderLayout.EAST);
            }

            if (isSelected) itemPanel.setBackground(new Color(225, 235, 255));
            else itemPanel.setBackground(index % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));

            return itemPanel;
        }
    }

    private void cargarPedidosPorEstado(String estado) {
        try {
            List<PedidoEntregaDTO> pedidos = pedidoBO.obtenerPorEstado(estado);
            modelPedidos.clear();
            pedidos.forEach(modelPedidos::addElement);
        } catch (NegocioException ex) { mostrarError("Error al cargar pedidos", ex); }
    }

    private void filtrarPorCliente() {
        try {
            String cliente = txtCliente.getText().trim();
            if (!cliente.isEmpty()) {
                modelPedidos.clear();
                pedidoBO.buscarPorCliente(cliente).forEach(modelPedidos::addElement);
            }
        } catch (NegocioException ex) { mostrarError("Error en filtro por cliente", ex); }
    }

    private void filtrarPorTelefono() {
        try {
            String tel = txtTelefono.getText().trim();
            if (!tel.isEmpty()) {
                modelPedidos.clear();
                pedidoBO.buscarPorTelefono(tel).forEach(modelPedidos::addElement);
            }
        } catch (NegocioException ex) { mostrarError("Error en filtro", ex); }
    }

    private void filtrarPorFolio() {
        try {
            String f = txtFolio.getText().trim();
            if (!f.isEmpty()) {
                modelPedidos.clear();
                pedidoBO.buscarPorFolio(Integer.parseInt(f)).forEach(modelPedidos::addElement);
            }
        } catch (NumberFormatException | NegocioException ex) { mostrarError("Folio inválido", ex); }
    }

    private void filtrarPorFecha() {
        try {
            java.util.Date fecha = (java.util.Date) spinnerFecha.getValue();
            LocalDate inicio = new java.sql.Date(fecha.getTime()).toLocalDate();
            modelPedidos.clear();
            pedidoBO.buscarPorRangoFechas(inicio, LocalDate.now()).forEach(modelPedidos::addElement);
        } catch (NegocioException ex) { mostrarError("Error en fecha", ex); }
    }

    private void mostrarDetallesPedido(PedidoEntregaDTO pedido) {
        if (pedido == null) {
            detallesPedido.setText("");
            lblTipoPedido.setText("Seleccione un pedido");
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();
            boolean esExpress = (pedido.getNombreCliente() == null);

            sb.append("  ").append(esExpress ? "PEDIDO EXPRESS" : "PEDIDO PROGRAMADO").append("\n");
            sb.append("  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            sb.append("   [DATOS GENERALES]\n");

            if (esExpress) sb.append("   • Folio:     ").append(pedido.getFolio()).append("\n");
            sb.append("   • Estado:    ").append(pedido.getEstado().toUpperCase()).append("\n");
            sb.append("   • Fecha:     ").append(pedido.getFechaCreacion()).append("\n\n");

            if (esExpress) {
                lblTipoPedido.setText("Gestión Express");
                PedidoExpressDTO pExp = pedidoExpressBO.buscarPorIdDTO(pedido.getIdPedido());
                sb.append("   [SEGURIDAD]\n");
                agregarTablaProductos(sb, pExp.getDetalles());
            } else {
                lblTipoPedido.setText("Gestión Programada");
                PedidoProgramadoDTO pProg = pedidoProgramadoBO.buscarPorIdDTO(pedido.getIdPedido());
                sb.append("   [CLIENTE]\n");
                sb.append("   • Nombre:    ").append(pedido.getNombreCliente()).append("\n");
                sb.append("   • Teléfono:  ").append(pedido.getTelefono()).append("\n");
                sb.append("   • Cupón:     ").append(pProg.getIdCupon() != null ? pProg.getIdCupon() : "Ninguno").append("\n\n");
                agregarTablaProductos(sb, pProg.getDetalles());
            }

            sb.append("\n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            sb.append("   TOTAL A COBRAR:   $").append(String.format("%.2f", pedido.getTotal()));

            detallesPedido.setText(sb.toString());
            detallesPedido.setCaretPosition(0);

        } catch (NegocioException ex) {
            mostrarError("Error al cargar información", ex);
        }
    }

    private void agregarTablaProductos(StringBuilder sb, List<DetallePedidoDTO> detalles) {
        sb.append("   [PRODUCTOS]\n");
        sb.append(String.format("   %-15s %-10s %-10s\n", "ID Prod.", "Cant.", "Precio"));
        sb.append("   ------------------------------------\n");
        for (DetallePedidoDTO d : detalles) {
            sb.append(String.format("   %-15d %-10d $%-10.2f\n", d.getIdProducto(), d.getCantidad(), d.getPrecioUnitario()));
            if(d.getNota() != null && !d.getNota().isEmpty()) sb.append("     └ Nota: ").append(d.getNota()).append("\n");
        }
    }

    private void cambiarEstadoPedido() {

        PedidoEntregaDTO pedido = listaPedidos.getSelectedValue();

        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido primero.");
            return;
        }

        String nuevoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Actualizar estado a:",
                "Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Pendiente", "Listo", "Entregado", "Cancelado", "No Entregado"},
                pedido.getEstado()
        );

        if (nuevoEstado == null || nuevoEstado.equals(pedido.getEstado())) {
            return;
        }

        try {

            boolean esExpress = (pedido.getNombreCliente() == null);

            if (esExpress) {

                JPasswordField passwordField = new JPasswordField();

                int opcion = JOptionPane.showConfirmDialog(
                        this,
                        passwordField,
                        "Ingrese PIN de seguridad",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (opcion != JOptionPane.OK_OPTION) {
                    return; // Canceló
                }

                String pinIngresado = new String(passwordField.getPassword());

                pedidoExpressBO.validarPin(
                        pedido.getIdPedido(),
                        pinIngresado
                );
            }

            if ("Entregado".equalsIgnoreCase(nuevoEstado)) {

                String metodoPago = (String) JOptionPane.showInputDialog(
                        this,
                        "Seleccione el método de pago:",
                        "Generar Pago",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Efectivo", "Tarjeta"},
                        "Efectivo"
                );

                if (metodoPago == null) {
                    return;
                }

                pedidoBO.generarPago(
                        pedido.getTotal(),
                        metodoPago,
                        pedido.getIdPedido()
                );
            }

            if (esExpress) {
                pedidoExpressBO.actualizarEstado(pedido.getIdPedido(), nuevoEstado);
            } else {
                pedidoProgramadoBO.actualizarEstado(pedido.getIdPedido(), nuevoEstado);
            }

            JOptionPane.showMessageDialog(this, "¡Estado actualizado correctamente!");
            cargarPedidosPorEstado((String) comboEstado.getSelectedItem());

        } catch (NegocioException ex) {
            mostrarError("No se pudo actualizar", ex);
        }
    }

    private void mostrarError(String titulo, Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), titulo, JOptionPane.ERROR_MESSAGE);
    }
}