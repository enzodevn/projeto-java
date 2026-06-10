import controller.*;
import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class Main {

    static UsuarioController usuarioCtrl = new UsuarioController();
    static ProjetoController projetoCtrl = new ProjetoController();
    static TarefaController  tarefaCtrl  = new TarefaController(projetoCtrl);
    static EquipeController  equipeCtrl  = new EquipeController(usuarioCtrl, projetoCtrl);

    public static void main(String[] args) {
        Tema.aplicarLookAndFeel();
        popularDados();

        SwingUtilities.invokeLater(() -> {
            Usuario logado = mostrarLogin();
            if (logado == null) System.exit(0);
            JanelaPrincipal janela = new JanelaPrincipal(
                    usuarioCtrl, projetoCtrl, tarefaCtrl, equipeCtrl, logado);
            janela.setVisible(true);
        });
    }

    private static Usuario mostrarLogin() {
        final Usuario[] resultado = {null};

        JDialog dialog = new JDialog();
        dialog.setTitle("Login");
        dialog.setModal(true);
        dialog.setSize(400, 420);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridx = 0;

        // Ícone
        JLabel icone = new JLabel("", SwingConstants.CENTER);
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
        g.gridy = 0; g.insets = new Insets(0, 0, 8, 0); painel.add(icone, g);

        // Título
        JLabel tit = new JLabel("Gestão de Projetos", SwingConstants.CENTER);
        tit.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tit.setForeground(Tema.TEXTO_PRINCIPAL);
        g.gridy = 1; g.insets = new Insets(0, 0, 4, 0); painel.add(tit, g);

        JLabel sub = new JLabel("Acesse com suas credenciais", SwingConstants.CENTER);
        sub.setFont(Tema.FONTE_PEQUENA);
        sub.setForeground(Tema.TEXTO_SECUNDARIO);
        g.gridy = 2; g.insets = new Insets(0, 0, 24, 0); painel.add(sub, g);

        // Label Login
        JLabel lLogin = new JLabel("Login");
        lLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lLogin.setForeground(Tema.TEXTO_SECUNDARIO);
        g.gridy = 3; g.insets = new Insets(0, 0, 4, 0); painel.add(lLogin, g);

        JTextField fLogin = Tema.campo();
        fLogin.setPreferredSize(new Dimension(320, 38));
        g.gridy = 4; g.insets = new Insets(0, 0, 12, 0); painel.add(fLogin, g);

        // Label Senha
        JLabel lSenha = new JLabel("Senha");
        lSenha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lSenha.setForeground(Tema.TEXTO_SECUNDARIO);
        g.gridy = 5; g.insets = new Insets(0, 0, 4, 0); painel.add(lSenha, g);

        JPasswordField fSenha = Tema.campaSenha();
        fSenha.setPreferredSize(new Dimension(320, 38));
        g.gridy = 6; g.insets = new Insets(0, 0, 8, 0); painel.add(fSenha, g);

        // Mensagem de erro
        JLabel lblErro = new JLabel(" ", SwingConstants.CENTER);
        lblErro.setFont(Tema.FONTE_PEQUENA);
        lblErro.setForeground(Tema.PERIGO);
        g.gridy = 7; g.insets = new Insets(0, 0, 8, 0); painel.add(lblErro, g);

        // Botão entrar
        JButton btnEntrar = Tema.botaoPrimario("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setPreferredSize(new Dimension(320, 40));
        g.gridy = 8; g.insets = new Insets(0, 0, 0, 0); painel.add(btnEntrar, g);

        // Dica
        JLabel dica = new JLabel("Dica: login 'ana' / senha 'senha123'", SwingConstants.CENTER);
        dica.setFont(Tema.FONTE_PEQUENA);
        dica.setForeground(Tema.TEXTO_SECUNDARIO);
        g.gridy = 9; g.insets = new Insets(16, 0, 0, 0); painel.add(dica, g);

        Runnable tentarLogin = () -> {
            String login = fLogin.getText().trim();
            String senha = new String(fSenha.getPassword());
            if (login.isEmpty() || senha.isEmpty()) { lblErro.setText("Preencha login e senha."); return; }
            Usuario u = usuarioCtrl.autenticar(login, senha);
            if (u == null) { lblErro.setText("Login ou senha inválidos."); fSenha.setText(""); }
            else { resultado[0] = u; dialog.dispose(); }
        };

        btnEntrar.addActionListener(e -> tentarLogin.run());
        fSenha.addActionListener(e -> tentarLogin.run());
        fLogin.addActionListener(e -> fSenha.requestFocus());

        dialog.setContentPane(painel);
        dialog.setVisible(true);
        return resultado[0];
    }

    static void popularDados() {
        usuarioCtrl.cadastrar("Ana Silva",      "95847362948", "ana@email.com",    "Diretora de TI",        "ana",  "senha123", Perfil.ADMINISTRADOR);
        usuarioCtrl.cadastrar("Carlos Souza",   "95857436363", "carlos@email.com", "Gerente de Projetos",   "carlos.ger", "senha123", Perfil.GERENTE);
        usuarioCtrl.cadastrar("Maria Oliveira", "13245234565", "maria@email.com",  "Desenvolvedora Senior", "maria.dev",  "senha123", Perfil.COLABORADOR);
        usuarioCtrl.cadastrar("João Santos",    "34564567111", "joao@email.com",   "Designer UX",           "joao.des",   "senha123", Perfil.COLABORADOR);
        usuarioCtrl.cadastrar("Luciana Reis",   "14623465235", "lu@email.com",     "QA Engineer",           "lu.qa",      "senha123", Perfil.COLABORADOR);

        Usuario gerente = usuarioCtrl.buscarPorId(2);
        Usuario admin   = usuarioCtrl.buscarPorId(1);
        projetoCtrl.cadastrar("Sistema ERP",  "Desenvolvimento do ERP corporativo", LocalDate.of(2026,1,10), LocalDate.of(2026,12,31), gerente);
        projetoCtrl.atualizar(1,"Sistema ERP","Desenvolvimento do ERP corporativo", LocalDate.of(2026,12,31),StatusProjeto.EM_ANDAMENTO,gerente);
        projetoCtrl.cadastrar("App Mobile",   "App mobile para clientes",           LocalDate.of(2026,3,1),  LocalDate.of(2026,9,30),  gerente);
        projetoCtrl.cadastrar("Portal RH",    "Modernização do portal de RH",       LocalDate.of(2026,4,1),  LocalDate.of(2026,8,31),  admin);

        Usuario dev = usuarioCtrl.buscarPorId(3);
        Usuario des = usuarioCtrl.buscarPorId(4);
        Usuario qa  = usuarioCtrl.buscarPorId(5);
        tarefaCtrl.cadastrar("Modelagem do Banco",   "Diagrama ER e tabelas", LocalDate.of(2026,1,10),LocalDate.of(2026,2,10),"ALTA",dev,1);
        tarefaCtrl.cadastrar("Backend API RESET",     "Endpoints Spring Boot", LocalDate.of(2026,2,11),LocalDate.of(2026,6,30),"ALTA",dev,1);
        tarefaCtrl.cadastrar("Design das Telas",     "Prototipação Figma",    LocalDate.of(2026,1,15),LocalDate.of(2026,3,15),"MEDIA",des,1);
        tarefaCtrl.cadastrar("Testes de Integração", "Cobertura >= 80%",      LocalDate.of(2026,5,1), LocalDate.of(2026,7,30),"MEDIA",qa,1);
        tarefaCtrl.cadastrar("Telas iOS",            "Implementação SwiftUI", LocalDate.of(2026,3,5), LocalDate.of(2026,6,30),"ALTA",dev,2);
        tarefaCtrl.cadastrar("Layout Portal RH",     "HTML/CSS responsivo",   LocalDate.of(2026,4,5), LocalDate.of(2026,6,15),"BAIXA",des,3);

        tarefaCtrl.buscarPorId(1).setStatus(StatusTarefa.CONCLUIDA);
        tarefaCtrl.buscarPorId(2).setStatus(StatusTarefa.EM_PROGRESSO);
        tarefaCtrl.buscarPorId(3).setStatus(StatusTarefa.CONCLUIDA);

        equipeCtrl.cadastrar("Equipe Backend","Desenvolvedores back-end Java");
        equipeCtrl.cadastrar("Equipe Design", "Designers e UX");
        equipeCtrl.cadastrar("Equipe QA",     "Qualidade e testes");
        equipeCtrl.adicionarMembro(1,3); equipeCtrl.adicionarMembro(2,4); equipeCtrl.adicionarMembro(3,5);
        equipeCtrl.associarProjeto(1,1); equipeCtrl.associarProjeto(2,1);
        equipeCtrl.associarProjeto(1,2); equipeCtrl.associarProjeto(3,1);
    }
}
