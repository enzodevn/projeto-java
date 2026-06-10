package model;
public abstract class Pessoa {
    private int id;
    private String nome, cpf, email;
    public Pessoa() {}
    public Pessoa(int id, String nome, String cpf, String email) {
        this.id = id; this.nome = nome; this.cpf = cpf; this.email = email;
    }
    public abstract String getTipo();
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String n) { nome = n; }
    public String getCpf() { return cpf; } public void setCpf(String c) { cpf = c; }
    public String getEmail() { return email; } public void setEmail(String e) { email = e; }
    @Override public String toString() { return nome; }
}
