package view;

import controller.UsuarioController;
import model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaLogin extends JFrame {
    private UsuarioController usuarioCtrl;
    private JTextField campoLogin;
    private JPasswordField campoSenha;

    public TelaLogin(UsuarioController usuarioCtrl) {
        this.usuarioCtrl = usuarioCtrl;
        configurar();
    }

    private void configurar() {
        setTitle("Sistema de Gestão de Projetos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(440, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Tema.BG_PRINCIPAL);
        painel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 0, 6, 0);
        g.gridx = 0; g.weightx = 1;

        // Logo / ícone
        JLabel icone = new JLabel("📋", SwingConstants.CENTER);
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        g.gridy = 0; painel.add(icone, g);

        // Título
        JLabel tit = Tema.titulo("Gestão de Projetos");
        tit.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 1; painel.add(tit, g);

        // Subtítulo
        JLabel sub = Tema.label("Faça login para continuar");
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 2; g.insets = new Insets(0, 0, 24, 0);
        painel.add(sub, g);
        g.insets = new Insets(6, 0, 6, 0);

        // Campo login
        g.gridy = 3; painel.add(Tema.label("Login"), g);
        campoLogin = Tema.campo();
        g.gridy = 4; painel.add(campoLogin, g);

        // Campo senha
        g.gridy = 5; painel.add(Tema.label("Senha"), g);
        campoSenha = Tema.campaSenha();
        g.gridy = 6; painel.add(campoSenha, g);

        // Botão entrar
        JButton btnEntrar = Tema.botaoPrimario("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g.gridy = 7; g.insets = new Insets(20, 0, 6, 0);
        painel.add(btnEntrar, g);

        btnEntrar.addActionListener(e -> login());
        campoSenha.addActionListener(e -> login());
        campoLogin.addActionListener(e -> campoSenha.requestFocus());

        // Rodapé
        JLabel rod = Tema.label("A3 – Programação de Soluções Computacionais");
        rod.setHorizontalAlignment(SwingConstants.CENTER);
        rod.setFont(Tema.FONTE_PEQUENA);
        g.gridy = 8; g.insets = new Insets(30, 0, 0, 0);
        painel.add(rod, g);

        setContentPane(painel);
    }

    private void login() {
        String login = campoLogin.getText().trim();
        String senha = new String(campoSenha.getPassword());
        if (login.isEmpty() || senha.isEmpty()) {
            Tema.erro(this, "Preencha login e senha.");
            return;
        }
        Usuario u = usuarioCtrl.autenticar(login, senha);
        if (u == null) {
            Tema.erro(this, "Login ou senha inválidos.");
            campoSenha.setText("");
            return;
        }
        dispose();
    }

    public static Usuario exibir(UsuarioController uc) {
        // Loop até autenticar
        final Usuario[] result = {null};
        while (result[0] == null) {
            TelaLogin tela = new TelaLogin(uc);
            tela.setVisible(true);
            // Tenta autenticar com o que foi digitado (a tela já faz dispose ao autenticar)
            String login = tela.campoLogin.getText().trim();
            String senha = new String(tela.campoSenha.getPassword());
            result[0] = uc.autenticar(login, senha);
        }
        return result[0];
    }
}
