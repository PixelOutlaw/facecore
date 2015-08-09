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
package be.maximvdw.titlemotd.ui;

import java.lang.reflect.Method;

import com.google.common.base.Preconditions;
import com.tealcube.minecraft.bukkit.mirror.ClassType;
import com.tealcube.minecraft.bukkit.mirror.Mirror;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.tealcube.minecraft.bukkit.facecore.utilities.TextUtils;

/**
 * Minecraft 1.8 Title
 *
 * @author Maxim Van de Wynckel
 *         <p>
 *         Modified by ToppleTheNun on 1/21/2015
 * @version 1.0.4
 */
public class Title {

    private static final Class<?> CHAT_SERIALIZER;
    private static final Class<?> CHAT_BASE_COMPONENT;
    private static final Class<?> PACKET;
    private static final Class<?> PLAY_OUT_TITLE_PACKET;
    private static final Class<?> ENUM_TITLE_ACTION;

    static {
        CHAT_SERIALIZER = Mirror.getClass("IChatBaseComponent$ChatSerializer", ClassType.MINECRAFT_SERVER);
        CHAT_BASE_COMPONENT = Mirror.getClass("IChatBaseComponent", ClassType.MINECRAFT_SERVER);
        PACKET = Mirror.getClass("Packet", ClassType.MINECRAFT_SERVER);
        PLAY_OUT_TITLE_PACKET = Mirror.getClass("PacketPlayOutTitle", ClassType.MINECRAFT_SERVER);
        ENUM_TITLE_ACTION = Mirror.getClass("PacketPlayOutTitle$EnumTitleAction", ClassType.MINECRAFT_SERVER);
    }

    /* Title text and color */
    private String title = "";
    private ChatColor titleColor = ChatColor.WHITE;
    /* Subtitle text and color */
    private String subtitle = "";
    private ChatColor subtitleColor = ChatColor.WHITE;
    /* Title timings */
    private int fadeInTime = -1;
    private int stayTime = -1;
    private int fadeOutTime = -1;

    private boolean ticks = false;

    /**
     * Create a new 1.8 title
     *
     * @param title Title
     */
    public Title(String title) {
        this.title = title;
    }

    /**
     * Create a new 1.8 title
     *
     * @param title    Title text
     * @param subtitle Subtitle text
     */
    public Title(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    /**
     * Copy 1.8 title
     *
     * @param title Title
     */
    public Title(Title title) {
        // Copy title
        this.title = title.title;
        this.subtitle = title.subtitle;
        this.titleColor = title.titleColor;
        this.subtitleColor = title.subtitleColor;
        this.fadeInTime = title.fadeInTime;
        this.fadeOutTime = title.fadeOutTime;
        this.stayTime = title.stayTime;
        this.ticks = title.ticks;
    }

    /**
     * Create a new 1.8 title
     *
     * @param title       Title text
     * @param subtitle    Subtitle text
     * @param fadeInTime  Fade in time
     * @param stayTime    Stay on screen time
     * @param fadeOutTime Fade out time
     */
    public Title(String title, String subtitle, int fadeInTime, int stayTime,
                 int fadeOutTime) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeInTime = fadeInTime;
        this.stayTime = stayTime;
        this.fadeOutTime = fadeOutTime;
    }

    /**
     * Set title text
     *
     * @param title Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get title text
     *
     * @return Title text
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Set subtitle text
     *
     * @param subtitle Subtitle text
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Get subtitle text
     *
     * @return Subtitle text
     */
    public String getSubtitle() {
        return this.subtitle;
    }

    /**
     * Set the title color
     *
     * @param color Chat color
     */
    public void setTitleColor(ChatColor color) {
        this.titleColor = color;
    }

    /**
     * Set the subtitle color
     *
     * @param color Chat color
     */
    public void setSubtitleColor(ChatColor color) {
        this.subtitleColor = color;
    }

    /**
     * Set title fade in time
     *
     * @param time Time
     */
    public void setFadeInTime(int time) {
        this.fadeInTime = time;
    }

    /**
     * Set title fade out time
     *
     * @param time Time
     */
    public void setFadeOutTime(int time) {
        this.fadeOutTime = time;
    }

    /**
     * Set title stay time
     *
     * @param time Time
     */
    public void setStayTime(int time) {
        this.stayTime = time;
    }

    /**
     * Set timings to ticks
     */
    public void setTimingsToTicks() {
        ticks = true;
    }

    /**
     * Set timings to seconds
     */
    public void setTimingsToSeconds() {
        ticks = false;
    }

