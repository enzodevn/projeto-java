package view;

import controller.EquipeController;
import controller.ProjetoController;
import controller.UsuarioController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelEquipes extends JPanel {
    private EquipeController equipeCtrl;
    private UsuarioController usuarioCtrl;
    private ProjetoController projetoCtrl;
    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public PainelEquipes(EquipeController ec, UsuarioController uc, ProjetoController pc) {
        this.equipeCtrl = ec; this.usuarioCtrl = uc; this.projetoCtrl = pc;
        setBackground(Tema.BG_PRINCIPAL);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        construir();
    }

    private void construir() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Tema.BG_PRINCIPAL);
        topo.add(Tema.titulo(" Equipes"), BorderLayout.WEST);
        JButton btnNovo = Tema.botaoPrimario("+ Nova Equipe");
        btnNovo.addActionListener(e -> abrirFormulario(null));
        topo.add(btnNovo, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Descrição", "Membros", "Projetos"};
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
        JButton btnMembros = Tema.botaoSecundario("Membros");
        JButton btnAlocar  = Tema.botaoSecundario("Alocar Projeto");
        JButton btnRemover = Tema.botaoPerigo("Remover");
        btnEditar.addActionListener(e -> editarSelecionado());
        btnMembros.addActionListener(e -> gerenciarMembros());
        btnAlocar.addActionListener(e -> alocarProjeto());
        btnRemover.addActionListener(e -> removerSelecionado());
        rodape.add(btnEditar); rodape.add(btnMembros); rodape.add(btnAlocar); rodape.add(btnRemover);
        add(rodape, BorderLayout.SOUTH);
        atualizar();
    }

    public void atualizar() {
        modeloTabela.setRowCount(0);
        for (Equipe e : equipeCtrl.listarTodas())
            modeloTabela.addRow(new Object[]{
                e.getId(), e.getNome(), e.getDescricao(),
                e.getMembros().size() + " membro(s)",
                e.getIdsProjetos().size() + " projeto(s)"});
    }

    private void abrirFormulario(Equipe equipe) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                equipe == null ? "Nova Equipe" : "Editar Equipe", true);
        dialog.setSize(420, 240);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridx = 0;

        JTextField fNome = Tema.campo();
        JTextField fDesc = Tema.campo();
        if (equipe != null) { fNome.setText(equipe.getNome()); fDesc.setText(equipe.getDescricao()); }

        addRow(painel, g, "Nome da Equipe ", fNome, 0);
        addRow(painel, g, "Descrição",        fDesc, 1);

        JButton btnSalvar = Tema.botaoPrimario(equipe == null ? "Cadastrar" : "Salvar");
        g.gridy = 5; g.insets = new Insets(20, 0, 0, 0);
        painel.add(btnSalvar, g);

        btnSalvar.addActionListener(e -> {
            String nome = fNome.getText().trim();
            if (nome.isEmpty()) { Tema.erro(dialog, "Informe o nome."); return; }
            String err = equipe == null
                    ? equipeCtrl.cadastrar(nome, fDesc.getText().trim())
                    : equipeCtrl.atualizar(equipe.getId(), nome, fDesc.getText().trim());
            if (err != null) { Tema.erro(dialog, err); return; }
            Tema.sucesso(dialog, "Equipe salva!");
            atualizar(); dialog.dispose();
        });

        dialog.setContentPane(painel);
        dialog.setVisible(true);
    }

    private void addRow(JPanel p, GridBagConstraints g, String lbl, JComponent campo, int row) {
        JLabel l = new JLabel(lbl);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Tema.TEXTO_SECUNDARIO);
        g.gridy = row * 2; g.insets = new Insets(row == 0 ? 0 : 14, 0, 4, 0);
        p.add(l, g);
        campo.setPreferredSize(new Dimension(360, 36));
        g.gridy = row * 2 + 1; g.insets = new Insets(0, 0, 0, 0);
        p.add(campo, g);
    }

    private void gerenciarMembros() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione uma equipe."); return; }
        Equipe equipe = equipeCtrl.buscarPorId((int) modeloTabela.getValueAt(row, 0));

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Membros – " + equipe.getNome(), true);
        dialog.setSize(520, 380);
        dialog.setLocationRelativeTo(this);

        JPanel painel = new JPanel(new BorderLayout(16, 12));
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        DefaultListModel<Usuario> modeloMembros = new DefaultListModel<>();
        equipe.getMembros().forEach(modeloMembros::addElement);
        JList<Usuario> listaMembros = new JList<>(modeloMembros);
        listaMembros.setBackground(Tema.BG_INPUT);
        listaMembros.setForeground(Tema.TEXTO_PRINCIPAL);
        listaMembros.setFont(Tema.FONTE_NORMAL);
        listaMembros.setSelectionBackground(Tema.ACCENT);
        listaMembros.setSelectionForeground(Color.WHITE);
        listaMembros.setFixedCellHeight(30);

        JPanel esq = new JPanel(new BorderLayout(0, 8));
        esq.setBackground(Tema.BG_CARD);
        JLabel lMembros = new JLabel("Membros atuais");
        lMembros.setFont(Tema.FONTE_SECAO);
        lMembros.setForeground(Tema.TEXTO_PRINCIPAL);
        esq.add(lMembros, BorderLayout.NORTH);
        esq.add(Tema.scroll(listaMembros), BorderLayout.CENTER);
        JButton btnRem = Tema.botaoPerigo("Remover selecionado");
        btnRem.addActionListener(e -> {
            Usuario sel = listaMembros.getSelectedValue();
            if (sel == null) return;
            equipeCtrl.removerMembro(equipe.getId(), sel.getId());
            modeloMembros.removeElement(sel);
            atualizar();
        });
        esq.add(btnRem, BorderLayout.SOUTH);
        painel.add(esq, BorderLayout.WEST);

        JPanel dir = new JPanel(new BorderLayout(0, 8));
        dir.setBackground(Tema.BG_CARD);
        JLabel lAdicionar = new JLabel("Adicionar usuário");
        lAdicionar.setFont(Tema.FONTE_SECAO);
        lAdicionar.setForeground(Tema.TEXTO_PRINCIPAL);
        dir.add(lAdicionar, BorderLayout.NORTH);

        JComboBox<Usuario> cbUsuarios = Tema.combo();
        usuarioCtrl.listarTodos().forEach(cbUsuarios::addItem);
        dir.add(cbUsuarios, BorderLayout.CENTER);

        JButton btnAdd = Tema.botaoPrimario("+ Adicionar");
        btnAdd.addActionListener(e -> {
            Usuario u = (Usuario) cbUsuarios.getSelectedItem();
            if (u == null) return;
            String err = equipeCtrl.adicionarMembro(equipe.getId(), u.getId());
            if (err != null) { Tema.erro(dialog, err); return; }
            if (!modeloMembros.contains(u)) modeloMembros.addElement(u);
            atualizar();
        });
        dir.add(btnAdd, BorderLayout.SOUTH);
        painel.add(dir, BorderLayout.CENTER);

        dialog.setContentPane(painel);
        dialog.setVisible(true);
    }

    private void alocarProjeto() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione uma equipe."); return; }
        int idEquipe = (int) modeloTabela.getValueAt(row, 0);
        Equipe equipe = equipeCtrl.buscarPorId(idEquipe);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Alocar em Projeto", true);
        dialog.setSize(400, 220);
        dialog.setLocationRelativeTo(this);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridx = 0;

        JLabel lEquipe = new JLabel("Equipe: " + equipe.getNome());
        lEquipe.setFont(Tema.FONTE_SECAO); lEquipe.setForeground(Tema.TEXTO_PRINCIPAL);
        g.gridy = 0; g.insets = new Insets(0, 0, 12, 0); painel.add(lEquipe, g);

        JLabel lProjeto = new JLabel("Selecione o projeto:");
        lProjeto.setFont(new Font("Segoe UI", Font.BOLD, 12)); lProjeto.setForeground(Tema.TEXTO_SECUNDARIO);
        g.gridy = 1; g.insets = new Insets(0, 0, 4, 0); painel.add(lProjeto, g);

        JComboBox<Projeto> cbProjeto = Tema.combo();
        projetoCtrl.listarTodos().forEach(cbProjeto::addItem);
        cbProjeto.setPreferredSize(new Dimension(350, 36));
        g.gridy = 2; g.insets = new Insets(0, 0, 0, 0); painel.add(cbProjeto, g);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        botoes.setBackground(Tema.BG_CARD);
        JButton btnAssociar    = Tema.botaoPrimario("Associar");
        JButton btnDesassociar = Tema.botaoPerigo("Desassociar");
        btnAssociar.addActionListener(e -> {
            Projeto p = (Projeto) cbProjeto.getSelectedItem();
            if (p == null) return;
            String err = equipeCtrl.associarProjeto(idEquipe, p.getId());
            if (err != null) { Tema.erro(dialog, err); return; }
            Tema.sucesso(dialog, "Equipe associada ao projeto!");
            atualizar(); dialog.dispose();
        });
        btnDesassociar.addActionListener(e -> {
            Projeto p = (Projeto) cbProjeto.getSelectedItem();
            if (p == null) return;
            equipeCtrl.desassociarProjeto(idEquipe, p.getId());
            Tema.sucesso(dialog, "Equipe desassociada.");
            atualizar(); dialog.dispose();
        });
        botoes.add(btnAssociar); botoes.add(btnDesassociar);
        g.gridy = 3; g.insets = new Insets(16, 0, 0, 0); painel.add(botoes, g);

        dialog.setContentPane(painel);
        dialog.setVisible(true);
    }

    private void editarSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione uma equipe."); return; }
        abrirFormulario(equipeCtrl.buscarPorId((int) modeloTabela.getValueAt(row, 0)));
    }
    private void removerSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione uma equipe."); return; }
        if (!Tema.confirmar(this, "Remover equipe?")) return;
        String err = equipeCtrl.remover((int) modeloTabela.getValueAt(row, 0));
        if (err != null) { Tema.erro(this, err); return; }
        atualizar();
    }
}
