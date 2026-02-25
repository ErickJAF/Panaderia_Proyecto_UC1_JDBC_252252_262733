package presentacion;

import negocio.BOs.*;
import negocio.DTOs.UsuarioDTO;
import negocio.excepciones.NegocioException;
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
        setSize(380, 520); // Tamaño más ajustado
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
    }

    private void inicializarComponentes() {
        setLayout(new GridBagLayout());
        
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(10, 40, 10, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(ACCENT_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 25, 0);
        container.add(lblTitulo, gbc);
        
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(TEXT_COLOR);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 4, 0);
        container.add(lblUser, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(0, 35));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(txtUsuario, gbc);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPass.setForeground(TEXT_COLOR);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 4, 0);
        container.add(lblPass, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(0, 35));
        txtPassword.putClientProperty("JTextField.showRevealButton", true);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 25, 0);
        container.add(txtPassword, gbc);

        JButton btnLogin = new JButton("INGRESAR");
        estilizarBoton(btnLogin, ACCENT_COLOR, Color.WHITE);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 10, 0);
        container.add(btnLogin, gbc);

        JButton btnExpress = new JButton("PEDIDO EXPRESS");
        estilizarBoton(btnExpress, Color.WHITE, new Color(230, 81, 0));
        btnExpress.setBorder(BorderFactory.createLineBorder(new Color(230, 81, 0), 1));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        container.add(btnExpress, gbc);

        JSeparator sep = new JSeparator();
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(sep, gbc);

        JButton btnRegistrar = new JButton("REGISTRARSE");
        estilizarBoton(btnRegistrar, Color.WHITE, ACCENT_COLOR);
        btnRegistrar.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        container.add(btnRegistrar, gbc);

        add(container);

        btnLogin.addActionListener(e -> iniciarSesion());
        btnExpress.addActionListener(e -> {
            dispose();
            new FrmPedidoExpress(productoBO, pedidoExpressBO).setVisible(true);
        });
        btnRegistrar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abriendo registro...");
        });
    }

    private void estilizarBoton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(0, 40));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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