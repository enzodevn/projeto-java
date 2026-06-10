package view;

import controller.*;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JanelaPrincipal extends JFrame {
    private PainelUsuarios   painelUsuarios;
    private PainelProjetos   painelProjetos;
    private PainelTarefas    painelTarefas;
    private PainelEquipes    painelEquipes;
    private PainelRelatorios painelRelatorios;
    private JPanel painelConteudo;
    private CardLayout cardLayout;
    private Usuario usuarioLogado;
    private JButton btnAtivo = null;

    public JanelaPrincipal(UsuarioController uc, ProjetoController pc,
                           TarefaController tc, EquipeController ec, Usuario logado) {
        this.usuarioLogado = logado;
        painelUsuarios   = new PainelUsuarios(uc);
        painelProjetos   = new PainelProjetos(pc, uc);
        painelTarefas    = new PainelTarefas(tc, pc, uc);
        painelEquipes    = new PainelEquipes(ec, uc, pc);
        painelRelatorios = new PainelRelatorios(pc, tc, ec, uc);
        configurar();
    }

    private void configurar() {
        setTitle("Sistema de Gestão de Projetos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setBackground(Tema.BG_PRINCIPAL);
        painelConteudo.add(painelProjetos,   "projetos");
        painelConteudo.add(painelTarefas,    "tarefas");
        painelConteudo.add(painelEquipes,    "equipes");
        painelConteudo.add(painelUsuarios,   "usuarios");
        painelConteudo.add(painelRelatorios, "relatorios");

        JPanel sidebar = criarSidebar();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(painelConteudo, BorderLayout.CENTER);

        navegarPara("projetos", null);
    }

    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Tema.BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));

        // Logo
        JLabel lblLogo = new JLabel(" TRABALHO A3 ");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(22, 16, 22, 16));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblLogo);
        sidebar.add(separador());

        // Info usuário
        JLabel lblNome = new JLabel("  " + usuarioLogado.getNome().split(" ")[0]);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNome.setForeground(new Color(147, 197, 253));
        lblNome.setBorder(BorderFactory.createEmptyBorder(12, 16, 2, 16));
        lblNome.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblPerfil = new JLabel("  " + usuarioLogado.getPerfil().toString());
        lblPerfil.setFont(Tema.FONTE_PEQUENA);
        lblPerfil.setForeground(new Color(148, 163, 184));
        lblPerfil.setBorder(BorderFactory.createEmptyBorder(0, 16, 14, 16));
        lblPerfil.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblNome);
        sidebar.add(lblPerfil);
        sidebar.add(separador());
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        // Itens de menu – guardamos referência do primeiro para marcar como ativo
        JButton[] btnProjetos   = {null};
        btnProjetos[0]          = itemMenu("-", "Projetos",   "projetos");
        JButton btnTarefas      = itemMenu("-", "Tarefas",    "tarefas");
        JButton btnEquipes      = itemMenu("-", "Equipes",    "equipes");
        JButton btnUsuarios     = itemMenu("-", "Usuários",   "usuarios");
        JButton btnRelatorios   = itemMenu("-", "Relatórios", "relatorios");

        // Wiring: cada botão sabe quem é e pode desativar os outros
        JButton[] todos = {btnProjetos[0], btnTarefas, btnEquipes, btnUsuarios, btnRelatorios};
        String[]  ids   = {"projetos",     "tarefas",  "equipes",  "usuarios",  "relatorios"};

        for (int i = 0; i < todos.length; i++) {
            final JButton btn = todos[i];
            final String id   = ids[i];
            btn.addActionListener(e -> navegarPara(id, btn));
        }

        sidebar.add(btnProjetos[0]);
        sidebar.add(btnTarefas);
        sidebar.add(btnEquipes);
        sidebar.add(btnUsuarios);
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(separador());
        sidebar.add(btnRelatorios);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(separador());

        // Botão sair
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(Tema.FONTE_NORMAL);
        btnSair.setForeground(new Color(148, 163, 184));
        btnSair.setBackground(Tema.BG_SIDEBAR);
        btnSair.setBorderPainted(false);
        btnSair.setFocusPainted(false);
        btnSair.setOpaque(true);
        btnSair.setHorizontalAlignment(SwingConstants.LEFT);
        btnSair.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSair.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnSair.setBorder(BorderFactory.createEmptyBorder(10, 16, 14, 16));
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.addActionListener(e -> {
            if (Tema.confirmar(this, "Deseja sair do sistema?")) System.exit(0);
        });
        sidebar.add(btnSair);

        return sidebar;
    }

    private JButton itemMenu(String icone, String label, String painelId) {
        JButton btn = new JButton("  " + icone + "  " + label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(Tema.BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != btnAtivo) btn.setBackground(new Color(51, 65, 85));
            }
            public void mouseExited(MouseEvent e) {
                if (btn != btnAtivo) btn.setBackground(Tema.BG_SIDEBAR);
            }
        });
        return btn;
    }

    private JComponent separador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(51, 65, 85));
        sep.setBackground(Tema.BG_SIDEBAR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private void navegarPara(String painel, JButton btn) {
        // Desativa botão anterior
        if (btnAtivo != null) {
            btnAtivo.setBackground(Tema.BG_SIDEBAR);
            btnAtivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btnAtivo.setForeground(new Color(203, 213, 225));
        }
        // Ativa novo botão
        if (btn != null) {
            btn.setBackground(new Color(37, 99, 235));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btnAtivo = btn;
        }
        // Atualiza dados e exibe
        switch (painel) {
            case "usuarios"   -> painelUsuarios.atualizar();
            case "projetos"   -> painelProjetos.atualizar();
            case "tarefas"    -> painelTarefas.atualizar();
            case "equipes"    -> painelEquipes.atualizar();
        }
        cardLayout.show(painelConteudo, painel);
    }
}
