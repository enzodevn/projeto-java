package model;
public enum StatusTarefa {
    PENDENTE, EM_PROGRESSO, CONCLUIDA, BLOQUEADA;
    @Override public String toString() {
        return switch (this) {
            case PENDENTE -> "Pendente";
            case EM_PROGRESSO -> "Em Progresso";
            case CONCLUIDA -> "Concluída";
            case BLOQUEADA -> "Bloqueada";
        };
    }
}
