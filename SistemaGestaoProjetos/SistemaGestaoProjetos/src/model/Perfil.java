package model;
public enum Perfil {
    ADMINISTRADOR, GERENTE, COLABORADOR;
    @Override public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
