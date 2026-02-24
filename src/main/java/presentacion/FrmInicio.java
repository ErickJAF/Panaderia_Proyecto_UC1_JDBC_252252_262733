package presentacion;

import negocio.BOs.IUsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.IPedidoProgramadoBO;

import javax.swing.*;
import java.awt.*;

public class FrmInicio extends JFrame {

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoExpressBO pedidoExpressBO;
    private final IPedidoProgramadoBO pedidoProgramadoBO;

    public FrmInicio(IUsuarioBO usuarioBO,
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

        setTitle("Pansito - Sistema Panadería");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(new Color(210,160,115));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(60,40,60,40));

        JLabel lblTitulo = new JLabel("Pansito");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0,60)));

        JButton btnLogin = crearBoton("Iniciar sesión");
        JButton btnExpress = crearBoton("Pedido Express");

        panel.add(btnLogin);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(btnExpress);

        add(panel);

        // LOGIN
        btnLogin.addActionListener(e -> {
            dispose();
            new FrmLogin(
                    usuarioBO,
                    productoBO,
                    pedidoExpressBO,
                    pedidoProgramadoBO
            ).setVisible(true);
        });

        // PEDIDO EXPRESS (sin login)
        btnExpress.addActionListener(e -> {
            dispose();
            new FrmPedidoExpress(
                    productoBO,
                    pedidoExpressBO
            ).setVisible(true);
        });
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(200,40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(50,50,50));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
}