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
package ninja.amp.ampmenus.items;

import com.tealcube.minecraft.bukkit.facecore.utilities.PaletteUtil;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import ninja.amp.ampmenus.events.ItemClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class MenuItem {

  private final String displayName;
  private final TextComponent displayNameComponent;
  private final ItemStack icon;
  private final List<String> lore;
  private final List<TextComponent> loreComponent;

  public MenuItem(String displayName, ItemStack icon, String... lore) {
    this.displayName = displayName;
    this.displayNameComponent = PaletteUtil.convert(displayName);
    this.icon = icon;
    this.lore = Arrays.asList(lore);
    loreComponent = PaletteUtil.convert(this.lore);
  }

  public static ItemStack setNameAndLore(ItemStack itemStack, String displayName, List<String> lore) {
    itemStack.editMeta(m -> {
      m.setLore(lore);
      m.setDisplayName(displayName);
    });
    return itemStack;
  }

  public static ItemStack setNameAndLore(ItemStack itemStack, TextComponent displayName, List<TextComponent> lore) {
    itemStack.editMeta(m -> {
      m.lore(lore);
      m.displayName(displayName);
    });
    return itemStack;
  }

  public ItemStack getFinalIcon(Player player) {
    return setNameAndLore(getIcon().clone(), displayNameComponent, loreComponent);
  }

  public void onItemClick(ItemClickEvent event) {
    // Do nothing by default
  }
}
