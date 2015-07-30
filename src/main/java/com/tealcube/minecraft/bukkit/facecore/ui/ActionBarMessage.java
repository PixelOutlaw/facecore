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
        this.chatSerializer = Mirror.getClass("IChatBaseComponent.ChatSerializer", ClassType.NMS);
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
