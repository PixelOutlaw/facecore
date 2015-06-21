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
package com.tealcube.minecraft.bukkit.facecore.utilities;

import org.bukkit.ChatColor;
import org.junit.Assert;
import org.junit.Test;

public class TextUtilsTest {
    @Test
    public void testRemoveFirstColors() throws Exception {
        String string = ChatColor.GOLD + "" + ChatColor.BOLD + "Hello, world!";
        String expected = "Hello, world!";
        String actual = TextUtils.removeFirstColors(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testKeepFirstColors() throws Exception {
        String string = ChatColor.GOLD + "" + ChatColor.BOLD + "Hello, world!";
        String expected = ChatColor.GOLD + "" + ChatColor.BOLD;
        String actual = TextUtils.keepFirstColors(string);
        Assert.assertEquals(expected, actual);
    }
}