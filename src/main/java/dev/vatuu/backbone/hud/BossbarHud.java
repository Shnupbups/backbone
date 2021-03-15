package dev.vatuu.backbone.hud;

import dev.vatuu.backbone.managers.BossbarManager;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossbarHud {

    private final Map<Identifier, BossBar> layers;

    public BossbarHud(List<Identifier> ids, World w) {
        this.layers = new HashMap<>();
        ids.forEach(i -> layers.put(i, BossbarManager.INSTANCE.createForWorld(i, new LiteralText(""), w)));
    }

    public BossbarHud setText(Identifier id, Text t) {
        BossBar bar = layers.get(id);
        if(bar == null)
            return this;
        bar.setName(t);
        return this;
    }

    public void destroy() {
        layers.forEach((id, t) -> BossbarManager.INSTANCE.destroy(id));
    }
}
