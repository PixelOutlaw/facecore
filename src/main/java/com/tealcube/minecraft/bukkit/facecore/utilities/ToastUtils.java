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

import com.comphenix.protocol.events.PacketContainer;
import com.loohp.interactivechat.InteractiveChat;
import com.loohp.interactivechat.libs.net.kyori.adventure.text.Component;
import com.loohp.interactivechat.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.loohp.interactivechat.utils.ChatComponentType;
import com.loohp.interactivechat.utils.ClassUtils;
import com.loohp.interactivechat.utils.ItemStackUtils;
import com.loohp.interactivechat.utils.NMSUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.loohp.interactivechat.objectholders.ICPlayer;

public class ToastUtils {

  private static Class<?> nmsMinecraftKeyClass;
  private static Constructor<?> nmsMinecraftKeyConstructor;
  private static Class<?> nmsAdvancementRewardsClass;
  private static Class<?> nmsCustomFunctionAClass;
  private static Constructor<?> nmsAdvancementRewardsConstructor;
  private static Class<?> nmsAdvancementDisplayClass;
  private static Class<?> nmsItemStackClass;
  private static Class<?> nmsIChatBaseComponentClass;
  private static Class<?> nmsAdvancementFrameTypeClass;
  private static Constructor<?> nmsAdvancementDisplayConstructor;
  private static Object[] nmsAdvancementFrameTypeEnums;
  private static Class<?> nmsCriterionClass;
  private static Class<?> nmsCriterionTriggerImpossibleAClass;
  private static Constructor<?> nmsCriterionTriggerImpossibleAConstructor;
  private static Class<?> nmsCriterionInstanceClass;
  private static Constructor<?> nmsCriterionConstructor;
  private static Class<?> nmsAdvancementClass;
  private static Constructor<?> nmsAdvancementConstructor;
  private static Class<?> nmsAdvancementProgressClass;
  private static Constructor<?> nmsAdvancementProgressConstructor;
  private static Method nmsAdvancementProgressAMethod;
  private static Method nmsAdvancementProgressGetCriterionProgressMethod;
  private static Method nmsAdvancementProgressGetCriterionProgressBMethod;
  private static Class<?> nmsPacketPlayOutAdvancementsClass;
  private static Constructor<?> nmsPacketPlayOutAdvancementsConstuctor;

  public static void sendToast(Player player, String message, ItemStack stack) {
    sendToast(player, message, stack, ToastStyle.GOAL);
  }

  public static void sendToast(Player player, String message, ItemStack stack, ToastStyle style) {
    ICPlayer toastPlayer = new ICPlayer(player);
    sendPacketToast(toastPlayer, player, message, stack, style);
  }

