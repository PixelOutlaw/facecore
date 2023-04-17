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
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public class ChunkUtil {

  private static final Map<String, Chunk> chunkMap = new HashMap<>();
  private static final Map<Entity, Boolean> DESPAWN_ON_UNLOAD = new WeakHashMap<>();

  /**
   * Clears saved chunk keys. Don't use this, unless you WANT
   * unexpected results from other methods
   */
  public static void clearChunkCache() {
    chunkMap.clear();
  }

  /**
   * Generates a unique key for the chunk and stores it
   * for faster chunk checking
   */
  public static void cacheChunk(Chunk chunk) {
    chunkMap.put(buildChunkKey(chunk), chunk);
  }

  /**
   * Removes a chunk from the chunk key cache
   */
  public static void unCacheChunk(Chunk chunk) {
    chunkMap.values().remove(chunk);
  }

  public static boolean canSpawnEntity(String chunkKey) {
    if (chunkMap.containsKey((chunkKey))) {
      return chunkMap.get(chunkKey).isEntitiesLoaded();
    }
    return false;
  }

  public static boolean isChunkLoaded(Chunk chunk) {
    return isChuckLoaded(buildChunkKey(chunk));
  }

  public static boolean isChuckLoaded(String chunkKey) {
    return chunkMap.containsKey(chunkKey);
  }

  public static String buildChunkKey(Chunk chunk) {
    return chunk.getWorld().getName() + chunk.getChunkKey();
  }

  public static void setDespawnOnUnload(Entity e) {
    e.getPersistentDataContainer().set(FacecorePlugin.killKey, PersistentDataType.SHORT, (short) 1);
    DESPAWN_ON_UNLOAD.put(e, true);
  }

  public static boolean isDespawnOnUnload(Entity e) {
    return DESPAWN_ON_UNLOAD.getOrDefault(e, false);
  }

  public static void despawnAllTempEntities() {
    for (Entity e : DESPAWN_ON_UNLOAD.keySet()) {
      e.remove();
    }
  }
}
