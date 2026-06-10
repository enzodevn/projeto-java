package view;

import controller.ProjetoController;
import controller.UsuarioController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PainelProjetos extends JPanel {
    private ProjetoController projetoCtrl;
    private UsuarioController usuarioCtrl;
    private DefaultTableModel modeloTabela;
    private JTable tabela;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelProjetos(ProjetoController pc, UsuarioController uc) {
        this.projetoCtrl = pc; this.usuarioCtrl = uc;
        setBackground(Tema.BG_PRINCIPAL);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        construir();
    }

    private void construir() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Tema.BG_PRINCIPAL);
        topo.add(Tema.titulo("Projetos"), BorderLayout.WEST);
        JButton btnNovo = Tema.botaoPrimario("+ Novo Projeto");
        btnNovo.addActionListener(e -> abrirFormulario(null));
        topo.add(btnNovo, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Status", "Gerente", "Início", "Término Prev."};
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
        for (Projeto p : projetoCtrl.listarTodos())
            modeloTabela.addRow(new Object[]{
                p.getId(), p.getNome(), p.getStatus(),
                p.getGerente() != null ? p.getGerente().getNome() : "—",
                p.getDataInicio() != null ? p.getDataInicio().format(FMT) : "—",
                p.getDataTerminoPrevista() != null ? p.getDataTerminoPrevista().format(FMT) : "—",
                String.format("%.0f%%", p.getPercentualConclusao())});
    }

    private void abrirFormulario(Projeto projeto) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                projeto == null ? "Novo Projeto" : "Editar Projeto", true);
        dialog.setSize(460, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1; g.gridx = 0;

        JTextField fNome    = Tema.campo();
        JTextField fDesc    = Tema.campo();
        JTextField fInicio  = Tema.campo();
        JTextField fTermino = Tema.campo();
        JComboBox<StatusProjeto> cbStatus = Tema.combo();
        for (StatusProjeto s : StatusProjeto.values()) cbStatus.addItem(s);
        JComboBox<Usuario> cbGerente = Tema.combo();
        usuarioCtrl.listarTodos().stream()
            .filter(u -> u.getPerfil() == Perfil.GERENTE || u.getPerfil() == Perfil.ADMINISTRADOR)
            .forEach(cbGerente::addItem);

        if (projeto != null) {
            fNome.setText(projeto.getNome());
            fDesc.setText(projeto.getDescricao());
            if (projeto.getDataInicio() != null) fInicio.setText(projeto.getDataInicio().format(FMT));
            if (projeto.getDataTerminoPrevista() != null) fTermino.setText(projeto.getDataTerminoPrevista().format(FMT));
            cbStatus.setSelectedItem(projeto.getStatus());
            if (projeto.getGerente() != null) cbGerente.setSelectedItem(projeto.getGerente());
        }

        int row = 0;
        addRow(painel, g, "Nome do Projeto *", fNome, row++);
        addRow(painel, g, "Descrição", fDesc, row++);
        addRow(painel, g, "Data de Início (dd/MM/yyyy)", fInicio, row++);
        addRow(painel, g, "Término Previsto (dd/MM/yyyy)", fTermino, row++);
        addRow(painel, g, "Status", cbStatus, row++);
        addRow(painel, g, "Gerente Responsável *", cbGerente, row++);

        JButton btnSalvar = Tema.botaoPrimario(projeto == null ? "Cadastrar Projeto" : "Salvar Alterações");
        g.gridy = row * 2 + 1; g.insets = new Insets(20, 0, 0, 0);
        painel.add(btnSalvar, g);

        btnSalvar.addActionListener(e -> {
            String nome = fNome.getText().trim();
            if (nome.isEmpty()) { Tema.erro(dialog, "Informe o nome do projeto."); return; }
            LocalDate inicio = null, termino = null;
            try {
                if (!fInicio.getText().isBlank()) inicio = LocalDate.parse(fInicio.getText().trim(), FMT);
                if (!fTermino.getText().isBlank()) termino = LocalDate.parse(fTermino.getText().trim(), FMT);
            } catch (DateTimeParseException ex) { Tema.erro(dialog, "Data inválida. Use dd/MM/yyyy."); return; }
            StatusProjeto status = (StatusProjeto) cbStatus.getSelectedItem();
            Usuario gerente = (Usuario) cbGerente.getSelectedItem();
            String err = projeto == null
                    ? projetoCtrl.cadastrar(nome, fDesc.getText().trim(), inicio, termino, gerente)
                    : projetoCtrl.atualizar(projeto.getId(), nome, fDesc.getText().trim(), termino, status, gerente);
            if (err != null) { Tema.erro(dialog, err); return; }
            Tema.sucesso(dialog, "Projeto salvo!");
            atualizar(); dialog.dispose();
        });

        dialog.setContentPane(new JScrollPane(painel));
        dialog.setVisible(true);
    }

    private void addRow(JPanel p, GridBagConstraints g, String lbl, JComponent campo, int row) {
        JLabel l = new JLabel(lbl);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Tema.TEXTO_SECUNDARIO);
        g.gridy = row * 2; g.insets = new Insets(row == 0 ? 0 : 14, 0, 4, 0);
        p.add(l, g);
        campo.setPreferredSize(new Dimension(400, 36));
        g.gridy = row * 2 + 1; g.insets = new Insets(0, 0, 0, 0);
        p.add(campo, g);
    }

    private void editarSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione um projeto."); return; }
        abrirFormulario(projetoCtrl.buscarPorId((int) modeloTabela.getValueAt(row, 0)));
    }
    private void removerSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione um projeto."); return; }
        if (!Tema.confirmar(this, "Remover projeto selecionado?")) return;
        String err = projetoCtrl.remover((int) modeloTabela.getValueAt(row, 0));
        if (err != null) { Tema.erro(this, err); return; }
        atualizar();
    }
}
