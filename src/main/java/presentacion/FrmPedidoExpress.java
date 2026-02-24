package presentacion;

import negocio.DTOs.ProductoDTO;
import negocio.DTOs.DetallePedidoDTO;
import negocio.DTOs.PedidoExpressDTO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FrmPedidoExpress extends JFrame {

    private final IPedidoExpressBO pedidoBO;
    private final IProductoBO productoBO;

    private JPanel panelProductos;
    private JPanel panelDetalles;
    private JLabel lblNombre, lblPrecio, lblDescripcion;
    private JTextField txtCantidad;
    private JTextArea txtNota;
    private JButton btnAgregar, btnFinalizar;

    private List<ProductoDTO> productos;
    private ProductoDTO productoSeleccionado;
    private List<DetallePedidoDTO> detallesPedido = new ArrayList<>();
    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private JList<String> listaDetalles;

    public FrmPedidoExpress(IProductoBO productoBO,
                            IPedidoExpressBO pedidoExpressBO) {

        this.productoBO = productoBO;
        this.pedidoBO = pedidoExpressBO;

        inicializar();

        try {
            cargarProductos();
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializar() {

        setTitle("Pedido Express");
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

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
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelDetalles.add(lblNombre, gbc);

        lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 1;
        panelDetalles.add(lblPrecio, gbc);

        lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 2;
        panelDetalles.add(lblDescripcion, gbc);

        gbc.gridwidth = 1;

        JLabel lblCantidad = new JLabel("Cantidad:");
        gbc.gridy = 3; gbc.gridx = 0;
        panelDetalles.add(lblCantidad, gbc);

        txtCantidad = new JTextField("1");
        gbc.gridx = 1;
        panelDetalles.add(txtCantidad, gbc);

        JLabel lblNota = new JLabel("Nota:");
        gbc.gridy = 4; gbc.gridx = 0;
        panelDetalles.add(lblNota, gbc);

        txtNota = new JTextArea(3,20);
        txtNota.setLineWrap(true);
        txtNota.setWrapStyleWord(true);
        JScrollPane scrollNota = new JScrollPane(txtNota);
        gbc.gridx = 1;
        panelDetalles.add(scrollNota, gbc);

        btnAgregar = new JButton("Agregar al pedido");
        btnAgregar.setBackground(new Color(0,123,255));
        btnAgregar.setForeground(Color.WHITE);

        btnFinalizar = new JButton("Finalizar Pedido");
        btnFinalizar.setBackground(new Color(40,167,69));
        btnFinalizar.setForeground(Color.WHITE);

        gbc.gridy = 5; gbc.gridx = 0;
        panelDetalles.add(btnAgregar, gbc);

        gbc.gridx = 1;
        panelDetalles.add(btnFinalizar, gbc);

        listaDetalles = new JList<>(modeloLista);
        listaDetalles.setBorder(BorderFactory.createTitledBorder("Detalles del pedido"));
        gbc.gridy = 6; gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        panelDetalles.add(new JScrollPane(listaDetalles), gbc);

        btnAgregar.addActionListener(e -> agregarDetalle());
        btnFinalizar.addActionListener(e -> finalizarPedido());
    }

    private void cargarProductos() throws NegocioException {

        productos = productoBO.obtenerDisponibles();
        panelProductos.removeAll();

        for (ProductoDTO p : productos) {

            JPanel tarjeta = new JPanel(new BorderLayout());
            tarjeta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            tarjeta.setMaximumSize(new Dimension(420, 80));
            tarjeta.setBackground(new Color(245,245,245));
            tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel lblTitulo = new JLabel("<html><b>" + p.getNombre() + "</b></html>");
            lblTitulo.setBorder(new EmptyBorder(5,5,5,5));

            JLabel lblPrecioProd = new JLabel("Precio: $" + p.getPrecio());
            lblPrecioProd.setBorder(new EmptyBorder(0,5,5,5));

            tarjeta.add(lblTitulo, BorderLayout.NORTH);
            tarjeta.add(lblPrecioProd, BorderLayout.SOUTH);

            tarjeta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mostrarDetallesProducto(p);
                }
            });

            panelProductos.add(tarjeta);
            panelProductos.add(Box.createRigidArea(new Dimension(0,5)));
        }

        panelProductos.revalidate();
        panelProductos.repaint();
    }

    private void mostrarDetallesProducto(ProductoDTO p) {
        productoSeleccionado = p;
        lblNombre.setText("Nombre: " + p.getNombre());
        lblPrecio.setText("Precio: $" + p.getPrecio());
        lblDescripcion.setText("Descripción: " +
                (p.getDescripcion() != null ? p.getDescripcion() : "Sin descripción"));
        txtCantidad.setText("1");
        txtNota.setText("");
    }

    private void agregarDetalle() {

        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un producto primero");
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) throw new NumberFormatException();

            DetallePedidoDTO detalle = new DetallePedidoDTO(
                    productoSeleccionado.getIdProducto(),
                    cantidad,
                    productoSeleccionado.getPrecio(),
                    txtNota.getText()
            );

            detallesPedido.add(detalle);
            modeloLista.addElement(productoSeleccionado.getNombre() + " x" + cantidad);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Cantidad inválida");
        }
    }

    private void finalizarPedido() {

        if (detallesPedido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Agrega productos al pedido");
            return;
        }

        double subtotal = calcularSubtotal();

        PedidoExpressDTO dto = new PedidoExpressDTO();
        dto.setSubtotal(subtotal);
        dto.setTotal(subtotal);
        dto.setDetalles(detallesPedido);
        dto.setFechaCreacion(LocalDateTime.now());

        try {

            PedidoExpressDTO resultado =
                    pedidoBO.registrarPedido(dto);

            JOptionPane.showMessageDialog(this,
                    "Pedido registrado exitosamente\n\n"
                    + "Folio: " + resultado.getFolio()
                    + "\nPIN: " + resultado.getPin(),
                    "Pedido Express",
                    JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();

        } catch (NegocioException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        lblNombre.setText("Nombre:");
        lblPrecio.setText("Precio:");
        lblDescripcion.setText("Descripción:");
        txtCantidad.setText("1");
        txtNota.setText("");
    }
}