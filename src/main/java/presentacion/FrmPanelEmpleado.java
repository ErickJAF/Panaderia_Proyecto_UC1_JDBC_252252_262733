package presentacion;

import negocio.BOs.Sesion;
import negocio.DTOs.UsuarioDTO;
import negocio.BOs.IUsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoExpressBO;

import javax.swing.*;
import java.awt.*;

public class FrmPanelEmpleado extends JFrame {

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoExpressBO pedidoExpressBO;

    private JButton btnPendientes;
    private JButton btnBuscar;
    private JButton btnEntregaPago;
    private JButton btnCerrarSesion;

    public FrmPanelEmpleado(IUsuarioBO usuarioBO,
                            IProductoBO productoBO,
                            IPedidoExpressBO pedidoExpressBO) {

        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.pedidoExpressBO = pedidoExpressBO;

        inicializar();
    }

    private void inicializar() {

        UsuarioDTO usuario = Sesion.getUsuarioActual();

        setTitle("Panel Empleado - " + usuario.getNombreUsuario());
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        btnPendientes = crearBoton("Ver Pedidos Pendientes");
        btnBuscar = crearBoton("Buscar Pedido");
        btnEntregaPago = crearBoton("Registrar Entrega y Pago");
        btnCerrarSesion = crearBoton("Cerrar Sesión");

        panel.add(btnPendientes);
        panel.add(btnBuscar);
        panel.add(btnEntregaPago);
        panel.add(btnCerrarSesion);

        add(panel, BorderLayout.CENTER);

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(40,167,69));
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