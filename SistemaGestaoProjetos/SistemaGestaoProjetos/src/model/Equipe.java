package model;
import java.util.ArrayList;
import java.util.List;

public class Equipe {
    private int id;
    private String nome, descricao;
    private List<Usuario> membros = new ArrayList<>();
    private List<Integer> idsProjetos = new ArrayList<>();

    public Equipe() {}
    public Equipe(int id, String nome, String descricao) {
        this.id = id; this.nome = nome; this.descricao = descricao;
    }
    public void adicionarMembro(Usuario u) { if (membros.stream().noneMatch(m -> m.getId() == u.getId())) membros.add(u); }
    public void removerMembro(int id) { membros.removeIf(m -> m.getId() == id); }
    public void associarProjeto(int id) { if (!idsProjetos.contains(id)) idsProjetos.add(id); }
    public void desassociarProjeto(int id) { idsProjetos.remove(Integer.valueOf(id)); }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String n) { nome = n; }
    public String getDescricao() { return descricao; } public void setDescricao(String d) { descricao = d; }
    public List<Usuario> getMembros() { return membros; }
    public List<Integer> getIdsProjetos() { return idsProjetos; }
    @Override public String toString() { return nome; }
}
