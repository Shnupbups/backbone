package dev.vatuu.backbone.sam.model;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class ServerAnimatableModel {

    private final double renderDistance;
    private final World world;

    public ServerAnimatableModel(World w, double renderDistance) {
        this.world = w;
        this.renderDistance = renderDistance;
    }

    public abstract ModelBone getRootBone();

    public void render(ServerPlayerEntity p) { getRootBone().render(p); }
    public void destroy(ServerPlayerEntity p) { getRootBone().destroy(p); }

    public void spawn() { getRootBone().show(); }
    public void despawn() { getRootBone().hide(); }

    public void translate(Vec3d vec) { getRootBone().translate(vec); }
    public void moveTo(Vec3d loc) { getRootBone().moveTo(loc); }

    public double getRenderDistance() { return renderDistance; }
    public World getWorld() { return world; }
    public Vec3d getPos() { return getRootBone().getPos(); }


}