  private static void sendPacketToast(ICPlayer sender, Player pinged, String message, ItemStack icon, ToastStyle style) {
    ChatColor.translateAlternateColorCodes('&', message);
    try {
      Object minecraftKey = nmsMinecraftKeyConstructor.newInstance("facecore", "toasty/" + sender.getUniqueId());
      Object advRewards = nmsAdvancementRewardsConstructor.newInstance(0, Array.newInstance(nmsMinecraftKeyClass, 0), Array.newInstance(nmsMinecraftKeyClass, 0), null);
      Object componentTitle = ChatComponentType.IChatBaseComponent.convertTo(
          LegacyComponentSerializer.legacySection().deserialize(message), InteractiveChat.version.isLegacy());
      Object componentSubtitle = ChatComponentType.IChatBaseComponent.convertTo(Component.empty(), InteractiveChat.version.isLegacy());
      Object advancementDisplay = nmsAdvancementDisplayConstructor.newInstance(ItemStackUtils.toNMSCopy(icon), componentTitle, componentSubtitle, null, nmsAdvancementFrameTypeEnums[style.ordinal()], true, false, true);
      Map<String, Object> advCriteria = new HashMap();
      String[][] advRequirements;
      advCriteria.put("for_free", nmsCriterionConstructor.newInstance(nmsCriterionTriggerImpossibleAConstructor.newInstance()));
      List<String[]> fixedRequirements = new ArrayList();
      fixedRequirements.add(new String[]{"for_free"});
      advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);
      Object saveAdv = nmsAdvancementConstructor.newInstance(minecraftKey, null, advancementDisplay, advRewards, advCriteria, advRequirements);
      Map<Object, Object> prg = new HashMap();
      Object advPrg = nmsAdvancementProgressConstructor.newInstance();
      nmsAdvancementProgressAMethod.invoke(advPrg, advCriteria, advRequirements);
      nmsAdvancementProgressGetCriterionProgressBMethod.invoke(nmsAdvancementProgressGetCriterionProgressMethod.invoke(advPrg, "for_free"));
      prg.put(minecraftKey, advPrg);
      PacketContainer packet1 = PacketContainer.fromPacket(nmsPacketPlayOutAdvancementsConstuctor.newInstance(false, Arrays.asList(saveAdv), Collections.emptySet(), prg));
      InteractiveChat.protocolManager.sendServerPacket(pinged, packet1);
      Set<Object> rm = new HashSet();
      rm.add(minecraftKey);
      prg.clear();
      PacketContainer packet2 = PacketContainer.fromPacket(nmsPacketPlayOutAdvancementsConstuctor.newInstance(false, Collections.emptyList(), rm, prg));
      InteractiveChat.protocolManager.sendServerPacket(pinged, packet2);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException var18) {
      var18.printStackTrace();
    }
  }

  public enum ToastStyle {
    INFO,
    CHALLENGE,
    GOAL
  }

  static {
    try {
      nmsMinecraftKeyClass = NMSUtils.getNMSClass("net.minecraft.server.%s.MinecraftKey", new String[]{"net.minecraft.resources.MinecraftKey"});
      nmsMinecraftKeyConstructor = nmsMinecraftKeyClass.getConstructor(String.class, String.class);
      nmsAdvancementRewardsClass = NMSUtils.getNMSClass("net.minecraft.server.%s.AdvancementRewards", new String[]{"net.minecraft.advancements.AdvancementRewards"});
      nmsCustomFunctionAClass = NMSUtils.getNMSClass("net.minecraft.server.%s.CustomFunction$a", new String[]{"net.minecraft.commands.CustomFunction$a"});
      nmsAdvancementRewardsConstructor = nmsAdvancementRewardsClass.getConstructor(Integer.TYPE, ClassUtils.arrayType(nmsMinecraftKeyClass), ClassUtils.arrayType(nmsMinecraftKeyClass), nmsCustomFunctionAClass);
      nmsAdvancementDisplayClass = NMSUtils.getNMSClass("net.minecraft.server.%s.AdvancementDisplay", new String[]{"net.minecraft.advancements.AdvancementDisplay"});
      nmsItemStackClass = NMSUtils.getNMSClass("net.minecraft.server.%s.ItemStack", new String[]{"net.minecraft.world.item.ItemStack"});
      nmsIChatBaseComponentClass = NMSUtils.getNMSClass("net.minecraft.server.%s.IChatBaseComponent", new String[]{"net.minecraft.network.chat.IChatBaseComponent"});
      nmsAdvancementFrameTypeClass = NMSUtils.getNMSClass("net.minecraft.server.%s.AdvancementFrameType", new String[]{"net.minecraft.advancements.AdvancementFrameType"});
      nmsAdvancementDisplayConstructor = nmsAdvancementDisplayClass.getConstructor(nmsItemStackClass, nmsIChatBaseComponentClass, nmsIChatBaseComponentClass, nmsMinecraftKeyClass, nmsAdvancementFrameTypeClass, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE);
      nmsAdvancementFrameTypeEnums = nmsAdvancementFrameTypeClass.getEnumConstants();
      nmsCriterionClass = NMSUtils.getNMSClass("net.minecraft.server.%s.Criterion", new String[]{"net.minecraft.advancements.Criterion"});
      nmsCriterionTriggerImpossibleAClass = NMSUtils.getNMSClass("net.minecraft.server.%s.CriterionTriggerImpossible$a", new String[]{"net.minecraft.advancements.critereon.CriterionTriggerImpossible$a"});
      nmsCriterionTriggerImpossibleAConstructor = nmsCriterionTriggerImpossibleAClass.getConstructor();
      nmsCriterionInstanceClass = NMSUtils.getNMSClass("net.minecraft.server.%s.CriterionInstance", new String[]{"net.minecraft.advancements.CriterionInstance"});
      nmsCriterionConstructor = nmsCriterionClass.getConstructor(nmsCriterionInstanceClass);
      nmsAdvancementClass = NMSUtils.getNMSClass("net.minecraft.server.%s.Advancement", new String[]{"net.minecraft.advancements.Advancement"});
      nmsAdvancementConstructor = nmsAdvancementClass.getConstructor(nmsMinecraftKeyClass, nmsAdvancementClass, nmsAdvancementDisplayClass, nmsAdvancementRewardsClass, Map.class, String[][].class);
      nmsAdvancementProgressClass = NMSUtils.getNMSClass("net.minecraft.server.%s.AdvancementProgress", new String[]{"net.minecraft.advancements.AdvancementProgress"});
      nmsAdvancementProgressConstructor = nmsAdvancementProgressClass.getConstructor();
      nmsAdvancementProgressAMethod = nmsAdvancementProgressClass.getMethod("a", Map.class, String[][].class);
      nmsAdvancementProgressGetCriterionProgressMethod = nmsAdvancementProgressClass.getMethod("getCriterionProgress", String.class);
      nmsAdvancementProgressGetCriterionProgressBMethod = nmsAdvancementProgressGetCriterionProgressMethod.getReturnType().getMethod("b");
      nmsPacketPlayOutAdvancementsClass = NMSUtils.getNMSClass("net.minecraft.server.%s.PacketPlayOutAdvancements", new String[]{"net.minecraft.network.protocol.game.PacketPlayOutAdvancements"});
      nmsPacketPlayOutAdvancementsConstuctor = nmsPacketPlayOutAdvancementsClass.getConstructor(Boolean.TYPE, Collection.class, Set.class, Map.class);
    } catch (Exception var1) {
      var1.printStackTrace();
    }

  }
}
