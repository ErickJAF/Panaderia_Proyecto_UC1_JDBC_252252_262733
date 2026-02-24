package presentacion;

import negocio.BOs.IUsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.IPedidoProgramadoBO;
import negocio.DTOs.UsuarioDTO;
import negocio.BOs.Sesion;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import java.awt.*;

public class FrmLogin extends JFrame {

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

        // Título
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2;

        JLabel titulo = new JLabel("INICIAR SESIÓN");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;

        // Usuario
        gbc.gridy = 1; 
        gbc.gridx = 0;
        panel.add(new JLabel("Usuario:"), gbc);

        txtUsuario = new JTextField();
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        // Password
        gbc.gridy = 2; 
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField();
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Botón login
        JButton btnLogin = new JButton("Ingresar");
        gbc.gridy = 3; 
        gbc.gridx = 0; 
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        // Botones inferiores
        JButton btnVolver = new JButton("Volver");
        gbc.gridy = 4; 
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(btnVolver, gbc);

        JButton btnRegistrar = new JButton("Registrarse");
        gbc.gridx = 1;
        panel.add(btnRegistrar, gbc);

        add(panel);

        // Eventos
        btnLogin.addActionListener(e -> iniciarSesion());

        btnVolver.addActionListener(e -> {
            dispose();
            new FrmInicio(usuarioBO, productoBO, pedidoExpressBO, pedidoProgramadoBO)
                    .setVisible(true);
        });

        btnRegistrar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí irá el registro de cliente");
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

if (usuarioDTO.getRol().equalsIgnoreCase("Empleado")) {

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