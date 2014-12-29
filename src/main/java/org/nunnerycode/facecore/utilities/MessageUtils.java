package org.nunnerycode.facecore.utilities;

import org.bukkit.command.CommandSender;
import org.nunnerycode.kern.apache.commons.lang3.Validate;

public final class MessageUtils {

    private MessageUtils() {
        // do nothing
    }

    public static void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, message, new String[][]{});
    }

    public static void sendMessage(CommandSender sender, String message, String[][] args) {
        Validate.notNull(sender, "sender cannot be null");
        Validate.notNull(message, "message cannot be null");
        Validate.notNull(args, "args cannot be null");
        String toSend = message;
        toSend = TextUtils.args(toSend, args);
        toSend = TextUtils.color(toSend);
        sender.sendMessage(toSend);
    }

}
