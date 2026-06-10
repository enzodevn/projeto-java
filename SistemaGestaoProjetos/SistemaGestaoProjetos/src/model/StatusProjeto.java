package model;
public enum StatusProjeto {
    PLANEJADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO;
    @Override public String toString() {
        return switch (this) {
            case PLANEJADO -> "Planejado";
            case EM_ANDAMENTO -> "Em Andamento";
            case CONCLUIDO -> "Concluído";
            case CANCELADO -> "Cancelado";
        };
    }
}
