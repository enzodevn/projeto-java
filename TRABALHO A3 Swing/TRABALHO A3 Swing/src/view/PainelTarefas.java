package view;

import controller.ProjetoController;
import controller.TarefaController;
import controller.UsuarioController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PainelTarefas extends JPanel {
    private TarefaController tarefaCtrl;
    private ProjetoController projetoCtrl;
    private UsuarioController usuarioCtrl;
    private DefaultTableModel modeloTabela;
    private JTable tabela;
    private JComboBox<Projeto> cbFiltro;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelTarefas(TarefaController tc, ProjetoController pc, UsuarioController uc) {
        this.tarefaCtrl = tc; this.projetoCtrl = pc; this.usuarioCtrl = uc;
        setBackground(Tema.BG_PRINCIPAL);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        construir();
    }

    private void construir() {
        JPanel topo = new JPanel(new BorderLayout(16, 0));
        topo.setBackground(Tema.BG_PRINCIPAL);

        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        esq.setBackground(Tema.BG_PRINCIPAL);
        esq.add(Tema.titulo("Tarefas"));
        cbFiltro = Tema.combo();
        cbFiltro.addItem(null);
        projetoCtrl.listarTodos().forEach(cbFiltro::addItem);
        cbFiltro.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                setText(v == null ? "— Todos os projetos —" : v.toString());
                setFont(Tema.FONTE_NORMAL);
                if (s) { setBackground(Tema.ACCENT); setForeground(Color.WHITE); }
                else   { setBackground(Tema.BG_INPUT); setForeground(Tema.TEXTO_PRINCIPAL); }
                return this;
            }
        });
        cbFiltro.setPreferredSize(new Dimension(200, 32));
        cbFiltro.addActionListener(e -> recarregarTabela());
        esq.add(Tema.label("  Projeto:"));
        esq.add(cbFiltro);
        topo.add(esq, BorderLayout.WEST);

        JButton btnNovo = Tema.botaoPrimario("+ Nova Tarefa");
        btnNovo.addActionListener(e -> abrirFormulario(null));
        topo.add(btnNovo, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Título", "Projeto", "Status", "Prioridade", "Responsável", "Conclusão Prev."};
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
        recarregarTabela();
    }

    public void atualizar() {
        // Atualiza o filtro de projetos
        Projeto sel = (Projeto) cbFiltro.getSelectedItem();
        cbFiltro.removeAllItems();
        cbFiltro.addItem(null);
        projetoCtrl.listarTodos().forEach(cbFiltro::addItem);
        if (sel != null) {
            for (int i = 0; i < cbFiltro.getItemCount(); i++) {
                Object item = cbFiltro.getItemAt(i);
                if (item != null && ((Projeto)item).getId() == sel.getId()) {
                    cbFiltro.setSelectedIndex(i); break;
                }
            }
        }
        recarregarTabela();
    }

    private void recarregarTabela() {
        modeloTabela.setRowCount(0);
        Projeto filtrado = (Projeto) cbFiltro.getSelectedItem();
        java.util.List<Tarefa> lista = filtrado != null
                ? tarefaCtrl.listarPorProjeto(filtrado.getId())
                : tarefaCtrl.listarTodas();
        for (Tarefa t : lista) {
            Projeto p = projetoCtrl.buscarPorId(t.getIdProjeto());
            modeloTabela.addRow(new Object[]{
                t.getId(), t.getTitulo(),
                p != null ? p.getNome() : "—",
                t.getStatus(), t.getPrioridade(),
                t.getResponsavel() != null ? t.getResponsavel().getNome() : "—",
                t.getDataConclusaoPrevista() != null ? t.getDataConclusaoPrevista().format(FMT) : "—"});
        }
    }

    private void abrirFormulario(Tarefa tarefa) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                tarefa == null ? "Nova Tarefa" : "Editar Tarefa", true);
        dialog.setSize(460, 540);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1; g.gridx = 0;

        JTextField fTitulo = Tema.campo();
        JTextField fDesc   = Tema.campo();
        JTextField fInicio = Tema.campo();
        JTextField fConclu = Tema.campo();

        JComboBox<Projeto> cbProjeto = Tema.combo();
        projetoCtrl.listarTodos().forEach(cbProjeto::addItem);

        JComboBox<String> cbPrio = Tema.combo();
        cbPrio.addItem("ALTA"); cbPrio.addItem("MEDIA"); cbPrio.addItem("BAIXA");

        JComboBox<StatusTarefa> cbStatus = Tema.combo();
        for (StatusTarefa s : StatusTarefa.values()) cbStatus.addItem(s);

        JComboBox<Usuario> cbResp = Tema.combo();
        cbResp.addItem(null);
        usuarioCtrl.listarTodos().forEach(cbResp::addItem);
        cbResp.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                setText(v == null ? "— Sem responsável —" : v.toString());
                setFont(Tema.FONTE_NORMAL);
                if (s) { setBackground(Tema.ACCENT); setForeground(Color.WHITE); }
                else   { setBackground(Tema.BG_INPUT); setForeground(Tema.TEXTO_PRINCIPAL); }
                return this;
            }
        });

        if (tarefa != null) {
            fTitulo.setText(tarefa.getTitulo());
            fDesc.setText(tarefa.getDescricao());
            if (tarefa.getDataInicio() != null) fInicio.setText(tarefa.getDataInicio().format(FMT));
            if (tarefa.getDataConclusaoPrevista() != null) fConclu.setText(tarefa.getDataConclusaoPrevista().format(FMT));
            cbPrio.setSelectedItem(tarefa.getPrioridade());
            cbStatus.setSelectedItem(tarefa.getStatus());
            if (tarefa.getResponsavel() != null) cbResp.setSelectedItem(tarefa.getResponsavel());
            for (int i = 0; i < cbProjeto.getItemCount(); i++)
                if (cbProjeto.getItemAt(i).getId() == tarefa.getIdProjeto()) {
                    cbProjeto.setSelectedIndex(i); break; }
        }

        int row = 0;
        addRow(painel, g, "Título *",                          fTitulo,  row++);
        addRow(painel, g, "Descrição",                         fDesc,    row++);
        addRow(painel, g, "Projeto *",                         cbProjeto,row++);
        addRow(painel, g, "Início (dd/MM/yyyy)",               fInicio,  row++);
        addRow(painel, g, "Conclusão Prevista (dd/MM/yyyy)",   fConclu,  row++);
        addRow(painel, g, "Prioridade",                        cbPrio,   row++);
        addRow(painel, g, "Status",                            cbStatus, row++);
        addRow(painel, g, "Responsável",                       cbResp,   row++);

        JButton btnSalvar = Tema.botaoPrimario(tarefa == null ? "Cadastrar Tarefa" : "Salvar Alterações");
        g.gridy = row * 2 + 1; g.insets = new Insets(20, 0, 0, 0);
        painel.add(btnSalvar, g);

        btnSalvar.addActionListener(e -> {
            String titulo = fTitulo.getText().trim();
            if (titulo.isEmpty()) { Tema.erro(dialog, "Informe o título."); return; }
            LocalDate inicio = null, conclusao = null;
            try {
                if (!fInicio.getText().isBlank()) inicio = LocalDate.parse(fInicio.getText().trim(), FMT);
                if (!fConclu.getText().isBlank()) conclusao = LocalDate.parse(fConclu.getText().trim(), FMT);
            } catch (DateTimeParseException ex) { Tema.erro(dialog, "Data inválida. Use dd/MM/yyyy."); return; }
            Projeto proj = (Projeto) cbProjeto.getSelectedItem();
            if (proj == null) { Tema.erro(dialog, "Selecione um projeto."); return; }
            String prio = (String) cbPrio.getSelectedItem();
            StatusTarefa st = (StatusTarefa) cbStatus.getSelectedItem();
            Usuario resp = (Usuario) cbResp.getSelectedItem();
            String err = tarefa == null
                    ? tarefaCtrl.cadastrar(titulo, fDesc.getText().trim(), inicio, conclusao, prio, resp, proj.getId())
                    : tarefaCtrl.atualizar(tarefa.getId(), titulo, fDesc.getText().trim(), conclusao, prio, st, resp);
            if (err != null) { Tema.erro(dialog, err); return; }
            Tema.sucesso(dialog, "Tarefa salva!");
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
        if (row < 0) { Tema.erro(this, "Selecione uma tarefa."); return; }
        abrirFormulario(tarefaCtrl.buscarPorId((int) modeloTabela.getValueAt(row, 0)));
    }
    private void removerSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { Tema.erro(this, "Selecione uma tarefa."); return; }
        if (!Tema.confirmar(this, "Remover tarefa selecionada?")) return;
        String err = tarefaCtrl.remover((int) modeloTabela.getValueAt(row, 0));
        if (err != null) { Tema.erro(this, err); return; }
        atualizar();
    }
}
