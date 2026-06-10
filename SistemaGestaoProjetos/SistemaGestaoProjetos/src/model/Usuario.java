package model;
public class Usuario extends Pessoa {
    private String cargo, login, senha;
    private Perfil perfil;
    public Usuario() {}
    public Usuario(int id, String nome, String cpf, String email,
                   String cargo, String login, String senha, Perfil perfil) {
        super(id, nome, cpf, email);
        this.cargo = cargo; this.login = login; this.senha = senha; this.perfil = perfil;
    }
    @Override public String getTipo() { return "Usuário"; }
    public String getCargo() { return cargo; } public void setCargo(String c) { cargo = c; }
    public String getLogin() { return login; } public void setLogin(String l) { login = l; }
    public String getSenha() { return senha; } public void setSenha(String s) { senha = s; }
    public Perfil getPerfil() { return perfil; } public void setPerfil(Perfil p) { perfil = p; }
}
