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
package com.tealcube.minecraft.bukkit.facecore.ui;

import com.tealcube.minecraft.bukkit.facecore.utilities.TextUtils;
import com.tealcube.minecraft.bukkit.mirror.ClassType;
import com.tealcube.minecraft.bukkit.mirror.Mirror;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class ActionBarMessage {

    private final String message;
    private final Class<?> chatSerializer;
    private final Class<?> chatBaseComponent;
    private final Class<?> playOutChatPacket;

    public ActionBarMessage(String message) {
        this.message = message;
        this.chatSerializer = Mirror.getClass("ChatSerializer", ClassType.NMS);
        this.chatBaseComponent = Mirror.getClass("IChatBaseComponent", ClassType.NMS);
        this.playOutChatPacket = Mirror.getClass("PacketPlayOutChat", ClassType.NMS);
    }

    public void send(Iterable<Player> players) {
        for (Player player : players) {
            send(player);
        }
    }

    public void send(Player player) {
        try {
            Class<?> packetClass = Mirror.getClass("Packet", ClassType.NMS);
            Object handle = Mirror.getMethod(player.getClass(), "getHandle").invoke(player);
            Object connection = Mirror.getField(handle.getClass(), "playerConnection").get(handle);
            Method sendPacket = Mirror.getMethod(connection.getClass(), "sendPacket", packetClass);
            Object serialized = Mirror.getMethod(chatSerializer, "a", String.class).invoke(null,
                    "{\"text\":\"" + TextUtils.color(message) + "\"}");
            Object packet = playOutChatPacket.getConstructor(chatBaseComponent, Byte.TYPE).newInstance(serialized,
                    (byte) 2);
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
