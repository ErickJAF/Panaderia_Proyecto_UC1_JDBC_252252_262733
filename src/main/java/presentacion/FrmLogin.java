package presentacion;

import negocio.BOs.IUsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.DTOs.UsuarioDTO;
import negocio.BOs.Sesion;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import java.awt.*;

public class FrmLogin extends JFrame {

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoExpressBO pedidoExpressBO;

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public FrmLogin(IUsuarioBO usuarioBO,
                    IProductoBO productoBO,
                    IPedidoExpressBO pedidoExpressBO) {

        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.pedidoExpressBO = pedidoExpressBO;

        inicializar();
    }

    private void inicializar() {

        setTitle("Iniciar Sesión - Sistema Panadería");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("INICIAR SESIÓN");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("Usuario:"), gbc);

        txtUsuario = new JTextField();
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField();
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Ingresar");
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        JButton btnVolver = new JButton("Volver");
        gbc.gridy = 4; gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(btnVolver, gbc);

        JButton btnRegistrar = new JButton("Registrarse");
        gbc.gridx = 1;
        panel.add(btnRegistrar, gbc);

        add(panel);

        btnLogin.addActionListener(e -> iniciarSesion());

        btnVolver.addActionListener(e -> {
            dispose();
            new FrmInicio(usuarioBO, productoBO, pedidoExpressBO)
                    .setVisible(true);
        });
    }

    private void iniciarSesion() {

        try {

            UsuarioDTO usuarioDTO =
                    usuarioBO.autenticar(
                            txtUsuario.getText(),
                            new String(txtPassword.getPassword()));

            Sesion.iniciarSesion(usuarioDTO);

            dispose();

            if (usuarioDTO.getRol().equals("Empleado")) {
                new FrmPanelEmpleado(usuarioBO, productoBO, pedidoExpressBO)
                        .setVisible(true);
            } else {
                new FrmMenuCliente(usuarioBO, productoBO, pedidoExpressBO)
                        .setVisible(true);
            }

        } catch (NegocioException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}