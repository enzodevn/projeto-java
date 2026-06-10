package controller;
import model.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeController {
    private List<Equipe> equipes = new ArrayList<>();
    private int proximoId = 1;
    private UsuarioController usuarioCtrl;
    private ProjetoController projetoCtrl;

    public EquipeController(UsuarioController uc, ProjetoController pc) {
        usuarioCtrl = uc; projetoCtrl = pc; }

    public String cadastrar(String nome, String descricao) {
        equipes.add(new Equipe(proximoId++, nome, descricao)); return null;
    }
    public String atualizar(int id, String nome, String descricao) {
        Equipe e = buscarPorId(id);
        if (e == null) return "Equipe não encontrada.";
        e.setNome(nome); e.setDescricao(descricao); return null;
    }
    public String remover(int id) {
        Equipe e = buscarPorId(id);
        if (e == null) return "Equipe não encontrada.";
        equipes.remove(e); return null;
    }
    public String adicionarMembro(int idEquipe, int idUsuario) {
        Equipe e = buscarPorId(idEquipe); Usuario u = usuarioCtrl.buscarPorId(idUsuario);
        if (e == null || u == null) return "Equipe ou Usuário não encontrado.";
        e.adicionarMembro(u); return null;
    }
    public String removerMembro(int idEquipe, int idUsuario) {
        Equipe e = buscarPorId(idEquipe);
        if (e == null) return "Equipe não encontrada.";
        e.removerMembro(idUsuario); return null;
    }
    public String associarProjeto(int idEquipe, int idProjeto) {
        Equipe e = buscarPorId(idEquipe); Projeto p = projetoCtrl.buscarPorId(idProjeto);
        if (e == null || p == null) return "Equipe ou Projeto não encontrado.";
        e.associarProjeto(idProjeto); return null;
    }
    public String desassociarProjeto(int idEquipe, int idProjeto) {
        Equipe e = buscarPorId(idEquipe);
        if (e == null) return "Equipe não encontrada.";
        e.desassociarProjeto(idProjeto); return null;
    }
    public Equipe buscarPorId(int id) { return equipes.stream().filter(e -> e.getId() == id).findFirst().orElse(null); }
    public List<Equipe> listarTodas() { return new ArrayList<>(equipes); }
    public List<Equipe> listarPorProjeto(int idProjeto) {
        return equipes.stream().filter(e -> e.getIdsProjetos().contains(idProjeto)).toList();
    }
}
