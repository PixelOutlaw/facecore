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
package com.tealcube.minecraft.bukkit.facecore.ui;

import com.google.common.base.Preconditions;
import com.tealcube.minecraft.bukkit.facecore.utilities.TextUtils;
import com.tealcube.minecraft.bukkit.mirror.ClassType;
import com.tealcube.minecraft.bukkit.mirror.Mirror;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public final class ActionBarMessage {

    private static final Class<?> CHAT_SERIALIZER;
    private static final Class<?> CHAT_BASE_COMPONENT;
    private static final Class<?> PACKET;
    private static final Class<?> PLAY_OUT_CHAT_PACKET;

    static {
        CHAT_SERIALIZER = Mirror.getClass("IChatBaseComponent$ChatSerializer", ClassType.MINECRAFT_SERVER);
        CHAT_BASE_COMPONENT = Mirror.getClass("IChatBaseComponent", ClassType.MINECRAFT_SERVER);
        PACKET = Mirror.getClass("Packet", ClassType.MINECRAFT_SERVER);
        PLAY_OUT_CHAT_PACKET = Mirror.getClass("PacketPlayOutChat", ClassType.MINECRAFT_SERVER);
    }

    private ActionBarMessage() {
        // do nothing, make it a singleton
    }

    public static void send(Iterable<Player> players, String message) {
        for (Player player : players) {
            send(player, message);
        }
    }

    public static void send(Player player, String message) {
        Preconditions.checkNotNull(CHAT_SERIALIZER);
        Preconditions.checkNotNull(CHAT_BASE_COMPONENT);
        Preconditions.checkNotNull(PACKET);
        Preconditions.checkNotNull(PLAY_OUT_CHAT_PACKET);
        try {
            Object handle = Mirror.getMethod(player.getClass(), "getHandle").invoke(player);
            Object connection = Mirror.getField(handle.getClass(), "playerConnection").get(handle);
            Method sendPacket = Mirror.getMethod(connection.getClass(), "sendPacket", PACKET);
            Object serialized = Mirror.getMethod(CHAT_SERIALIZER, "a", String.class).invoke(null,
                    "{\"text\":\"" + TextUtils.color(message) + "\"}");
            Object packet = PLAY_OUT_CHAT_PACKET.getConstructor(CHAT_BASE_COMPONENT, Byte.TYPE).newInstance(serialized,
                    (byte) 2);
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

}
