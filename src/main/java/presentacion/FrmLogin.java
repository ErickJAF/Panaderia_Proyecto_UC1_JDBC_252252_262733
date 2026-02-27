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
        setSize(380, 520);
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

        gbc.gridy = 1;
        container.add(new JLabel("Usuario"), gbc);

        txtUsuario = new JTextField();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        container.add(txtUsuario, gbc);

        gbc.gridy = 3;
        container.add(new JLabel("Contraseña"), gbc);

        txtPassword = new JPasswordField();
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 25, 0);
        container.add(txtPassword, gbc);

        JButton btnLogin = new JButton("INGRESAR");
        gbc.gridy = 5;
        container.add(btnLogin, gbc);
        
        // Texto tipo link para registro
JLabel lblRegistro = new JLabel("<HTML><U>No tienes cuenta? Regístrate</U></HTML>");
lblRegistro.setForeground(ACCENT_COLOR);
lblRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));

gbc.gridy = 6;
gbc.insets = new Insets(5, 0, 20, 0);
container.add(lblRegistro, gbc);

        JButton btnExpress = new JButton("PEDIDO EXPRESS");
        gbc.gridy = 7;
        container.add(btnExpress, gbc);

        add(container);

        btnLogin.addActionListener(e -> iniciarSesion());
       

        // 
        btnExpress.addActionListener(e -> {
            dispose();
            new FrmCrearPedido(
                    usuarioBO,
                    productoBO,
                    pedidoProgramadoBO,
                    pedidoExpressBO,
                    true
            ).setVisible(true);
        });
    }

    private void iniciarSesion() {

        try {

            UsuarioDTO u =
                    usuarioBO.autenticar(
                            txtUsuario.getText(),
                            new String(txtPassword.getPassword())
                    );

            Sesion.iniciarSesion(u);
            dispose();

            if (u.getRol().equalsIgnoreCase("Empleado")) {

                new FrmPanelEmpleado(
                        usuarioBO,
                        productoBO,
                        pedidoExpressBO,
                        pedidoProgramadoBO
                ).setVisible(true);

            } else {

                new FrmMenuCliente(
                        usuarioBO,
                        productoBO,
                        pedidoExpressBO,
                        pedidoProgramadoBO
                ).setVisible(true);
            }

        } catch (NegocioException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
}