package presentacion;

import negocio.DTOs.*;
import negocio.BOs.*;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FrmCrearPedido extends JFrame {

    private final IPedidoProgramadoBO pedidoBO;
    private final IPedidoExpressBO pedidoExpressBO;
    private final IProductoBO productoBO;
    private final IUsuarioBO usuarioBO;

    private final boolean esExpress;

    private JPanel panelProductos;
    private JPanel panelDetalles;
    private JLabel lblNombre, lblPrecio, lblDescripcion;
    private JTextField txtCantidad, txtCupon;
    private JTextArea txtNota;
    private JButton btnAgregar, btnFinalizar, btnVolver, btnEliminar;

    private List<ProductoDTO> productos;
    private ProductoDTO productoSeleccionado;

    private final List<DetallePedidoDTO> detallesPedido = new ArrayList<>();
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private JList<String> listaDetalles;

    public FrmCrearPedido(
            IUsuarioBO usuarioBO,
            IProductoBO productoBO,
            IPedidoProgramadoBO pedidoProgramadoBO,
            IPedidoExpressBO pedidoExpressBO,
            boolean esExpress
    ) {

        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.pedidoBO = pedidoProgramadoBO;
        this.pedidoExpressBO = pedidoExpressBO;
        this.esExpress = esExpress;

        inicializar();
        cargarProductosSeguro();

        if (esExpress) {
            setTitle("Pedido Express");
            txtCupon.setVisible(false);
        } else {
            setTitle("Pedido Programado");
        }
    }

    private void inicializar() {

        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        JScrollPane scrollProductos = new JScrollPane(panelProductos);
        scrollProductos.setPreferredSize(new Dimension(450, 0));
        add(scrollProductos, BorderLayout.WEST);

        panelDetalles = new JPanel(new GridBagLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalle del Producto"));
        panelDetalles.setBackground(Color.WHITE);
        add(panelDetalles, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));

        lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 18));

        lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelDetalles.add(lblNombre, gbc);

        gbc.gridy = 1;
        panelDetalles.add(lblPrecio, gbc);

        gbc.gridy = 2;
        panelDetalles.add(lblDescripcion, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 3; gbc.gridx = 0;
        panelDetalles.add(new JLabel("Cantidad:"), gbc);

        txtCantidad = new JTextField("1");
        gbc.gridx = 1;
        panelDetalles.add(txtCantidad, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        panelDetalles.add(new JLabel("Nota:"), gbc);

        txtNota = new JTextArea(3, 20);
        txtNota.setLineWrap(true);
        txtNota.setWrapStyleWord(true);
        gbc.gridx = 1;
        panelDetalles.add(new JScrollPane(txtNota), gbc);

        gbc.gridy = 5; gbc.gridx = 0;
        panelDetalles.add(new JLabel("Código cupón:"), gbc);

        txtCupon = new JTextField();
        gbc.gridx = 1;
        panelDetalles.add(txtCupon, gbc);

        btnAgregar = new JButton("Agregar al pedido");
        btnFinalizar = new JButton("Finalizar pedido");
        btnVolver = new JButton("Volver al menú");
        btnEliminar = new JButton("Eliminar detalle");

        btnAgregar.setBackground(new Color(63, 81, 181));
        btnAgregar.setForeground(Color.WHITE);

        btnFinalizar.setBackground(new Color(40, 167, 69));
        btnFinalizar.setForeground(Color.WHITE);
        
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);

        gbc.gridy = 6; gbc.gridx = 0;
        panelDetalles.add(btnAgregar, gbc);

        gbc.gridx = 1;
        panelDetalles.add(btnFinalizar, gbc);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        panelDetalles.add(btnVolver, gbc);

        listaDetalles = new JList<>(modeloLista);
        listaDetalles.setBorder(BorderFactory.createTitledBorder("Detalles del pedido"));
        gbc.gridy = 8;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panelDetalles.add(new JScrollPane(listaDetalles), gbc);

        btnAgregar.addActionListener(e -> agregarDetalle());
        btnFinalizar.addActionListener(e -> mostrarResumen());
        btnVolver.addActionListener(e -> volverAlMenu());

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelDetalles.add(btnEliminar, gbc);

        btnEliminar.addActionListener(e -> eliminarDetalle());
    }

    private void mostrarResumen() {

    if (detallesPedido.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Agrega al menos un producto");
        return;
    }

    double subtotal = calcularSubtotal();
    double descuento = 0;
    double total = subtotal;

    try {

        if (!esExpress) {
            descuento = pedidoBO.calcularDescuento(txtCupon.getText(), subtotal);
            total = subtotal - descuento;
        }

        final double sub = subtotal;
        final double desc = descuento;
        final double tot = total;

        JDialog dialogo = new JDialog(this, "Confirmar Pedido", true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        JTextArea resumen = new JTextArea(
                "Subtotal: $" + sub +
                "\nDescuento: $" + desc +
                "\nTotal: $" + tot +
                "\n\n¿Confirmar pedido?"
        );
        resumen.setEditable(false);

        JButton confirmar = new JButton("Confirmar");
        JButton cancelar = new JButton("Cancelar");

        confirmar.addActionListener(e -> {
            dialogo.dispose();
            registrarPedido(sub, desc, tot);
        });

        cancelar.addActionListener(e -> dialogo.dispose());

        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);

        dialogo.add(resumen, BorderLayout.CENTER);
        dialogo.add(botones, BorderLayout.SOUTH);

        dialogo.setVisible(true);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

    private void registrarPedido(double subtotal, double descuento, double total) {

        try {

            UsuarioDTO usuario = Sesion.getUsuarioActual();

            if (esExpress) {

                PedidoExpressDTO dto = new PedidoExpressDTO();
                dto.setSubtotal(subtotal);
                dto.setTotal(total);
                dto.setDetalles(detallesPedido);

                PedidoExpressDTO r = pedidoExpressBO.registrarPedido(dto);

                JOptionPane.showMessageDialog(this,
                        "Pedido creado\nFolio: " + r.getFolio()
                                + "\nPIN: " + r.getPin());

            } else {

                pedidoBO.validarLimitePedidos(usuario.getId());

                PedidoProgramadoDTO dto =
                        new PedidoProgramadoDTO(
                                usuario.getId(),
                                1,
                                subtotal,
                                descuento,
                                total,
                                txtCupon.getText(),
                                detallesPedido
                        );

                pedidoBO.registrarPedido(dto);

                JOptionPane.showMessageDialog(this,
                        "Pedido Programado creado correctamente");
            }

            limpiarFormulario();

        } catch (NegocioException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlMenu() {
        dispose();
        if (Sesion.getUsuarioActual() == null) {
            new FrmLogin(usuarioBO, productoBO, pedidoExpressBO, pedidoBO).setVisible(true);
        } else {
            new FrmMenuCliente(usuarioBO, productoBO, pedidoExpressBO, pedidoBO).setVisible(true);
        }
    }

    private void agregarDetalle() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero");
            return;
        }

        int cantidad = Integer.parseInt(txtCantidad.getText());

        DetallePedidoDTO detalle = new DetallePedidoDTO(
                productoSeleccionado.getIdProducto(),
                cantidad,
                productoSeleccionado.getPrecio(),
                txtNota.getText()
        );

        detallesPedido.add(detalle);
        modeloLista.addElement(productoSeleccionado.getNombre() + " x" + cantidad);
    }

    private double calcularSubtotal() {
        double subtotal = 0;
        for (DetallePedidoDTO d : detallesPedido) {
            subtotal += d.getCantidad() * d.getPrecioUnitario();
        }
        return subtotal;
    }

    private void limpiarFormulario() {
        detallesPedido.clear();
        modeloLista.clear();
        txtCantidad.setText("1");
        txtNota.setText("");
        txtCupon.setText("");
    }

    private void cargarProductosSeguro() {
        try {
            productos = productoBO.obtenerDisponibles();
            panelProductos.removeAll();

            for (ProductoDTO p : productos) {

                JPanel tarjeta = new JPanel(new BorderLayout());
                tarjeta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                tarjeta.setMaximumSize(new Dimension(420, 60));

                JLabel lbl = new JLabel(p.getNombre() + " - $" + p.getPrecio());
                tarjeta.add(lbl, BorderLayout.CENTER);

                tarjeta.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        productoSeleccionado = p;
                        lblNombre.setText("Nombre: " + p.getNombre());
                        lblPrecio.setText("Precio: $" + p.getPrecio());
                        lblDescripcion.setText("Descripción: " + p.getDescripcion());
                    }
                });

                panelProductos.add(tarjeta);
                panelProductos.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            panelProductos.revalidate();
            panelProductos.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void eliminarDetalle() {

        int indiceSeleccionado = listaDetalles.getSelectedIndex();

        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Selecciona un detalle para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el producto seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {

            detallesPedido.remove(indiceSeleccionado);

            modeloLista.remove(indiceSeleccionado);

            JOptionPane.showMessageDialog(this,
                    "Detalle eliminado correctamente");
        }
    }
}