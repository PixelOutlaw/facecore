package be.maximvdw.titlemotd.ui;

import org.bukkit.entity.Player;

/**
 * SendUnknown
 * <p>
 * Send a message to an unknown receiver.
 *
 * @author Maxim Van de Wynckel (Maximvdw)
 * @version 1.0
 */
public class SendUnknown {
    /**
     * Send a message to an unkown receiver
     *
     * @param message Message to send
     * @param sender  Reciever
     */
    public static void toSender(String message, Object sender) {
        if (sender instanceof Player) {
            SendGame.toPlayer(message, (Player) sender); // Send to game
        } else {
            SendConsole.message(message); // Send to console
        }
    }
}
