package controller;
import model.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioController {
    private List<Usuario> usuarios = new ArrayList<>();
    private int proximoId = 1;

    public String cadastrar(String nome, String cpf, String email, String cargo, String login, String senha, Perfil perfil) {
        if (buscarPorCpf(cpf) != null) return "CPF já cadastrado.";
        if (buscarPorLogin(login) != null) return "Login já em uso.";
        usuarios.add(new Usuario(proximoId++, nome, cpf, email, cargo, login, senha, perfil));
        return null;
    }
    public String atualizar(int id, String nome, String email, String cargo, Perfil perfil) {
        Usuario u = buscarPorId(id);
        if (u == null) return "Usuário não encontrado.";
        u.setNome(nome); u.setEmail(email); u.setCargo(cargo); u.setPerfil(perfil);
        return null;
    }
    public String remover(int id) {
        Usuario u = buscarPorId(id);
        if (u == null) return "Usuário não encontrado.";
        usuarios.remove(u); return null;
    }
    public Usuario autenticar(String login, String senha) {
        return usuarios.stream().filter(u -> u.getLogin().equals(login) && u.getSenha().equals(senha)).findFirst().orElse(null);
    }
    public Usuario buscarPorId(int id) { return usuarios.stream().filter(u -> u.getId() == id).findFirst().orElse(null); }
    public Usuario buscarPorCpf(String cpf) { return usuarios.stream().filter(u -> u.getCpf().equals(cpf)).findFirst().orElse(null); }
    public Usuario buscarPorLogin(String login) { return usuarios.stream().filter(u -> u.getLogin().equals(login)).findFirst().orElse(null); }
    public List<Usuario> listarTodos() { return new ArrayList<>(usuarios); }
}
