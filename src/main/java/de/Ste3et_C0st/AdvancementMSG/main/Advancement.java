package de.Ste3et_C0st.AdvancementMSG.main;

import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tealcube.minecraft.bukkit.facecore.FacecorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Date;

import com.google.common.collect.Lists;

public class Advancement {

  private NamespacedKey id;
  private String title, parent, trigger, icon, description, background, frame;
  private Integer subID = 0, amount = 0;
  private boolean announce, toast = true;
  private List<ItemStack> items;

  public enum FrameType {
    CHALLANGE("challenge"),
    GOAL("goal"),
    DEFAULT("task");

    private String str;

    FrameType(String str) {
      this.str = str;
    }

    public String getName() {
      return this.str;
    }
  }

  public enum AdvancementBackground {
    ADVENTURE("minecraft:textures/gui/advancements/backgrounds/adventure.png"),
    END("minecraft:textures/gui/advancements/backgrounds/end.png"),
    HUSBANDRY("minecraft:textures/gui/advancements/backgrounds/husbandry.png"),
    NETHER("minecraft:textures/gui/advancements/backgrounds/nether.png"),
    STONE("minecraft:textures/gui/advancements/backgrounds/stone.png"),
    fromNamespace(null);

    public String str;

    AdvancementBackground(String str) {
      this.str = str;
    }

    public void fromNamespace(String string) {
      str = string;
    }
  }

  public Advancement(NamespacedKey id) {
    this.id = id;
    this.items = Lists.newArrayList();
    this.announce = true;
  }

  public NamespacedKey getID() {
    return id;
  }

  public String getIcon() {
    return icon;
  }

  public Advancement withIcon(String icon) {
    this.icon = icon;
    return this;
  }

  public Advancement withIcon(Material material) {
    this.icon = getMinecraftIDFrom(new ItemStack(material));
    return this;
  }

