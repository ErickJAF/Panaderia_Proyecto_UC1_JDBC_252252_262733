package presentacion;

import negocio.BOs.*;
import negocio.DTOs.UsuarioDTO;
import negocio.excepciones.NegocioException;
import negocio.BOs.Sesion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FrmLogin extends JFrame {

    private final Color ACCENT_COLOR = new Color(63, 81, 181);
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color TEXT_COLOR = new Color(70, 70, 70);

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoExpressBO pedidoExpressBO;
    private final IPedidoProgramadoBO pedidoProgramadoBO;

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public FrmLogin(IUsuarioBO usuarioBO,
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
        setTitle("Pansito - Acceso");
        setSize(400, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
    }

    private void inicializarComponentes() {
        setLayout(new GridBagLayout());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(ACCENT_COLOR);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        container.add(lblTitulo, gbc);

        JLabel lblUser = new JLabel("USUARIO");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(TEXT_COLOR);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        container.add(lblUser, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(0, 40));
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        container.add(txtUsuario, gbc);

        JLabel lblPass = new JLabel("CONTRASEÑA");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(TEXT_COLOR);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 8, 0);
        container.add(lblPass, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(0, 40));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 30, 0);
        container.add(txtPassword, gbc);

        JButton btnLogin = crearBotonRectangular("INGRESAR", ACCENT_COLOR, Color.WHITE);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(btnLogin, gbc);

        JButton btnExpress = crearBotonRectangular("PEDIDO EXPRESS", Color.WHITE, ACCENT_COLOR);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        container.add(btnExpress, gbc);

        add(container, gbc);

        btnLogin.addActionListener(e -> iniciarSesion());
        btnExpress.addActionListener(e -> {
            dispose();
            new FrmCrearPedido(usuarioBO, productoBO, pedidoProgramadoBO, pedidoExpressBO, true).setVisible(true);
        });
    }

    private JButton crearBotonRectangular(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (bg.equals(Color.WHITE)) {
            btn.setBorder(BorderFactory.createLineBorder(fg, 2));
        } else {
            btn.setBorder(BorderFactory.createEmptyBorder());
        }
        
        return btn;
    }

    private void iniciarSesion() {
        try {
            UsuarioDTO u = usuarioBO.autenticar(txtUsuario.getText(), new String(txtPassword.getPassword()));
            Sesion.iniciarSesion(u);
            dispose();

            if (u.getRol().equalsIgnoreCase("Empleado")) {
                new FrmPanelEmpleado(usuarioBO, productoBO, pedidoExpressBO, pedidoProgramadoBO).setVisible(true);
            } else {
                new FrmMenuCliente(usuarioBO, productoBO, pedidoExpressBO, pedidoProgramadoBO).setVisible(true);
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}