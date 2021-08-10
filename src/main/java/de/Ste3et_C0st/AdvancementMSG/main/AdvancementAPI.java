package de.Ste3et_C0st.AdvancementMSG.main;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.NamespacedKey;

public class AdvancementAPI {

  public static List<Advancement> advancements = new ArrayList<>();

  public static void addAdvancment(Advancement api) {
    NamespacedKey key = api.getID();
    for (Advancement advancement : advancements) {
      if (advancement.getID().toString().equalsIgnoreCase(key.toString())) {
        return;
      }
    }
    advancements.add(api);
  }
}