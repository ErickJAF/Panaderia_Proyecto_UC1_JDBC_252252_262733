package presentacion;

import negocio.DTOs.ProductoDTO;
import negocio.DTOs.DetallePedidoDTO;
import negocio.BOs.IProductoBO;
import persistencia.conexion.IConexionBD;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import negocio.BOs.IPedidoProgramadoBO;
import negocio.DTOs.PedidoProgramadoDTO;
import negocio.excepciones.NegocioException;

public class FrmCrearPedidoProgramado extends JFrame {
    private final IPedidoProgramadoBO pedidoBO;
    private final int idEmpleado;
    private final int idCliente;

    private JPanel panelProductos;
    private JPanel panelDetalles;
    private JLabel lblNombre, lblPrecio, lblDescripcion;
    private JTextField txtCantidad, txtCupon;
    private JTextArea txtNota;
    private JButton btnAgregar, btnFinalizar;

    private List<ProductoDTO> productos;
    private final IProductoBO productoBO;

    private ProductoDTO productoSeleccionado;
    private List<DetallePedidoDTO> detallesPedido = new ArrayList<>();
    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private JList<String> listaDetalles;

    public FrmCrearPedidoProgramado(
        IConexionBD conexionBD,
        IProductoBO productoBO,
        IPedidoProgramadoBO pedidoBO,
        int idEmpleado,
        int idCliente
) throws NegocioException {

    this.productoBO = productoBO;
    this.pedidoBO = pedidoBO;
    this.idEmpleado = idEmpleado;
    this.idCliente = idCliente;

    inicializar();
    cargarProductos();
}

    private void inicializar() {
        setTitle("Productos Disponibles");
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        JScrollPane scrollProductos = new JScrollPane(panelProductos);
        scrollProductos.setPreferredSize(new Dimension(450, 0));
        add(scrollProductos, BorderLayout.WEST);

        panelDetalles = new JPanel();
        panelDetalles.setLayout(new GridBagLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalle del Producto"));
        panelDetalles.setBackground(Color.WHITE);
        add(panelDetalles, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblNombre = new JLabel("Nombre: ");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelDetalles.add(lblNombre, gbc);

        lblPrecio = new JLabel("Precio: ");
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 1;
        panelDetalles.add(lblPrecio, gbc);

        lblDescripcion = new JLabel("<html>Descripción: </html>");
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 2;
        panelDetalles.add(lblDescripcion, gbc);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panelDetalles.add(lblCantidad, gbc);

        txtCantidad = new JTextField("1");
        gbc.gridx = 1;
        panelDetalles.add(txtCantidad, gbc);

        JLabel lblNota = new JLabel("Nota:");
        lblNota.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelDetalles.add(lblNota, gbc);

        txtNota = new JTextArea(3, 20);
        txtNota.setLineWrap(true);
        txtNota.setWrapStyleWord(true);
        JScrollPane scrollNota = new JScrollPane(txtNota);
        gbc.gridx = 1;
        panelDetalles.add(scrollNota, gbc);

        JLabel lblCupon = new JLabel("Código de cupón:");
        lblCupon.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelDetalles.add(lblCupon, gbc);

        txtCupon = new JTextField();
        gbc.gridx = 1;
        panelDetalles.add(txtCupon, gbc);

        btnAgregar = new JButton("Agregar al pedido");
        btnAgregar.setBackground(new Color(0, 123, 255));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 6;
        panelDetalles.add(btnAgregar, gbc);
        btnAgregar = crearBoton("Agregar al pedido", new Color(0, 123, 255));
        btnFinalizar = crearBoton("Finalizar pedido", new Color(40, 167, 69));

        btnFinalizar = new JButton("Finalizar pedido");
        btnFinalizar.setBackground(new Color(40, 167, 69));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFocusPainted(false);
        gbc.gridx = 1;
        panelDetalles.add(btnFinalizar, gbc);

        listaDetalles = new JList<>(modeloLista);
        listaDetalles.setBorder(BorderFactory.createTitledBorder("Detalles del pedido"));
        listaDetalles.setVisibleRowCount(10);
        JScrollPane scrollLista = new JScrollPane(listaDetalles);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panelDetalles.add(scrollLista, gbc);

        btnAgregar.addActionListener(e -> agregarDetalle());
        btnFinalizar.addActionListener(e -> finalizarPedido());
    }

    private void cargarProductos() throws NegocioException {
        try {
            productos = productoBO.obtenerDisponibles();
            panelProductos.removeAll();

            for (ProductoDTO p : productos) {
                JPanel tarjeta = new JPanel();
                tarjeta.setLayout(new BorderLayout());
                tarjeta.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                tarjeta.setMaximumSize(new Dimension(420, 80));
                tarjeta.setBackground(new Color(245, 245, 245));
                tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel lblTitulo = new JLabel("<html><b>" + p.getNombre() + "</b></html>");
                lblTitulo.setBorder(new EmptyBorder(5, 5, 5, 5));

                JLabel lblPrecioProd = new JLabel("Precio: $" + p.getPrecio());
                lblPrecioProd.setBorder(new EmptyBorder(0, 5, 5, 5));

                tarjeta.add(lblTitulo, BorderLayout.NORTH);
                tarjeta.add(lblPrecioProd, BorderLayout.SOUTH);

                tarjeta.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        tarjeta.setBackground(new Color(220, 220, 220));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        tarjeta.setBackground(new Color(245, 245, 245));
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mostrarDetallesProducto(p);
                    }
                });

                panelProductos.add(tarjeta);
                panelProductos.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            panelProductos.revalidate();
            panelProductos.repaint();

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los productos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallesProducto(ProductoDTO p) {
        productoSeleccionado = p;
        lblNombre.setText("Nombre: " + p.getNombre());
        lblPrecio.setText("Precio: $" + p.getPrecio());
        lblDescripcion.setText("<html>Descripción: " + (p.getDescripcion() != null ? p.getDescripcion() : "Sin descripción") + "</html>");
        txtCantidad.setText("1");
        txtNota.setText("");
        txtCupon.setText("");
    }

    private void agregarDetalle() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero");
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) throw new NumberFormatException();

            String nota = txtNota.getText();

            DetallePedidoDTO detalle = new DetallePedidoDTO(
                    productoSeleccionado.getIdProducto(),
                    cantidad,
                    productoSeleccionado.getPrecio(),
                    nota
            );

            detallesPedido.add(detalle);
            modeloLista.addElement(productoSeleccionado.getNombre() + " x" + cantidad + (nota.isBlank() ? "" : " [" + nota + "]"));

            JOptionPane.showMessageDialog(this,
        "Producto agregado correctamente",
        "Éxito",
        JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
        }
    }

