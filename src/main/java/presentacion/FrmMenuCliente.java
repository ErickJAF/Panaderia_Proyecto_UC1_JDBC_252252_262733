package presentacion;

import negocio.DTOs.UsuarioDTO;
import negocio.BOs.IUsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.Sesion;

import javax.swing.*;
import java.awt.*;

public class FrmMenuCliente extends JFrame {

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoExpressBO pedidoExpressBO;

    private JButton btnCrearPedido;
    private JButton btnHistorial;
    private JButton btnPerfil;
    private JButton btnCerrarSesion;

    public FrmMenuCliente(IUsuarioBO usuarioBO,
                          IProductoBO productoBO,
                          IPedidoExpressBO pedidoExpressBO) {

        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.pedidoExpressBO = pedidoExpressBO;

        inicializar();
    }

    private void inicializar() {

        UsuarioDTO usuario = Sesion.getUsuarioActual();

        setTitle("Menú Cliente - " + usuario.getNombreUsuario());
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        btnCrearPedido = crearBoton("Pedido Express");
        btnHistorial = crearBoton("Ver Historial");
        btnPerfil = crearBoton("Editar Perfil");
        btnCerrarSesion = crearBoton("Cerrar Sesión");

        panel.add(btnCrearPedido);
        panel.add(btnHistorial);
        panel.add(btnPerfil);
        panel.add(btnCerrarSesion);

        add(panel, BorderLayout.CENTER);

        // 🔹 Eventos

        btnCrearPedido.addActionListener(e -> {
            new FrmPedidoExpress(productoBO, pedidoExpressBO)
                    .setVisible(true);
        });

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(0,123,255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void cerrarSesion() {

        Sesion.cerrarSesion();
        dispose();

        new FrmLogin(usuarioBO, productoBO, pedidoExpressBO)
                .setVisible(true);
    }
}