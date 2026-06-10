package model;
import java.time.LocalDate;

public class Tarefa {
    private int id, idProjeto;
    private String titulo, descricao, prioridade;
    private LocalDate dataInicio, dataConclusaoPrevista;
    private StatusTarefa status = StatusTarefa.PENDENTE;
    private Usuario responsavel;

    public Tarefa() {}
    public Tarefa(int id, String titulo, String descricao, LocalDate dataInicio,
                  LocalDate dataConclusao, String prioridade, Usuario responsavel, int idProjeto) {
        this.id = id; this.titulo = titulo; this.descricao = descricao;
        this.dataInicio = dataInicio; this.dataConclusaoPrevista = dataConclusao;
        this.prioridade = prioridade; this.responsavel = responsavel; this.idProjeto = idProjeto;
    }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getIdProjeto() { return idProjeto; } public void setIdProjeto(int i) { idProjeto = i; }
    public String getTitulo() { return titulo; } public void setTitulo(String t) { titulo = t; }
    public String getDescricao() { return descricao; } public void setDescricao(String d) { descricao = d; }
    public String getPrioridade() { return prioridade; } public void setPrioridade(String p) { prioridade = p; }
    public LocalDate getDataInicio() { return dataInicio; } public void setDataInicio(LocalDate d) { dataInicio = d; }
    public LocalDate getDataConclusaoPrevista() { return dataConclusaoPrevista; } public void setDataConclusaoPrevista(LocalDate d) { dataConclusaoPrevista = d; }
    public StatusTarefa getStatus() { return status; } public void setStatus(StatusTarefa s) { status = s; }
    public Usuario getResponsavel() { return responsavel; } public void setResponsavel(Usuario r) { responsavel = r; }
    @Override public String toString() { return titulo; }
}
