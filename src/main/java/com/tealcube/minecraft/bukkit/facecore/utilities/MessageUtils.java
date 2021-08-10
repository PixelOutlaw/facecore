/**
 * The MIT License
 * Copyright (c) 2015 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.facecore.utilities;

import io.pixeloutlaw.minecraft.spigot.garbage.StringExtensionsKt;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kotlin.Pair;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MessageUtils {

    private MessageUtils() {
        // do nothing
    }

    public static void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, message, new String[][]{});
    }

    public static void sendMessage(CommandSender sender, String message, String[][] args) {
        String toSend = prepareMessage(sender, message, args);
        sender.sendMessage(toSend);
    }

    @NotNull
    private static String prepareMessage(CommandSender sender, String message, String[][] args) {
        Validate.notNull(sender, "sender cannot be null");
        Validate.notNull(message, "message cannot be null");
        Validate.notNull(args, "args cannot be null");
        List<Pair<String, String>> argList = Arrays.stream(args).map(arg -> {
            if (arg.length < 2) {
                return null;
            }
            return new Pair<>(arg[0], arg[1]);
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return StringExtensionsKt.chatColorize(StringExtensionsKt.replaceArgs(message, argList));
    }

    public static void sendActionBar(Player sender, String message) {
        sendActionBar(sender, message, new String[][]{});
    }

    public static void sendActionBar(Player sender, String message, String[][] args) {
        String toSend = prepareMessage(sender, message, args);
        sender.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(toSend));
    }
}
