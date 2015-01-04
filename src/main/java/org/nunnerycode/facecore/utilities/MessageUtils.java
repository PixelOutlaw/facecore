/*
 * This file is part of Facecore, licensed under the ISC License.
 *
 * Copyright (c) 2014 Richard Harrah
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 */
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
