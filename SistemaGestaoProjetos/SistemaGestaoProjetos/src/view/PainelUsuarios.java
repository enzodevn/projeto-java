package view;

import controller.UsuarioController;
import model.Perfil;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelUsuarios extends JPanel {
    private UsuarioController ctrl;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public PainelUsuarios(UsuarioController ctrl) {
        this.ctrl = ctrl;
        setBackground(Tema.BG_PRINCIPAL);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        construir();
    }

    private void construir() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Tema.BG_PRINCIPAL);
        topo.add(Tema.titulo("👤  Usuários"), BorderLayout.WEST);
        JButton btnNovo = Tema.botaoPrimario("+ Novo Usuário");
        btnNovo.addActionListener(e -> abrirFormulario(null));
        topo.add(btnNovo, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "CPF", "E-mail", "Cargo", "Login", "Perfil"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = Tema.tabela(colunas, new Object[0][0]);
        tabela.setModel(modeloTabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        add(Tema.scroll(tabela), BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rodape.setBackground(Tema.BG_PRINCIPAL);
        JButton btnEditar  = Tema.botaoSecundario("Editar");
        JButton btnRemover = Tema.botaoPerigo("Remover");
        btnEditar.addActionListener(e -> editarSelecionado());
        btnRemover.addActionListener(e -> removerSelecionado());
        rodape.add(btnEditar); rodape.add(btnRemover);
        add(rodape, BorderLayout.SOUTH);
        atualizar();
    }

    public void atualizar() {
        modeloTabela.setRowCount(0);
        for (Usuario u : ctrl.listarTodos())
            modeloTabela.addRow(new Object[]{
                u.getId(), u.getNome(), u.getCpf(), u.getEmail(),
                u.getCargo(), u.getLogin(), u.getPerfil()});
    }

    private void abrirFormulario(Usuario usuario) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                usuario == null ? "Novo Usuário" : "Editar Usuário", true);
        dialog.setSize(460, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1; g.gridx = 0; g.gridwidth = 1;

        JTextField fNome  = Tema.campo();
        JTextField fCpf   = Tema.campo();
        JTextField fEmail = Tema.campo();
        JTextField fCargo = Tema.campo();
        JTextField fLogin = Tema.campo();
        JPasswordField fSenha = Tema.campaSenha();
        JComboBox<Perfil> cbPerfil = Tema.combo();
        for (Perfil p : Perfil.values()) cbPerfil.addItem(p);

        if (usuario != null) {
            fNome.setText(usuario.getNome());
            fCpf.setText(usuario.getCpf()); fCpf.setEditable(false); fCpf.setBackground(Tema.LINHA_TABELA);
            fEmail.setText(usuario.getEmail());
            fCargo.setText(usuario.getCargo());
            fLogin.setText(usuario.getLogin()); fLogin.setEditable(false); fLogin.setBackground(Tema.LINHA_TABELA);
            fSenha.setText(usuario.getSenha());
            cbPerfil.setSelectedItem(usuario.getPerfil());
        }

        int row = 0;
        addRow(painel, g, "Nome Completo *", fNome,  row++);
        addRow(painel, g, "CPF *",           fCpf,   row++);
        addRow(painel, g, "E-mail *",        fEmail, row++);
        addRow(painel, g, "Cargo *",         fCargo, row++);
        addRow(painel, g, "Login *",         fLogin, row++);
        addRow(painel, g, "Senha *",         fSenha, row++);
        addRow(painel, g, "Perfil",          cbPerfil, row++);

        JButton btnSalvar = Tema.botaoPrimario(usuario == null ? "Cadastrar" : "Salvar Alterações");
        g.gridy = row * 2 + 1; g.insets = new Insets(20, 0, 0, 0);
        painel.add(btnSalvar, g);

        btnSalvar.addActionListener(e -> {
            String nome  = fNome.getText().trim();
            String email = fEmail.getText().trim();
            String cargo = fCargo.getText().trim();
            if (nome.isEmpty() || email.isEmpty() || cargo.isEmpty()) {
                Tema.erro(dialog, "Preencha todos os campos obrigatórios (*)."); return;
            }
            Perfil perfil = (Perfil) cbPerfil.getSelectedItem();
            String err;
            if (usuario == null) {
                String cpf = fCpf.getText().trim(), login = fLogin.getText().trim();
                String senha = new String(fSenha.getPassword()).trim();
                if (cpf.isEmpty() || login.isEmpty() || senha.isEmpty()) {
                    Tema.erro(dialog, "Preencha todos os campos."); return;
                }
                err = ctrl.cadastrar(nome, cpf, email, cargo, login, senha, perfil);
            } else {
                err = ctrl.atualizar(usuario.getId(), nome, email, cargo, perfil);
            }
            if (err != null) { Tema.erro(dialog, err); return; }
            Tema.sucesso(dialog, "Usuário salvo com sucesso!");
            atualizar(); dialog.dispose();
        });

        dialog.setContentPane(new JScrollPane(painel));
        dialog.setVisible(true);
    }

    private void addRow(JPanel painel, GridBagConstraints g, String labelTxt, JComponent campo, int row) {
        JLabel lbl = new JLabel(labelTxt);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Tema.TEXTO_SECUNDARIO);
        g.gridy = row * 2; g.insets = new Insets(row == 0 ? 0 : 14, 0, 4, 0);
        painel.add(lbl, g);
        campo.setPreferredSize(new Dimension(400, 36));
        g.gridy = row * 2 + 1; g.insets = new Insets(0, 0, 0, 0);
        painel.add(campo, g);
    }

    private void editarSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione um usuário."); return; }
        abrirFormulario(ctrl.buscarPorId((int) modeloTabela.getValueAt(row, 0)));
    }

    private void removerSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione um usuário."); return; }
        if (!Tema.confirmar(this, "Remover usuário selecionado?")) return;
        String err = ctrl.remover((int) modeloTabela.getValueAt(row, 0));
        if (err != null) { Tema.erro(this, err); return; }
        atualizar();
    }
}
