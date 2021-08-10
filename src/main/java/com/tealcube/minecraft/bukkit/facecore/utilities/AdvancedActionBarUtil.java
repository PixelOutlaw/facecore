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

import com.tealcube.minecraft.bukkit.facecore.FacecorePlugin;
import com.tealcube.minecraft.bukkit.facecore.pojo.ActionBarContainer;
import com.tealcube.minecraft.bukkit.facecore.pojo.ActionBarMessage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public final class AdvancedActionBarUtil {

  private static final Map<Player, ActionBarContainer> barMap = new WeakHashMap<>();
  private static final Map<Player, ActionBarContainer> overrideMap = new WeakHashMap<>();
  private static final String DELIMITER = ChatColor.DARK_GRAY + " ◆ ";

  private static int tickRate = 3;
  private static BukkitTask task;

  public static void addMessage(Player player, String messageId, String message, int ticks) {
    addMessage(player, messageId, message, ticks, 0);
  }

  public static void addOverrideMessage(Player player, String messageId, String message, int ticks) {
    addOverrideMessage(player, messageId, message, ticks, 0);
  }

  public static void addOverrideMessage(Player player, String messageId, String message, int ticks, int weight) {
    if (!overrideMap.containsKey(player)) {
      ActionBarContainer container = new ActionBarContainer();
      overrideMap.put(player, container);
    }
    ActionBarContainer container = overrideMap.get(player);
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
    playMessageToPlayer(player, overrideMap);
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
    if (!overrideMap.containsKey(player)) {
      playMessageToPlayer(player, barMap);
    }
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

  public static void removeOverrideMessage(Player player, String messageId) {
    if (!overrideMap.containsKey(player)) {
      return;
    }
    Iterator<String> messageIterator = overrideMap.get(player).getMessageMap().keySet().iterator();
    while (messageIterator.hasNext()) {
      String id = messageIterator.next();
      if (id.equals(messageId)) {
        messageIterator.remove();
        return;
      }
    }
  }

  private static void tickAllMessages() {
    for (Player p : Bukkit.getOnlinePlayers()) {
      if (overrideMap.containsKey(p)) {
        if (overrideMap.get(p).getMessageMap().isEmpty()) {
          overrideMap.remove(p);
          continue;
        }
        playMessageToPlayer(p, overrideMap);
        incrementBars(p, overrideMap);
        incrementBars(p, barMap);
        continue;
      }
      if (barMap.containsKey(p)) {
        if (barMap.get(p).getMessageMap().isEmpty()) {
          barMap.remove(p);
          continue;
        }
        playMessageToPlayer(p, barMap);
        incrementBars(p, barMap);
      }
    }
  }

  private static void playMessageToPlayer(Player player, Map<Player, ActionBarContainer> map) {
    List<ActionBarMessage> messageList = new ArrayList<>(map.get(player).getMessageMap().values());
    messageList.sort(Comparator.comparingInt(ActionBarMessage::getWeight));
    AtomicReference<String> result = new AtomicReference<>(messageList.stream().map(
        actionBarMessage -> actionBarMessage.getMessage()
            .replace("{n}", Integer.toString(actionBarMessage.getTicksRemaining())))
        .collect(Collectors.joining(DELIMITER)));
    Bukkit.getScheduler().runTask(FacecorePlugin.getInstance(), () -> {
      result.set(PlaceholderAPI.setPlaceholders(player, result.get()));
      MessageUtils.sendActionBar(player, result.get());
    });
  }

  private static void incrementBars(Player p, Map<Player, ActionBarContainer> map) {
    if (!map.containsKey(p)) {
      return;
    }
    Iterator<ActionBarMessage> messageIterator = map.get(p).getMessageMap().values().iterator();
    while (messageIterator.hasNext()) {
      ActionBarMessage message = messageIterator.next();
      if (message.getTicksRemaining() > 0) {
        message.setTicksRemaining(message.getTicksRemaining() - tickRate);
        continue;
      }
      messageIterator.remove();
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
