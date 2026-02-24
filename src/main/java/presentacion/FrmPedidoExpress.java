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
    private JLabel lblNombre, lblPrecio, lblDescripcion, lblTotalGeneral;
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
        gbc.gridy = 1;
        panelDetalles.add(lblPrecio, gbc);

        lblDescripcion = new JLabel("Descripción:");
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

        txtNota = new JTextArea(3,20);
        txtNota.setLineWrap(true);
        txtNota.setWrapStyleWord(true);
        gbc.gridx = 1;
        panelDetalles.add(new JScrollPane(txtNota), gbc);

        btnAgregar = new JButton("Agregar");
        btnFinalizar = new JButton("Finalizar Pedido");

        gbc.gridy = 5; gbc.gridx = 0;
        panelDetalles.add(btnAgregar, gbc);

        gbc.gridx = 1;
        panelDetalles.add(btnFinalizar, gbc);

        listaDetalles = new JList<>(modeloLista);
        gbc.gridy = 6; gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        panelDetalles.add(new JScrollPane(listaDetalles), gbc);

        lblTotalGeneral = new JLabel("Total: $0.00");
        lblTotalGeneral.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridy = 7;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelDetalles.add(lblTotalGeneral, gbc);

        btnAgregar.addActionListener(e -> agregarDetalle());
        btnFinalizar.addActionListener(e -> finalizarPedido());
    }

    // 🔹 CARGAR PRODUCTOS (FALTABA)
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
    }

    private void agregarDetalle() {

        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,"Selecciona un producto primero");
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
            actualizarTotal();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"Cantidad inválida");
        }
    }

    private void finalizarPedido() {

        if (detallesPedido.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Agrega productos al pedido");
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
                            + "\nPIN: " + resultado.getPin());

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

    private void actualizarTotal() {
        lblTotalGeneral.setText(
                "Total: $" + String.format("%.2f", calcularSubtotal()));
    }

    private void limpiarFormulario() {
        detallesPedido.clear();
        modeloLista.clear();
        lblTotalGeneral.setText("Total: $0.00");
    }
}