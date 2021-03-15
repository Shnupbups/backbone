package dev.vatuu.backbone.sam.model;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.vatuu.backbone.entity.meta.ArmorStandEntityMeta;
import dev.vatuu.backbone.entity.meta.base.EntityMeta;
import dev.vatuu.backbone.item.meta.ItemMeta;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class ModelBone {

    private final boolean isRoot;
    private final Item modelMaterial;
    private final int customModelValue;
    private final List<ModelBone> children = new ArrayList<>();
    private final World world;

    private ArmorStandEntity modelArmorStand, queuedStand;
    private Vec3d location;

    private final LinkedList<List<Commands>> commandQueue = new LinkedList<>();

    public ModelBone(Item model, int value, Vec3d loc, World w, boolean isRoot) {
        this.isRoot = isRoot;
        this.modelMaterial = model;
        this.customModelValue = value;
        this.location = loc;
        this.world = w;
    }
    public ModelBone(Item model, int value, Vec3d loc, World w) {
        this(model, value, loc, w, false);
    }
    public ModelBone(Vec3d loc, World w) {
        this(Items.AIR, 0, loc, w, true);
    }

    public void render(ServerPlayerEntity p) {
        if(!commandQueue.isEmpty()) {
            commandQueue.getFirst().forEach(c -> {
                switch (c) {
                    case CREATE: this.commandCreate(p); break;
                    case SHOW: this.commandShow(p); break;
                    case DELETE: this.commandDelete(p); break;
                }
            });
            commandQueue.removeFirst();
        }
        this.children.forEach(mp -> mp.render(p));
    }
    public void destroy(ServerPlayerEntity p) {
        if(this.modelArmorStand != null && !this.isRoot) {
            EntitiesDestroyS2CPacket packet = new EntitiesDestroyS2CPacket(this.modelArmorStand.getEntityId());
            p.networkHandler.connection.send(packet);
            this.modelArmorStand.remove();
            this.modelArmorStand = null;
        }

        this.children.forEach(b -> b.destroy(p));
    }

    public void addChild(ModelBone... parts) {
        children.addAll(Arrays.asList(parts));
    }

    public void show(boolean followChildren) {
        commandQueue.add(Collections.singletonList(Commands.CREATE));
        commandQueue.add(Collections.singletonList(Commands.SHOW));

        if(followChildren)
            this.children.forEach(p -> p.show(true));
    }
    public void show() {
        this.show(true);
    }

    public void hide(boolean followChildren) {
        commandQueue.add(Collections.singletonList(Commands.DELETE));

        if(followChildren)
            this.children.forEach(p -> p.hide(true));
    }
    public void hide() {
        this.hide(true);
    }

    public void translate(Vec3d vec, boolean followChildren) {
        moveTo(this.location.add(vec), followChildren);

        if(followChildren)
            this.children.forEach(p -> p.translate(vec, true));
    }
    public void translate(Vec3d vec) {
        this.translate(vec, true);
    }

    public void moveTo(Vec3d loc, boolean followChildren) {
        this.location = loc;

        commandQueue.add(Collections.singletonList(Commands.CREATE));
        commandQueue.add(Lists.newArrayList(Commands.DELETE, Commands.SHOW));

        if(followChildren)
            this.children.forEach(p -> p.moveTo(loc, true));
    }
    public void moveTo(Vec3d loc) {
        this.moveTo(loc, true);
    }

    public Vec3d getPos() {
        return location.add(0 ,1 ,0);
    }
    private ItemStack getStackCustomModel()  {
        return ItemMeta.of(this.modelMaterial)
                .setCustomModelValue(customModelValue)
                .apply();
    }

    private void commandCreate(ServerPlayerEntity p) {
        if(this.isRoot)
            return;

        this.queuedStand = new ArmorStandEntity(world, location.getX(), location.getY(), location.getZ());
        EntityMeta.<ArmorStandEntityMeta>getMeta(queuedStand)
                .setInvisible(true)
                .setNoGravity(true)
                .setBaseHidden(true)
                .setSilent(true)
                .setLockedSlots(ArmorStandEntityMeta.DisabledSlots.DISABLE_ALL)
                .setRotation(0, 0);

        Packet<?> spawn = queuedStand.createSpawnPacket();
        EntityTrackerUpdateS2CPacket data = new EntityTrackerUpdateS2CPacket(queuedStand.getEntityId(), queuedStand.getDataTracker(), false);
        p.networkHandler.connection.send(spawn);
        p.networkHandler.connection.send(data);
    }
    private void commandDelete(ServerPlayerEntity p) {
        if(this.modelArmorStand == null || this.isRoot)
            return;

        EntitiesDestroyS2CPacket packet = new EntitiesDestroyS2CPacket(this.modelArmorStand.getEntityId());
        p.networkHandler.connection.send(packet);
        this.modelArmorStand.remove();
        this.modelArmorStand = null;
    }
    private void commandShow(ServerPlayerEntity p) {
        if(this.isRoot || this.queuedStand == null)
            return;

        this.modelArmorStand = this.queuedStand;
        this.queuedStand = null;
        EntityEquipmentUpdateS2CPacket equip = new EntityEquipmentUpdateS2CPacket(this.modelArmorStand.getEntityId(), Collections.singletonList(new Pair<>(EquipmentSlot.HEAD, getStackCustomModel())));
        p.networkHandler.connection.send(equip);
    }

    private enum Commands {
        DELETE,
        CREATE,
        SHOW
    }
}
