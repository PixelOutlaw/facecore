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

import org.apache.commons.lang.StringEscapeUtils;

public class DrawImage {

  public static void screen(String imageId, int x, int y) {

  }

  public static enum DrawOffset {

    O_1024(StringEscapeUtils.unescapeJava("\\uF2F"), StringEscapeUtils.unescapeJava("\\uF80F")),
    O_512(StringEscapeUtils.unescapeJava("\\uF82E"), StringEscapeUtils.unescapeJava("\\uF80E")),
    O_256(StringEscapeUtils.unescapeJava("\\uF82D"), StringEscapeUtils.unescapeJava("\\uF80D")),
    O_128(StringEscapeUtils.unescapeJava("\\uF82C"), StringEscapeUtils.unescapeJava("\\uF80C")),
    O_64(StringEscapeUtils.unescapeJava("\\uF82B"), StringEscapeUtils.unescapeJava("\\uF80B")),
    O_32(StringEscapeUtils.unescapeJava("\\uF82A"), StringEscapeUtils.unescapeJava("\\uF80A")),
    O_16(StringEscapeUtils.unescapeJava("\\uF829"), StringEscapeUtils.unescapeJava("\\uF809")),
    O_8(StringEscapeUtils.unescapeJava("\\uF828"), StringEscapeUtils.unescapeJava("\\uF808")),
    O_4(StringEscapeUtils.unescapeJava("\\uF824"), StringEscapeUtils.unescapeJava("\\uF804")),
    O_2(StringEscapeUtils.unescapeJava("\\uF822"), StringEscapeUtils.unescapeJava("\\uF802")),
    O_1(StringEscapeUtils.unescapeJava("\\uF821"), StringEscapeUtils.unescapeJava("\\uF801"));

    private final String positive;
    private final String negative;

    DrawOffset(String positive, String negative) {
      this.positive = positive;
      this.negative = negative;
    }

    public String positive() {
      return positive;
    }

    public String negative() {
      return negative;
    }

  }

}
