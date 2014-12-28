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
package org.nunnerycode.facecore.hilt;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HiltItemStack extends ItemStack {

    public HiltItemStack(Material type) {
        super(type);
        createItemMeta();
    }

    public HiltItemStack(Material type, int amount) {
        super(type, amount);
        createItemMeta();
    }

    public HiltItemStack(Material type, int amount, short damage) {
        super(type, amount, damage);
        createItemMeta();
    }

    public HiltItemStack(ItemStack stack) throws IllegalArgumentException {
        super(stack);
        createItemMeta();
    }

    protected void createItemMeta() {
        if (!hasItemMeta()) {
            setItemMeta(Bukkit.getItemFactory().getItemMeta(getType()));
        }
    }

    public String getName() {
        createItemMeta();
        if (getItemMeta().hasDisplayName()) {
            return getItemMeta().getDisplayName();
        }
        return WordUtils.capitalizeFully(Joiner.on(" ").skipNulls().join(getType().name().split("_")));
    }

    public HiltItemStack setName(String name) {
        createItemMeta();
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(name.replace("\\s+", " "));
        setItemMeta(itemMeta);
        return this;
    }

    public List<String> getLore() {
        createItemMeta();
        if (getItemMeta().hasLore()) {
            return new ArrayList<>(getItemMeta().getLore());
        }
        return new ArrayList<>();
    }

    public HiltItemStack setLore(List<String> lore) {
        createItemMeta();
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
        return this;
    }

}
