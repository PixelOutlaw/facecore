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
package org.nunnerycode.facecore.ui;

import me.captainbern.bukkittool.BukkitTool;
import org.bukkit.entity.Player;
import org.nunnerycode.facecore.utilities.TextUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static me.captainbern.bukkittool.ServerClass.*;

public class ActionBarMessage {

    private final String message;
    private final Class<?> chatSerializer;
    private final Class<?> chatBaseComponent;
    private final Class<?> playOutChatPacket;

    private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<Class<?>, Class<?>>();

    public ActionBarMessage(String message) {
        this.message = message;
        this.chatSerializer = BukkitTool.getNMSClass("ChatSerializer");
        this.chatBaseComponent = BukkitTool.getNMSClass("IChatBaseComponent");
        this.playOutChatPacket = BukkitTool.getNMSClass("PacketPlayOutChat");
    }

    public void send(Player player) {
        Object handle = new ServerMethod(player.getClass(), "getHandle").invoke(player);
        Object connection = new ServerField(handle.getClass(), "playerConnection").get(handle);
        Method sendPacket = getMethod(connection.getClass(), "sendPacket");
        try {
            Object serialized = new ServerMethod(chatSerializer, "a", String.class).getMethod().invoke(null,
                    "{\"text\":\"" +
                            TextUtils.color(message) + "\"}");
            Object packet = playOutChatPacket.getConstructor(chatBaseComponent, Byte.TYPE).newInstance(serialized,
                    (byte) 2);
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Iterable<Player> players) {
        for (Player player : players) {
            send(player);
        }
    }

    private Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        for (Method m : clazz.getMethods())
            if (m.getName().equals(name)
                    && (args.length == 0 || ClassListEqual(args,
                    m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        return null;
    }

    private boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++)
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        return equal;
    }

}