    private void finalizarPedido() {

    if (detallesPedido.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Agrega al menos un producto al pedido");
        return;
    }

    double subtotal = calcularSubtotal();
    String codigoCupon = txtCupon.getText().trim();

    try {

        //  negocio valida y calcula descuento real
        double descuento = pedidoBO.calcularDescuento(codigoCupon, subtotal);
        double total = subtotal - descuento;

        mostrarResumenPedido(subtotal, descuento, total);

    } catch (NegocioException ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Cupón inválido",
                JOptionPane.ERROR_MESSAGE);
    }
}
     private void mostrarResumenPedido(double subtotal, double descuento, double total) {

    JDialog dialogo = new JDialog(this, "Confirmar Pedido", true);
    dialogo.setSize(420, 520);
    dialogo.setLocationRelativeTo(this);
    dialogo.setLayout(new BorderLayout());
    dialogo.getContentPane().setBackground(Color.WHITE);

    JPanel panelContenido = new JPanel();
    panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
    panelContenido.setBorder(new EmptyBorder(15, 20, 15, 20));
    panelContenido.setBackground(Color.WHITE);

    JLabel titulo = new JLabel("Resumen del Pedido");
    titulo.setFont(new Font("Arial", Font.BOLD, 20));
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    panelContenido.add(titulo);
    panelContenido.add(Box.createVerticalStrut(15));

    for (DetallePedidoDTO d : detallesPedido) {

        String nombre = productos.stream()
                .filter(p -> p.getIdProducto() == d.getIdProducto())
                .findFirst()
                .map(ProductoDTO::getNombre)
                .orElse("Producto");

        JLabel lblProducto = new JLabel(
                nombre + "  x" + d.getCantidad() +
                "   $" + String.format("%.2f", d.getCantidad() * d.getPrecioUnitario())
        );

        lblProducto.setFont(new Font("Arial", Font.PLAIN, 14));
        panelContenido.add(lblProducto);
    }

    panelContenido.add(Box.createVerticalStrut(15));
    panelContenido.add(new JSeparator());
    panelContenido.add(Box.createVerticalStrut(10));

    panelContenido.add(new JLabel("Subtotal: $" + String.format("%.2f", subtotal)));
    panelContenido.add(new JLabel("Descuento: $" + String.format("%.2f", descuento)));

    JLabel lblTotal = new JLabel("TOTAL: $" + String.format("%.2f", total));
    lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
    lblTotal.setForeground(new Color(40, 167, 69));

    panelContenido.add(Box.createVerticalStrut(10));
    panelContenido.add(lblTotal);

    JButton btnConfirmar = crearBoton("Confirmar Pedido", new Color(40, 167, 69));
    JButton btnCancelar = crearBoton("Cancelar", new Color(220, 53, 69));

    JPanel panelBotones = new JPanel();
    panelBotones.setBackground(Color.WHITE);
    panelBotones.add(btnConfirmar);
    panelBotones.add(btnCancelar);

    btnConfirmar.addActionListener(e -> {
        guardarPedidoEnBD(subtotal, descuento, total);
        dialogo.dispose();
    });

    btnCancelar.addActionListener(e -> dialogo.dispose());

    dialogo.add(panelContenido, BorderLayout.CENTER);
    dialogo.add(panelBotones, BorderLayout.SOUTH);

    dialogo.setVisible(true);
}

    private JButton crearBoton(String texto, Color color) {
    JButton boton = new JButton(texto);
    boton.setBackground(color);
    boton.setForeground(Color.WHITE);
    boton.setFocusPainted(false);
    boton.setFont(new Font("Arial", Font.BOLD, 14));
    boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    return boton;
  }
     
    private double calcularSubtotal() {
    double subtotal = 0;

    for (DetallePedidoDTO d : detallesPedido) {
        subtotal += d.getCantidad() * d.getPrecioUnitario();
    }

    return subtotal;
}

  private void guardarPedidoEnBD(double subtotal, double descuento, double total) {

    try {

        String codigoCupon = txtCupon.getText().trim();

        PedidoProgramadoDTO dto = new PedidoProgramadoDTO(
                idCliente,
                idEmpleado,
                subtotal,
                descuento,
                total,
                codigoCupon, 
                detallesPedido
        );

        pedidoBO.registrarPedido(dto);

        JOptionPane.showMessageDialog(this,
        "Pedido creado exitosamente",
        "Pedido Confirmado",
        JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();

    } catch (NegocioException ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
private void limpiarFormulario() {

    detallesPedido.clear();
    modeloLista.clear();

    txtCupon.setText("");
    txtCantidad.setText("1");
    txtNota.setText("");

    lblNombre.setText("Nombre:");
    lblPrecio.setText("Precio:");
    lblDescripcion.setText("Descripción:");

    productoSeleccionado = null;
}

}
