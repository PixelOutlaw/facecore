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

import org.bukkit.entity.Player;

public final class TitleUtils {
    private final static int DEFAULT_FADE_IN = 10;
    private final static int DEFAULT_FADE_OUT = 10;
    private final static int DEFAULT_DURATION = 40;

    public static void sendTitle(Player sender, String upper, String lower) {
        sendTitle(sender, upper, lower, DEFAULT_DURATION, DEFAULT_FADE_IN, DEFAULT_FADE_OUT);
    }

    public static void sendTitle(Player sender, String upper, String lower, int duration,
        int fadeIn, int fadeOut) {
        sender.sendTitle(upper, lower, fadeIn, duration, fadeOut);
    }
}