    /**
     * Send the title to a player
     *
     * @param player Player
     */
    public void send(Player player) {
        Preconditions.checkNotNull(PACKET);
        Preconditions.checkNotNull(PLAY_OUT_TITLE_PACKET);
        Preconditions.checkNotNull(CHAT_BASE_COMPONENT);
        Preconditions.checkNotNull(CHAT_SERIALIZER);
        Preconditions.checkNotNull(ENUM_TITLE_ACTION);
        // First reset previous settings
        resetTitle(player);
        try {
            Object handle = Mirror.getMethod(player.getClass(), "getHandle").invoke(player);
            Object connection = Mirror.getField(handle.getClass(), "playerConnection").get(handle);
            Method sendPacket = Mirror.getMethod(connection.getClass(), "sendPacket", PACKET);
            Object[] actions = ENUM_TITLE_ACTION.getEnumConstants();
            Object packet = PLAY_OUT_TITLE_PACKET.getConstructor(ENUM_TITLE_ACTION,
                    CHAT_BASE_COMPONENT, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(
                    actions[2], null, fadeInTime * (ticks ? 1 : 20),
                    stayTime * (ticks ? 1 : 20), fadeOutTime * (ticks ? 1 : 20));
            // Send if set
            if (fadeInTime != -1 && fadeOutTime != -1 && stayTime != -1)
                sendPacket.invoke(connection, packet);

            // Send title
            Object serialized = Mirror.getMethod(CHAT_SERIALIZER, "a", String.class).invoke(null,
                    "{\"text\":\"" + TextUtils.color(title) + "\",color:" + titleColor.name().toLowerCase() + "}");
            packet = PLAY_OUT_TITLE_PACKET.getConstructor(ENUM_TITLE_ACTION, CHAT_BASE_COMPONENT).newInstance(
                    actions[0], serialized);
            sendPacket.invoke(connection, packet);
            if (subtitle != null && !subtitle.isEmpty()) {
                // Send subtitle if present
                serialized = Mirror.getMethod(CHAT_SERIALIZER, "a", String.class).invoke(null,
                        "{\"text\":\"" + TextUtils.color(subtitle) + "\",color:" + subtitleColor.name().toLowerCase() + "}");
                PLAY_OUT_TITLE_PACKET.getConstructor(ENUM_TITLE_ACTION, CHAT_BASE_COMPONENT).newInstance(
                        actions[1], serialized);
                sendPacket.invoke(connection, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Broadcast the title to all players
     */
    public void broadcast() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            send(p);
        }
    }

    /**
     * Clear the title
     *
     * @param player Player
     */
    public void clearTitle(Player player) {
        Preconditions.checkNotNull(PACKET);
        Preconditions.checkNotNull(PLAY_OUT_TITLE_PACKET);
        Preconditions.checkNotNull(CHAT_BASE_COMPONENT);
        Preconditions.checkNotNull(CHAT_SERIALIZER);
        Preconditions.checkNotNull(ENUM_TITLE_ACTION);
        try {
            Object handle = Mirror.getMethod(player.getClass(), "getHandle").invoke(player);
            Object connection = Mirror.getField(handle.getClass(), "playerConnection").get(handle);
            Method sendPacket = Mirror.getMethod(connection.getClass(), "sendPacket", PACKET);
            Object[] actions = ENUM_TITLE_ACTION.getEnumConstants();
            Object packet = PLAY_OUT_TITLE_PACKET.getConstructor(ENUM_TITLE_ACTION,
                    CHAT_BASE_COMPONENT).newInstance(actions[3], null);
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset the title settings
     *
     * @param player Player
     */
    public void resetTitle(Player player) {
        Preconditions.checkNotNull(PACKET);
        Preconditions.checkNotNull(PLAY_OUT_TITLE_PACKET);
        Preconditions.checkNotNull(CHAT_BASE_COMPONENT);
        Preconditions.checkNotNull(CHAT_SERIALIZER);
        Preconditions.checkNotNull(ENUM_TITLE_ACTION);
        try {
            Object handle = Mirror.getMethod(player.getClass(), "getHandle").invoke(player);
            Object connection = Mirror.getField(handle.getClass(), "playerConnection").get(handle);
            Method sendPacket = Mirror.getMethod(connection.getClass(), "sendPacket", PACKET);
            Object[] actions = ENUM_TITLE_ACTION.getEnumConstants();
            Object packet = PLAY_OUT_TITLE_PACKET.getConstructor(ENUM_TITLE_ACTION,
                    CHAT_BASE_COMPONENT).newInstance(actions[4], null);
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

}