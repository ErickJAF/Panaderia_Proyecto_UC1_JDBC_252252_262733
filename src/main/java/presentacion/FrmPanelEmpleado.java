package presentacion;

import negocio.BOs.Sesion;
import negocio.DTOs.UsuarioDTO;
import negocio.BOs.IUsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.IPedidoProgramadoBO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FrmPanelEmpleado extends JFrame {

    private final Color ACCENT_COLOR = new Color(63, 81, 181);
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color RED_COLOR = new Color(180, 50, 50);

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoExpressBO pedidoExpressBO;
    private final IPedidoProgramadoBO pedidoProgramadoBO;

    public FrmPanelEmpleado(IUsuarioBO usuarioBO,
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
        setTitle("Panel Empleado - " + (usuario != null ? usuario.getNombreUsuario() : ""));
        setSize(380, 420);
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Panel de Control");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(ACCENT_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        container.add(lblTitulo, gbc);

        JButton btnGestionDeEntregas = crearBotonRectangular("GESTIONAR ENTREGAS", ACCENT_COLOR, Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        container.add(btnGestionDeEntregas, gbc);

        JButton btnGestionProductos = crearBotonRectangular("GESTIONAR PRODUCTOS", Color.WHITE, ACCENT_COLOR);
        btnGestionProductos.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        container.add(btnGestionProductos, gbc);

        JSeparator sep = new JSeparator();
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        container.add(sep, gbc);

        JButton btnCerrarSesion = crearBotonRectangular("CERRAR SESIÓN", Color.WHITE, RED_COLOR);
        btnCerrarSesion.setBorder(BorderFactory.createLineBorder(new Color(230, 200, 200)));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        container.add(btnCerrarSesion, gbc);

        add(container);

        btnGestionDeEntregas.addActionListener(e -> abrirGestionEntregas());
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
        btn.putClientProperty("JButton.buttonType", "square"); 
        return btn;
    }

    private void abrirGestionEntregas() {
        dispose();
        new FrmGestionDeEntregas(usuarioBO, productoBO, pedidoExpressBO, pedidoProgramadoBO).setVisible(true);
    }

    private void cerrarSesion() {
        Sesion.cerrarSesion();
        dispose();
        new FrmLogin(usuarioBO, productoBO, pedidoExpressBO, pedidoProgramadoBO).setVisible(true);
    }
}