package presentacion;

import negocio.DTOs.*;
import negocio.BOs.*;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FrmCrearPedido extends JFrame {

    private final Color ACCENT_COLOR = new Color(63, 81, 181); 
    private final Color BG_COLOR = new Color(245, 247, 250);    
    private final Color SUCCESS_COLOR = new Color(46, 125, 50); 
    private final Color DANGER_COLOR = new Color(211, 47, 47);  
    private final Color CARD_BORDER = new Color(220, 220, 220);

    private final IPedidoProgramadoBO pedidoBO;
    private final IPedidoExpressBO pedidoExpressBO;
    private final IProductoBO productoBO;
    private final IUsuarioBO usuarioBO;
    private final boolean esExpress;

    private JPanel panelProductos;
    private JLabel lblNombre, lblPrecio, lblDescripcion;
    private JTextField txtCantidad, txtCupon;
    private JTextArea txtNota;
    private JButton btnAgregar, btnFinalizar, btnVolver, btnEliminar;

    private List<ProductoDTO> productos;
    private ProductoDTO productoSeleccionado;
    private final List<DetallePedidoDTO> detallesPedido = new ArrayList<>();
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private JList<String> listaDetalles;

    public FrmCrearPedido(IUsuarioBO usuarioBO, IProductoBO productoBO, 
                          IPedidoProgramadoBO pedidoProgramadoBO, IPedidoExpressBO pedidoExpressBO, 
                          boolean esExpress) {
        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.pedidoBO = pedidoProgramadoBO;
        this.pedidoExpressBO = pedidoExpressBO;
        this.esExpress = esExpress;

        configurarVentana();
        inicializarComponentes();
        cargarProductosSeguro();
    }

    private void configurarVentana() {
        setTitle(esExpress ? "Nuevo Pedido Express" : "Nuevo Pedido Programado");
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_COLOR);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 0));

        // --- PANEL IZQUIERDO: CATÁLOGO ---
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setBorder(new EmptyBorder(25, 25, 25, 10));

        JLabel lblTituloCat = new JLabel("Catálogo de Productos");
        lblTituloCat.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloCat.setForeground(ACCENT_COLOR);
        panelIzquierdo.add(lblTituloCat, BorderLayout.NORTH);

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        panelProductos.setBackground(BG_COLOR);
        
        JScrollPane scrollProd = new JScrollPane(panelProductos);
        scrollProd.setBorder(new LineBorder(CARD_BORDER));
        scrollProd.getVerticalScrollBar().setUnitIncrement(16);
        panelIzquierdo.add(scrollProd, BorderLayout.CENTER);
        panelIzquierdo.setPreferredSize(new Dimension(400, 0));
        add(panelIzquierdo, BorderLayout.WEST);

        // --- PANEL DERECHO: FORMULARIO Y CARRITO ---
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(new EmptyBorder(25, 10, 25, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // 1. Card Info (Producto Seleccionado)
        JPanel cardInfo = crearPanelTarjeta();
        cardInfo.setLayout(new GridLayout(0, 1, 2, 2));
        lblNombre = new JLabel("Seleccione un producto");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPrecio = new JLabel("$ 0.00");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrecio.setForeground(SUCCESS_COLOR);
        lblDescripcion = new JLabel("<html><body style='width: 300px'>Selecciona un producto a la izquierda.</body></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(Color.GRAY);
        cardInfo.add(lblNombre);
        cardInfo.add(lblPrecio);
        cardInfo.add(lblDescripcion);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelDerecho.add(cardInfo, gbc);

        // 2. Card Formulario (Ajuste de tamaño para que no se vea chico)
        JPanel cardForm = crearPanelTarjeta();
        cardForm.setLayout(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5, 10, 5, 10);
        f.fill = GridBagConstraints.HORIZONTAL;

        // Cantidad
        f.gridx = 0; f.gridy = 0; f.weightx = 0.0;
        cardForm.add(new JLabel("Cantidad:"), f);
        txtCantidad = new JTextField("1");
        txtCantidad.setPreferredSize(new Dimension(0, 35));
        f.gridx = 1; f.weightx = 1.0;
        cardForm.add(txtCantidad, f);

        // Nota
        f.gridx = 0; f.gridy = 1; f.weightx = 0.0;
        cardForm.add(new JLabel("Nota:"), f);
        txtNota = new JTextArea(2, 20);
        txtNota.setLineWrap(true);
        JScrollPane scrollNota = new JScrollPane(txtNota);
        scrollNota.setPreferredSize(new Dimension(0, 50));
        f.gridx = 1; f.weightx = 1.0;
        cardForm.add(scrollNota, f);

        // Cupón (Condicional)
        if (!esExpress) {
            f.gridx = 0; f.gridy = 2; f.weightx = 0.0;
            cardForm.add(new JLabel("Cupón:"), f);
            txtCupon = new JTextField();
            txtCupon.setPreferredSize(new Dimension(0, 35));
            f.gridx = 1; f.weightx = 1.0;
            cardForm.add(txtCupon, f);
        }

        // Botón Agregar
        btnAgregar = crearBoton("AGREGAR AL PEDIDO", ACCENT_COLOR, Color.WHITE);
        f.gridx = 0; f.gridy = 3; f.gridwidth = 2; 
        f.insets = new Insets(10, 10, 5, 10);
        cardForm.add(btnAgregar, f);

        gbc.gridy = 1; gbc.weighty = 0.0;
        panelDerecho.add(cardForm, gbc);

        // 3. Card Carrito (ESTE ES EL QUE SE RECORTA/ESTIRA)
        JPanel cardCarrito = crearPanelTarjeta();
        cardCarrito.setLayout(new BorderLayout(0, 8));
        cardCarrito.setBorder(BorderFactory.createTitledBorder(new LineBorder(CARD_BORDER), " RESUMEN ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 11), ACCENT_COLOR));
        
        listaDetalles = new JList<>(modeloLista);
        listaDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLista = new JScrollPane(listaDetalles);
        cardCarrito.add(scrollLista, BorderLayout.CENTER);

        btnEliminar = crearBoton("ELIMINAR", Color.WHITE, DANGER_COLOR);
        btnEliminar.setPreferredSize(new Dimension(0, 35)); // Más compacto
        cardCarrito.add(btnEliminar, BorderLayout.SOUTH);

        gbc.gridy = 2; gbc.weighty = 1.0; // Prioridad total de crecimiento
        panelDerecho.add(cardCarrito, gbc);

        // 4. Panel Acciones Finales
        JPanel panelAcciones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelAcciones.setOpaque(false);
        btnVolver = crearBoton("← VOLVER", Color.WHITE, ACCENT_COLOR);
        btnFinalizar = crearBoton("FINALIZAR", SUCCESS_COLOR, Color.WHITE);
        panelAcciones.add(btnVolver);
        panelAcciones.add(btnFinalizar);

        gbc.gridy = 3; gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 0, 0, 0);
        panelDerecho.add(panelAcciones, gbc);

        add(panelDerecho, BorderLayout.CENTER);

        // Eventos
        btnAgregar.addActionListener(e -> agregarDetalle());
        btnFinalizar.addActionListener(e -> mostrarResumen());
        btnVolver.addActionListener(e -> volverAlMenu());
        btnEliminar.addActionListener(e -> eliminarDetalle());
    }

    private JPanel crearPanelTarjeta() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(CARD_BORDER, 1), new EmptyBorder(10, 12, 10, 12)));
        return p;
    }

    private JButton crearBoton(String t, Color bg, Color fg) {
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 42)); // Altura fija robusta
        b.setBorder(BorderFactory.createLineBorder(fg.equals(Color.WHITE) ? bg : fg, 1));
        return b;
    }

    private void agregarDetalle() {
        if (productoSeleccionado == null) return;
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) throw new NumberFormatException();

            String nota = txtNota.getText().trim();
            DetallePedidoDTO detalle = new DetallePedidoDTO(
                    productoSeleccionado.getIdProducto(), cantidad,
                    productoSeleccionado.getPrecio(), nota
            );
            detallesPedido.add(detalle);

            String notaTxt = nota.isEmpty() ? "" : " - " + (nota.length() > 20 ? nota.substring(0, 20) + "..." : nota);
            String item = String.format("%-12s x%-2d $%6.2f%s", 
                    productoSeleccionado.getNombre(), cantidad, (cantidad * productoSeleccionado.getPrecio()), notaTxt);

            modeloLista.addElement(item);
            txtCantidad.setText("1");
            txtNota.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
        }
    }

    private void cargarProductosSeguro() {
        try {
            productos = productoBO.obtenerDisponibles();
            panelProductos.removeAll();
            for (ProductoDTO p : productos) {
                panelProductos.add(crearTarjetaProducto(p));
                panelProductos.add(Box.createRigidArea(new Dimension(0, 8)));
            }
            panelProductos.revalidate();
            panelProductos.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private JPanel crearTarjetaProducto(ProductoDTO p) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 0));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new CompoundBorder(new LineBorder(CARD_BORDER, 1), new EmptyBorder(10, 12, 10, 12)));
        tarjeta.setMaximumSize(new Dimension(380, 60));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel name = new JLabel(p.getNombre());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel price = new JLabel("$" + String.format("%.2f", p.getPrecio()));
        price.setForeground(SUCCESS_COLOR);

        tarjeta.add(name, BorderLayout.CENTER);
        tarjeta.add(price, BorderLayout.EAST);

        tarjeta.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                productoSeleccionado = p;
                lblNombre.setText(p.getNombre());
                lblPrecio.setText("$" + String.format("%.2f", p.getPrecio()));
                lblDescripcion.setText("<html><body style='width: 300px'>" + p.getDescripcion() + "</body></html>");
            }
            public void mouseEntered(MouseEvent e) { tarjeta.setBackground(new Color(248, 249, 255)); }
            public void mouseExited(MouseEvent e) { tarjeta.setBackground(Color.WHITE); }
        });
        return tarjeta;
    }

    private double calcularSubtotal() {
        return detallesPedido.stream().mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario()).sum();
    }

    private void mostrarResumen() {
        if (detallesPedido.isEmpty()) return;
        double sub = calcularSubtotal();
        double desc = 0;
        try {
            if (!esExpress) desc = pedidoBO.calcularDescuento(txtCupon.getText(), sub);
            double tot = sub - desc;
            String msg = String.format("Subtotal: $%.2f\nDescuento: $%.2f\nTotal: $%.2f\n\n¿Confirmar?", sub, desc, tot);
            if (JOptionPane.showConfirmDialog(this, msg, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                registrarPedido(sub, desc, tot);
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void registrarPedido(double subtotal, double descuento, double total) {
        try {
            UsuarioDTO usuario = Sesion.getUsuarioActual();
            if (esExpress) {
                PedidoExpressDTO dto = new PedidoExpressDTO();
                dto.setSubtotal(subtotal); dto.setTotal(total); dto.setDetalles(detallesPedido);
                PedidoExpressDTO r = pedidoExpressBO.registrarPedido(dto);
                JOptionPane.showMessageDialog(this, "Éxito\nFolio: " + r.getFolio() + "\nPIN: " + r.getPin());
            } else {
                pedidoBO.validarLimitePedidos(usuario.getId());
                PedidoProgramadoDTO dto = new PedidoProgramadoDTO(usuario.getId(), 1, subtotal, descuento, total, txtCupon.getText(), detallesPedido);
                pedidoBO.registrarPedido(dto);
                JOptionPane.showMessageDialog(this, "Pedido Programado registrado.");
            }
            limpiarFormulario();
        } catch (NegocioException ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void eliminarDetalle() {
        int idx = listaDetalles.getSelectedIndex();
        if (idx != -1) { detallesPedido.remove(idx); modeloLista.remove(idx); }
    }

    private void limpiarFormulario() {
        detallesPedido.clear(); modeloLista.clear();
        txtCantidad.setText("1"); txtNota.setText("");
        if(txtCupon != null) txtCupon.setText("");
        productoSeleccionado = null;
        lblNombre.setText("Seleccione un producto");
        lblPrecio.setText("$ 0.00");
        lblDescripcion.setText("Seleccione un producto a la izquierda.");
    }

    private void volverAlMenu() {
        dispose();
        if (Sesion.getUsuarioActual() == null) {
            new FrmLogin(usuarioBO, productoBO, pedidoExpressBO, pedidoBO).setVisible(true);
        } else {
            new FrmMenuCliente(usuarioBO, productoBO, pedidoExpressBO, pedidoBO).setVisible(true);
        }
    }
}