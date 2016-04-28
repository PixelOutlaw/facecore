package be.maximvdw.titlemotd.ui;

import org.bukkit.entity.Player;

/**
 * SendUnknown
 * 
 * Send a message to an unknown receiver.
 * 
 * @version 1.0
 * @author Maxim Van de Wynckel (Maximvdw)
 */
public class SendUnknown {
	/**
	 * Send a message to an unkown receiver
	 * 
	 * @param message
	 *            Message to send
	 * @param sender
	 *            Reciever
	 */
	public static void toSender(String message, Object sender) {
		if (sender instanceof Player) {
			SendGame.toPlayer(message, (Player) sender); // Send to game
		} else {
			SendConsole.message(message); // Send to console
		}
	}
}
