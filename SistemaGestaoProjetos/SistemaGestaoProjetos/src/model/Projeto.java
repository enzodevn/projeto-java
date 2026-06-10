package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Projeto {
    private int id;
    private String nome, descricao;
    private LocalDate dataInicio, dataTerminoPrevista;
    private StatusProjeto status;
    private Usuario gerente;
    private List<Tarefa> tarefas = new ArrayList<>();

    public Projeto() { status = StatusProjeto.PLANEJADO; }
    public Projeto(int id, String nome, String descricao,
                   LocalDate dataInicio, LocalDate dataTermino, StatusProjeto status, Usuario gerente) {
        this(); this.id = id; this.nome = nome; this.descricao = descricao;
        this.dataInicio = dataInicio; this.dataTerminoPrevista = dataTermino;
        this.status = status; this.gerente = gerente;
    }
    public void adicionarTarefa(Tarefa t) { tarefas.add(t); }
    public void removerTarefa(int idT) { tarefas.removeIf(t -> t.getId() == idT); }
    public int getTotalTarefas() { return tarefas.size(); }
    public long getTarefasConcluidas() { return tarefas.stream().filter(t -> t.getStatus() == StatusTarefa.CONCLUIDA).count(); }
    public double getPercentualConclusao() { return tarefas.isEmpty() ? 0 : (getTarefasConcluidas() * 100.0) / getTotalTarefas(); }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String n) { nome = n; }
    public String getDescricao() { return descricao; } public void setDescricao(String d) { descricao = d; }
    public LocalDate getDataInicio() { return dataInicio; } public void setDataInicio(LocalDate d) { dataInicio = d; }
    public LocalDate getDataTerminoPrevista() { return dataTerminoPrevista; } public void setDataTerminoPrevista(LocalDate d) { dataTerminoPrevista = d; }
    public StatusProjeto getStatus() { return status; } public void setStatus(StatusProjeto s) { status = s; }
    public Usuario getGerente() { return gerente; } public void setGerente(Usuario g) { gerente = g; }
    public List<Tarefa> getTarefas() { return tarefas; }
    @Override public String toString() { return nome; }
}
