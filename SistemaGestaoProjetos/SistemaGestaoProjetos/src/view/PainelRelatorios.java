package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class PainelRelatorios extends JPanel {
    private ProjetoController projetoCtrl;
    private TarefaController tarefaCtrl;
    private EquipeController equipeCtrl;
    private UsuarioController usuarioCtrl;

    public PainelRelatorios(ProjetoController pc, TarefaController tc,
                            EquipeController ec, UsuarioController uc) {
        this.projetoCtrl = pc; this.tarefaCtrl = tc;
        this.equipeCtrl = ec; this.usuarioCtrl = uc;
        setBackground(Tema.BG_PRINCIPAL);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        construir();
    }

    private void construir() {
        add(Tema.titulo("Relatórios"), BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setBackground(Tema.BG_CARD);
        abas.setForeground(Tema.TEXTO_PRINCIPAL);
        abas.setFont(Tema.FONTE_SECAO);
        abas.addTab("Visão Geral", criarAbaGeral());
        abas.addTab("Por Projeto", criarAbaProjeto());
        abas.addTab("Por Colaborador", criarAbaColaborador());
        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarAbaGeral() {
        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JButton btnAtualizar = Tema.botaoPrimario("Atualizar");
        JTextArea area = new JTextArea();
        area.setFont(Tema.FONTE_MONO);
        area.setBackground(Tema.BG_PRINCIPAL);
        area.setForeground(Tema.TEXTO_PRINCIPAL);
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        btnAtualizar.addActionListener(e -> {
            List<Projeto> projetos = projetoCtrl.listarTodos();
            long plan = projetos.stream().filter(p -> p.getStatus() == StatusProjeto.PLANEJADO).count();
            long and  = projetos.stream().filter(p -> p.getStatus() == StatusProjeto.EM_ANDAMENTO).count();
            long conc = projetos.stream().filter(p -> p.getStatus() == StatusProjeto.CONCLUIDO).count();
            long canc = projetos.stream().filter(p -> p.getStatus() == StatusProjeto.CANCELADO).count();
            long totalTarefas = tarefaCtrl.listarTodas().size();
            long concluidas = tarefaCtrl.listarTodas().stream().filter(t -> t.getStatus() == StatusTarefa.CONCLUIDA).count();

            StringBuilder sb = new StringBuilder();
            sb.append("===========================================\n");
            sb.append("          RELATÓRIO GERAL DO SISTEMA       \n");
            sb.append("===========================================\n");
            sb.append(String.format("  Total de Projetos:    %3d                \n", projetos.size()));
            sb.append(String.format("     Planejados:       %3d                \n", plan));
            sb.append(String.format("     Em Andamento:     %3d                \n", and));
            sb.append(String.format("     Concluídos:       %3d                \n", conc));
            sb.append(String.format("     Cancelados:       %3d                \n", canc));
            sb.append("_________________________________________\n");
            sb.append(String.format("  Total de Tarefas:     %3d               \n", totalTarefas));
            sb.append(String.format("     Concluídas:       %3d                \n", concluidas));
            sb.append(String.format("     Pendentes:        %3d                \n", totalTarefas - concluidas));
            sb.append("_________________________________________\n");
            sb.append(String.format("  Total de Usuários:    %3d                \n", usuarioCtrl.listarTodos().size()));
            sb.append(String.format("  Total de Equipes:     %3d                \n", equipeCtrl.listarTodas().size()));
            sb.append("============================================\n");
            area.setText(sb.toString());
        });
        btnAtualizar.doClick();

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topo.setBackground(Tema.BG_CARD);
        topo.add(btnAtualizar);
        painel.add(topo, BorderLayout.NORTH);
        painel.add(Tema.scroll(area), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarAbaProjeto() {
        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JComboBox<Projeto> cbProjeto = Tema.combo();
        projetoCtrl.listarTodos().forEach(cbProjeto::addItem);
        JButton btnGerar = Tema.botaoPrimario("Gerar Relatório");

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topo.setBackground(Tema.BG_CARD);
        topo.add(Tema.label("Projeto:"));
        topo.add(cbProjeto);
        topo.add(btnGerar);
        painel.add(topo, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setFont(Tema.FONTE_MONO);
        area.setBackground(Tema.BG_PRINCIPAL);
        area.setForeground(Tema.TEXTO_PRINCIPAL);
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        btnGerar.addActionListener(e -> {
            // Atualizar combo
            Object sel = cbProjeto.getSelectedItem();
            cbProjeto.removeAllItems();
            projetoCtrl.listarTodos().forEach(cbProjeto::addItem);
            if (sel != null) cbProjeto.setSelectedItem(sel);

            Projeto p = (Projeto) cbProjeto.getSelectedItem();
            if (p == null) { area.setText("Nenhum projeto selecionado."); return; }

            List<Tarefa> tarefas = tarefaCtrl.listarPorProjeto(p.getId());
            List<Equipe> equipes = equipeCtrl.listarPorProjeto(p.getId());

            StringBuilder sb = new StringBuilder();
            sb.append("PROJETO: ").append(p.getNome().toUpperCase()).append("\n");
            sb.append("─".repeat(50)).append("\n");
            sb.append("Descrição:      ").append(p.getDescricao()).append("\n");
            sb.append("Status:         ").append(p.getStatus()).append("\n");
            sb.append("Gerente:        ").append(p.getGerente() != null ? p.getGerente().getNome() : "—").append("\n");
            sb.append("Início:         ").append(p.getDataInicio() != null ? p.getDataInicio() : "—").append("\n");
            sb.append("Término Prev.:  ").append(p.getDataTerminoPrevista() != null ? p.getDataTerminoPrevista() : "—").append("\n");
            sb.append("─".repeat(50)).append("\n");
            sb.append("─".repeat(50)).append("\n");
            sb.append("TAREFAS:\n");
            if (tarefas.isEmpty()) {
                sb.append("  (nenhuma tarefa cadastrada)\n");
            } else {
                for (Tarefa t : tarefas) {
                    sb.append(String.format("  [%-12s] %-30s | %s | %s\n",
                            t.getStatus(), t.getTitulo(), t.getPrioridade(),
                            t.getResponsavel() != null ? t.getResponsavel().getNome() : "Sem responsável"));
                }
            }
            sb.append("─".repeat(50)).append("\n");
            sb.append("EQUIPES ALOCADAS:\n");
            if (equipes.isEmpty()) {
                sb.append("  (nenhuma equipe alocada)\n");
            } else {
                for (Equipe eq : equipes) {
                    sb.append("  • ").append(eq.getNome()).append(" (").append(eq.getMembros().size()).append(" membros)\n");
                    eq.getMembros().forEach(m -> sb.append("      ↳ ").append(m.getNome()).append(" [").append(m.getPerfil()).append("]\n"));
                }
            }
            area.setText(sb.toString());
            area.setCaretPosition(0);
        });

        painel.add(Tema.scroll(area), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarAbaColaborador() {
        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBackground(Tema.BG_CARD);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JComboBox<Usuario> cbUsuario = Tema.combo();
        usuarioCtrl.listarTodos().forEach(cbUsuario::addItem);
        JButton btnGerar = Tema.botaoPrimario("Gerar Relatório");

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topo.setBackground(Tema.BG_CARD);
        topo.add(Tema.label("Colaborador:"));
        topo.add(cbUsuario);
        topo.add(btnGerar);
        painel.add(topo, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setFont(Tema.FONTE_MONO);
        area.setBackground(Tema.BG_PRINCIPAL);
        area.setForeground(Tema.TEXTO_PRINCIPAL);
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        btnGerar.addActionListener(e -> {
            Object sel = cbUsuario.getSelectedItem();
            cbUsuario.removeAllItems();
            usuarioCtrl.listarTodos().forEach(cbUsuario::addItem);
            if (sel != null) cbUsuario.setSelectedItem(sel);

            Usuario u = (Usuario) cbUsuario.getSelectedItem();
            if (u == null) { area.setText("Nenhum usuário selecionado."); return; }

            List<Tarefa> tarefas = tarefaCtrl.listarPorResponsavel(u.getId());
            long conc = tarefas.stream().filter(t -> t.getStatus() == StatusTarefa.CONCLUIDA).count();
            long prog = tarefas.stream().filter(t -> t.getStatus() == StatusTarefa.EM_PROGRESSO).count();
            long pend = tarefas.stream().filter(t -> t.getStatus() == StatusTarefa.PENDENTE).count();
            long bloq = tarefas.stream().filter(t -> t.getStatus() == StatusTarefa.BLOQUEADA).count();

            StringBuilder sb = new StringBuilder();
            sb.append("COLABORADOR: ").append(u.getNome().toUpperCase()).append("\n");
            sb.append("─".repeat(50)).append("\n");
            sb.append("Cargo:  ").append(u.getCargo()).append("\n");
            sb.append("Perfil: ").append(u.getPerfil()).append("\n");
            sb.append("E-mail: ").append(u.getEmail()).append("\n");
            sb.append("─".repeat(50)).append("\n");
            sb.append("TAREFAS ATRIBUÍDAS: ").append(tarefas.size()).append("\n");
            sb.append(String.format("   Concluídas:   %d\n", conc));
            sb.append(String.format("   Em Progresso: %d\n", prog));
            sb.append(String.format("   Pendentes:    %d\n", pend));
            sb.append(String.format("   Bloqueadas:   %d\n", bloq));
            sb.append("─".repeat(50)).append("\n");
            if (!tarefas.isEmpty()) {
                sb.append("DETALHES:\n");
                for (Tarefa t : tarefas) {
                    Projeto p = projetoCtrl.buscarPorId(t.getIdProjeto());
                    sb.append(String.format("  [%-12s] %-30s | Projeto: %s\n",
                            t.getStatus(), t.getTitulo(), p != null ? p.getNome() : "—"));
                }
            }
            area.setText(sb.toString());
            area.setCaretPosition(0);
        });

        painel.add(Tema.scroll(area), BorderLayout.CENTER);
        return painel;
    }

    public void atualizar() {
        // Pode ser chamado para forçar refresh dos combos se necessário
        removeAll(); construir(); revalidate(); repaint();
    }
}
