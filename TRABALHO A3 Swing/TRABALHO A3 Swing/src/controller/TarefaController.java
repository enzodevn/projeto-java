package controller;
import model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TarefaController {
    private List<Tarefa> tarefas = new ArrayList<>();
    private int proximoId = 1;
    private ProjetoController projetoCtrl;

    public TarefaController(ProjetoController pc) { this.projetoCtrl = pc; }

    public String cadastrar(String titulo, String descricao, LocalDate inicio, LocalDate conclusao,
                            String prioridade, Usuario responsavel, int idProjeto) {
        Projeto p = projetoCtrl.buscarPorId(idProjeto);
        if (p == null) return "Projeto não encontrado.";
        Tarefa t = new Tarefa(proximoId++, titulo, descricao, inicio, conclusao, prioridade, responsavel, idProjeto);
        tarefas.add(t); p.adicionarTarefa(t); return null;
    }
    public String atualizar(int id, String titulo, String descricao, LocalDate conclusao, String prioridade, StatusTarefa status, Usuario responsavel) {
        Tarefa t = buscarPorId(id);
        if (t == null) return "Tarefa não encontrada.";
        t.setTitulo(titulo); t.setDescricao(descricao); t.setDataConclusaoPrevista(conclusao);
        t.setPrioridade(prioridade); t.setStatus(status); t.setResponsavel(responsavel); return null;
    }
    public String remover(int id) {
        Tarefa t = buscarPorId(id);
        if (t == null) return "Tarefa não encontrada.";
        Projeto p = projetoCtrl.buscarPorId(t.getIdProjeto());
        if (p != null) p.removerTarefa(id);
        tarefas.remove(t); return null;
    }
    public Tarefa buscarPorId(int id) { return tarefas.stream().filter(t -> t.getId() == id).findFirst().orElse(null); }
    public List<Tarefa> listarPorProjeto(int idProjeto) { return tarefas.stream().filter(t -> t.getIdProjeto() == idProjeto).toList(); }
    public List<Tarefa> listarPorResponsavel(int idUsuario) {
        return tarefas.stream().filter(t -> t.getResponsavel() != null && t.getResponsavel().getId() == idUsuario).toList();
    }
    public List<Tarefa> listarTodas() { return new ArrayList<>(tarefas); }
}
