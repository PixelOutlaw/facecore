/*
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

import com.tealcube.minecraft.bukkit.facecore.FacecorePlugin;
import com.tealcube.minecraft.bukkit.facecore.pojo.ActionBarContainer;
import com.tealcube.minecraft.bukkit.facecore.pojo.ActionBarMessage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public final class AdvancedActionBarUtil {

  private final static Map<Player, ActionBarContainer> barMap = new HashMap<>();
  private static int tickRate = 4;
  private static BukkitTask task;
  private static final String DELIMITER = ChatColor.DARK_GRAY + " ◆ ";

  public static void addMessage(Player player, String messageId, String message, int ticks) {
    addMessage(player, messageId, message, ticks, 0);
  }

  public static void addMessage(Player player, String messageId, String message, int ticks, int weight) {
    if (!barMap.containsKey(player)) {
      ActionBarContainer container = new ActionBarContainer();
      barMap.put(player, container);
    }
    ActionBarContainer container = barMap.get(player);
    for (String id : container.getMessageMap().keySet()) {
      if (id.equals(messageId)) {
        ActionBarMessage barMessage = container.getMessageMap().get(id);
        barMessage.setMessage(message);
        barMessage.setTicksRemaining(ticks);
        barMessage.setWeight(weight);
        return;
      }
    }
    ActionBarMessage barMessage = new ActionBarMessage(message, ticks, weight);
    container.getMessageMap().put(messageId, barMessage);
  }

  public static void removeMessage(Player player, String messageId) {
    if (!barMap.containsKey(player)) {
      return;
    }
    Iterator<String> messageIterator = barMap.get(player).getMessageMap().keySet().iterator();
    while (messageIterator.hasNext()) {
      String id = messageIterator.next();
      if (id.equals(messageId)) {
        messageIterator.remove();
        return;
      }
    }
  }

  private static void tickAllMessages() {
    Iterator<Player> playerIterator = barMap.keySet().iterator();
    while (playerIterator.hasNext()) {
      Player player = playerIterator.next();
      if (player == null || !player.isValid() || barMap.get(player).getMessageMap().isEmpty()) {
        playerIterator.remove();
        continue;
      }
      List<ActionBarMessage> messageList = new ArrayList<>();
      Iterator<ActionBarMessage> messageIterator = barMap.get(player).getMessageMap().values().iterator();
      while (messageIterator.hasNext()) {
        ActionBarMessage message = messageIterator.next();
        if (message.getTicksRemaining() > 0) {
          messageList.add(message);
          message.setTicksRemaining(message.getTicksRemaining() - tickRate);
          continue;
        }
        messageIterator.remove();
      }
      messageList.sort(Comparator.comparingInt(ActionBarMessage::getWeight));
      AtomicReference<String> result = new AtomicReference<>(messageList.stream().map(ActionBarMessage::getMessage)
          .collect(Collectors.joining(DELIMITER)));
      Bukkit.getScheduler().runTask(FacecorePlugin.getInstance(), () -> {
        result.set(PlaceholderAPI.setPlaceholders(player, result.get()));
        MessageUtils.sendActionBar(player, result.get());
      });
    }
  }

  public static void stopTask() {
    if (task == null || task.isCancelled()) {
      return;
    }
    task.cancel();
  }

  public static void startTask(int newTickRate) {
    stopTask();
    tickRate = newTickRate;
    task = Bukkit.getScheduler().runTaskTimerAsynchronously(FacecorePlugin.getInstance(),
        AdvancedActionBarUtil::tickAllMessages, 100L, tickRate);
  }
}
