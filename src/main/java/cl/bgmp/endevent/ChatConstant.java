package cl.bgmp.endevent;

import org.bukkit.ChatColor;

public enum ChatConstant {
    NO_PERMISSION("No tienes permiso para ejecutar este comando."),
    NO_CONSOLE("Necesitas ser un jugador para ejecutar este comando."),

    MATCH_CANNOT_START("La partida no puede ser iniciada en este momento."),
    MATCH_STARTING("¡El evento del end iniciando en {0}!"),
    MATCH_STARTED("¡El evento ha iniciado!"),
    MATCH_CANCELLED("El evento del end  ha sido cancelado."),
    MATCH_WINNER("¡El evento ha finalizado!. Winner: "),
    MATCH_UNKNOWN_WINNER("The EndEvent has finished. The Dragon died for unknown reasons."),

    MISC_SECOND("segundo"),
    MISC_SECONDS("segundos"),
    MISC_GENERATING_MOBS("Generando mobs, ¡Cuidado!"),

    DEBUG_HEALTH_BEFORE("Vida del dragon antes: "),
    DEBUG_HEALTH_AFTER("Vida del dragon ahora: "),

    DRAGON_NAME("Event Dragon"),

    PREFIX(ChatColor.WHITE + "[" + ChatColor.GOLD + "End Event" + ChatColor.WHITE + "] ");

    private String message;

    ChatConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage(ChatColor color) {
        return PREFIX.getMessage() + color + message;
    }
}
