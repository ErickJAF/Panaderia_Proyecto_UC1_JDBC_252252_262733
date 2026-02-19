package presentacion;

import negocio.DTOs.ProductoDTO;
import negocio.BOs.IProductoBO;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FrmCrearPedidoProgramado extends JFrame {

    private JPanel panelProductos;
    private JPanel panelDetalles;
    private JLabel lblNombre, lblPrecio, lblDescripcion;
    private JTextField txtCantidad, txtCupon;
    private JTextArea txtNota;
    private JButton btnAgregar, btnFinalizar;

    private List<ProductoDTO> productos;
    private final IProductoBO productoBO;

    public FrmCrearPedidoProgramado(IConexionBD conexionBD, IProductoBO productoBO) {
        this.productoBO = productoBO;
        inicializar();
        cargarProductos();
    }

    private void inicializar() {
        setTitle("Productos Disponibles");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        btnFinalizar = new JButton("Finalizar pedido");
        btnFinalizar.setBackground(new Color(40, 167, 69));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFocusPainted(false);
        gbc.gridx = 1;
        panelDetalles.add(btnFinalizar, gbc);
    }

    private void cargarProductos() {
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

        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los productos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallesProducto(ProductoDTO p) {
        lblNombre.setText("Nombre: " + p.getNombre());
        lblPrecio.setText("Precio: $" + p.getPrecio());
        lblDescripcion.setText("<html>Descripción: " + (p.getDescripcion() != null ? p.getDescripcion() : "Sin descripción") + "</html>");
        txtCantidad.setText("1");
        txtNota.setText("");
        txtCupon.setText("");
    }
}
