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