  public Advancement withIconData(int subID) {
    this.subID = subID;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Advancement withDescription(String description) {
    this.description = description;
    return this;
  }

  public String getBackground() {
    return background;
  }

  public Advancement withBackground(String url) {
    this.background = url;
    return this;
  }

  public Advancement withAmount(int i) {
    this.amount = i;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Advancement withTitle(String title) {
    this.title = title;
    return this;
  }

  public String getParent() {
    return parent;
  }

  public Advancement withParent(String parent) {
    this.parent = parent;
    return this;
  }

  public Advancement withToast(boolean bool) {
    this.toast = bool;
    return this;
  }

  public String getTrigger() {
    return trigger;
  }

  public Advancement withTrigger(String trigger) {
    this.trigger = trigger;
    return this;
  }

  public List<ItemStack> getItems() {
    return items;
  }

  public Advancement withItem(ItemStack is) {
    items.add(is);
    return this;
  }

  public String getFrame() {
    return frame;
  }

  public Advancement withFrame(FrameType frame) {
    this.frame = frame.getName();
    return this;
  }

  public boolean getAnnouncement() {
    return announce;
  }

  public Advancement withAnnouncement(boolean announce) {
    this.announce = announce;
    return this;
  }

  @SuppressWarnings("unchecked")
  public String getJSON() {
    if (this.amount > 0) {
      return getJson(this.amount);
    }
    JsonObject json = new JsonObject();

    JsonObject icon = new JsonObject();
    icon.addProperty("item", getIcon());
    icon.addProperty("data", getIconSubID());

    JsonObject display = new JsonObject();
    display.add("icon", icon);
    display.addProperty("title", getTitle());
    display.addProperty("description", getDescription());
    display.addProperty("background", getBackground());
    display.addProperty("frame", getFrame());
    display.addProperty("announce_to_chat", getAnnouncement());
    display.addProperty("show_toast", getToast());

    json.addProperty("parent", getParent());

    JsonObject criteria = new JsonObject();
    JsonObject conditions = new JsonObject();
    JsonObject elytra = new JsonObject();

    JsonArray itemArray = new JsonArray();
    JsonObject itemJSON = new JsonObject();

    for (ItemStack i : getItems()) {
      itemJSON.addProperty("item", "minecraft:" + i.getType().name().toLowerCase());
      itemJSON.addProperty("amount", i.getAmount());
      itemArray.add(itemJSON);
    }

    conditions.add("items", itemArray);
    elytra.addProperty("trigger", getTrigger());
    elytra.add("conditions", conditions);

    criteria.add("elytra", elytra);

    json.add("criteria", criteria);
    json.add("display", display);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(json);
  }

  @SuppressWarnings("unchecked")
  public String getJson(int amount) {
    if (!getFrame().equalsIgnoreCase("challenge")) {
      return getJSON();
    }
    JsonObject json = new JsonObject();

    JsonObject icon = new JsonObject();
    icon.addProperty("item", getIcon());
    icon.addProperty("data", getIconSubID());

    JsonObject display = new JsonObject();
    display.add("icon", icon);
    display.addProperty("title", getTitle());
    display.addProperty("description", getDescription());
    display.addProperty("background", getBackground());
    display.addProperty("frame", getFrame());
    display.addProperty("announce_to_chat", getAnnouncement());
    display.addProperty("show_toast", getToast());

    json.addProperty("parent", getParent());

    JsonObject criteria = new JsonObject();
    JsonObject conditions = new JsonObject();

    JsonArray itemArray = new JsonArray();
    JsonObject itemJSON = new JsonObject();

    for (ItemStack i : getItems()) {
      itemJSON.addProperty("item", "minecraft:" + i.getType().name().toLowerCase());
      itemJSON.addProperty("amount", i.getAmount());
      itemArray.add(itemJSON);
    }

    for (int i = 0; i <= amount; i++) {
      JsonObject elytra = new JsonObject();
      elytra.addProperty("trigger", "minecraft:impossible");
      conditions.add("items", itemArray);
      elytra.add("conditions", conditions);
      criteria.add("key" + i, elytra);
    }

    json.add("criteria", criteria);
    json.add("display", display);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(json);
  }

  private boolean getToast() {
    return this.toast;
  }

  private int getIconSubID() {
    return this.subID;
  }

  @SuppressWarnings("deprecation")
  public void loadAdvancement() {
    for (World world : Bukkit.getWorlds()) {
      Path path = Paths.get(world.getWorldFolder() + File.separator + "data"
          + File.separator + "advancements" + File.separator + id.getNamespace() + File.separator
          + getID().getKey().split("/")[0]);

      Path path2 = Paths.get(world.getWorldFolder() + File.separator + "data"
          + File.separator + "advancements" + File.separator + id.getNamespace()
          + File.separator + getID().getKey().split("/")[0] + File.separator + getID().getKey()
          .split("/")[1] + ".json");

      if (!path.toFile().exists()) {
        path.toFile().mkdirs();
      }

      if (!path2.toFile().exists()) {
        File file = path2.toFile();
        try {
          file.createNewFile();
          FileWriter writer = new FileWriter(file);
          writer.write(getJSON());
          writer.flush();
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    if (Bukkit.getAdvancement(getID()) == null) {
      Bukkit.getUnsafe().loadAdvancement(getID(), getJSON());
    }

    AdvancementAPI.addAdvancment(this);
  }

  @SuppressWarnings("deprecation")
  public void delete() {
    Bukkit.getUnsafe().removeAdvancement(getID());
  }

  @SuppressWarnings("deprecation")
  public void delete(Player... player) {
    for (Player p : player) {
      if (p.getAdvancementProgress(getAdvancement()).isDone()) {
        p.getAdvancementProgress(getAdvancement()).revokeCriteria("elytra");
      }
    }

    Bukkit.getScheduler().runTaskLater(FacecorePlugin.getInstance(), new Runnable() {
      @Override
      public void run() {
        //CraftMagicNumbers.INSTANCE.removeAdvancement(getID());
      }
    }, 5);
  }

  public static String getMinecraftIDFrom(ItemStack stack) {
    /*
    final int check = Item.getId(CraftItemStack.asNMSCopy(stack).getItem());
    final MinecraftKey matching = Item.REGISTRY.keySet()
        .stream()
        .filter(key -> Item.getId(Item.REGISTRY.get(key)) == check)
        .findFirst().orElse(null);
    return Objects.toString(matching, null);
    */
    return "a";
  }

  public void sendPlayer(Player... player) {
    for (Player p : player) {
      if (!p.getAdvancementProgress(getAdvancement()).isDone()) {
        p.getAdvancementProgress(getAdvancement()).awardCriteria("elytra");
      }
    }
  }

  public void sendPlayer(String criteria, Player... player) {
    for (Player p : player) {
      if (!p.getAdvancementProgress(getAdvancement()).isDone()) {
        p.getAdvancementProgress(getAdvancement()).awardCriteria(criteria);
      }
    }
  }

  public boolean next(Player p) {
    if (!p.getAdvancementProgress(getAdvancement()).isDone()) {
      for (String criteria : getAdvancement().getCriteria()) {
        if (p.getAdvancementProgress(getAdvancement()).getDateAwarded(criteria) == null) {
          p.getAdvancementProgress(getAdvancement()).awardCriteria(criteria);
          return true;
        }
      }
    }
    return false;
  }

  public boolean next(Player p, long diff, boolean onlyLast) {
    if (!p.getAdvancementProgress(getAdvancement()).isDone()) {
      Date oldData = null;
      String str = "";
      for (String criteria : getAdvancement().getCriteria()) {
        if (p.getAdvancementProgress(getAdvancement()).getDateAwarded(criteria) != null) {
          oldData = p.getAdvancementProgress(getAdvancement()).getDateAwarded(criteria);
          str = criteria;
          continue;
        } else {
          if (oldData == null) {
            p.getAdvancementProgress(getAdvancement()).awardCriteria(criteria);
            return true;
          } else {
            long oldTime = oldData.getTime();
            long current = System.currentTimeMillis();
            if ((current - diff) > oldTime) {
              if (onlyLast) {
                p.getAdvancementProgress(getAdvancement()).revokeCriteria(str);
                return false;
              } else {
                for (String string : getAdvancement().getCriteria()) {
                  p.getAdvancementProgress(getAdvancement()).revokeCriteria(string);
                }
                p.getAdvancementProgress(getAdvancement())
                    .awardCriteria(getAdvancement().getCriteria().stream().findFirst().get());
                return false;
              }
            } else {
              p.getAdvancementProgress(getAdvancement()).awardCriteria(criteria);
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public Date getLastAwardTime(Player p) {
    if (!p.getAdvancementProgress(getAdvancement()).isDone()) {
      Date oldData = null;
      for (String criteria : getAdvancement().getCriteria()) {
        if (p.getAdvancementProgress(getAdvancement()).getDateAwarded(criteria) != null) {
          oldData = p.getAdvancementProgress(getAdvancement()).getDateAwarded(criteria);
          continue;
        } else {
          return oldData;
        }
      }
    }
    return null;
  }

  public org.bukkit.advancement.Advancement getAdvancement() {
    return Bukkit.getAdvancement(getID());
  }
}
