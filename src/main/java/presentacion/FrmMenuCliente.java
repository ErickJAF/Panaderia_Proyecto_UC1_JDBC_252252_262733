package presentacion;

import negocio.DTOs.UsuarioDTO;
import negocio.BOs.IUsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoProgramadoBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.Sesion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FrmMenuCliente extends JFrame {

    private final Color ACCENT_COLOR = new Color(63, 81, 181);
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color RED_COLOR = new Color(180, 50, 50);

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoProgramadoBO pedidoProgramadoBO;
    private final IPedidoExpressBO pedidoExpressBO;

    public FrmMenuCliente(IUsuarioBO usuarioBO,
                          IProductoBO productoBO,
                          IPedidoExpressBO pedidoExpressBO,
                          IPedidoProgramadoBO pedidoProgramadoBO) {

        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.pedidoExpressBO = pedidoExpressBO;
        this.pedidoProgramadoBO = pedidoProgramadoBO;

        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        UsuarioDTO usuario = Sesion.getUsuarioActual();
        setTitle("Menú Cliente - " + (usuario != null ? usuario.getNombreUsuario() : ""));
        setSize(380, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
    }

    private void inicializarComponentes() {

        setLayout(new GridBagLayout());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(10, 45, 10, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Mi Cuenta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(ACCENT_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        container.add(lblTitulo, gbc);

        JButton btnCrearPedido = crearBotonRectangular("NUEVO PEDIDO PROGRAMADO", ACCENT_COLOR, Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        container.add(btnCrearPedido, gbc);

        JButton btnHistorial = crearBotonRectangular("VER MI HISTORIAL", Color.WHITE, ACCENT_COLOR);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 12, 0);
        container.add(btnHistorial, gbc);

        JButton btnCerrarSesion = crearBotonRectangular("CERRAR SESIÓN", Color.WHITE, RED_COLOR);
        gbc.gridy = 3;
        container.add(btnCerrarSesion, gbc);

        add(container);

        // 🔹 CORREGIDO
        btnCrearPedido.addActionListener(e -> {
            new FrmCrearPedido(
                    usuarioBO,
                    productoBO,
                    pedidoProgramadoBO,
                    pedidoExpressBO,
                    false
            ).setVisible(true);
        });

        btnHistorial.addActionListener(e ->
                new FrmHistorialCliente(pedidoProgramadoBO).setVisible(true)
        );

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private JButton crearBotonRectangular(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(0, 42));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cerrarSesion() {
        Sesion.cerrarSesion();
        dispose();
        new FrmLogin(usuarioBO, productoBO, pedidoExpressBO, pedidoProgramadoBO)
                .setVisible(true);
    }
}