package controller;
import model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjetoController {
    private List<Projeto> projetos = new ArrayList<>();
    private int proximoId = 1;

    public String cadastrar(String nome, String descricao, LocalDate inicio, LocalDate termino, Usuario gerente) {
        if (gerente == null) return "Selecione um gerente.";
        if (gerente.getPerfil() != Perfil.GERENTE && gerente.getPerfil() != Perfil.ADMINISTRADOR)
            return "Usuário não tem perfil de Gerente ou Administrador.";
        projetos.add(new Projeto(proximoId++, nome, descricao, inicio, termino, StatusProjeto.PLANEJADO, gerente));
        return null;
    }
    public String atualizar(int id, String nome, String descricao, LocalDate termino, StatusProjeto status, Usuario gerente) {
        Projeto p = buscarPorId(id);
        if (p == null) return "Projeto não encontrado.";
        p.setNome(nome); p.setDescricao(descricao); p.setDataTerminoPrevista(termino);
        p.setStatus(status); p.setGerente(gerente); return null;
    }
    public String remover(int id) {
        Projeto p = buscarPorId(id);
        if (p == null) return "Projeto não encontrado.";
        projetos.remove(p); return null;
    }
    public Projeto buscarPorId(int id) { return projetos.stream().filter(p -> p.getId() == id).findFirst().orElse(null); }
    public List<Projeto> listarTodos() { return new ArrayList<>(projetos); }
}
