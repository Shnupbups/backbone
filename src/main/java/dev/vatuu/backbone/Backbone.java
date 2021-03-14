package dev.vatuu.backbone;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.vatuu.backbone.gambit.hud.GambitHUD;
import dev.vatuu.backbone.gamestate.GamestateManager;
import dev.vatuu.backbone.item.NumericBanners;
import dev.vatuu.backbone.item.meta.ItemMeta;
import dev.vatuu.backbone.item.meta.MapItemMeta;
import dev.vatuu.backbone.managers.BossbarManager;
import dev.vatuu.backbone.entity.meta.EndCrystalEntityMeta;
import dev.vatuu.backbone.entity.meta.base.EntityMeta;
import dev.vatuu.backbone.events.CommandRegistrationCallback;
import dev.vatuu.backbone.events.ServerLifecycleEvents;
import dev.vatuu.backbone.events.ServerTickEvents;
import dev.vatuu.backbone.sam.Player1pxHeadModel;
import dev.vatuu.backbone.sam.SamManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Random;

import static net.minecraft.server.command.CommandManager.literal;

public class Backbone implements DedicatedServerModInitializer {

    public static final String MOD_ID = "backbone";
    public static MinecraftServer SERVER;

    public static Identifier id(String path) { return new Identifier(MOD_ID, path); }

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            SERVER = server;
            SamManager.init();
            BossbarManager.init(SERVER);
            GamestateManager.init();
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if(hud != null)
                hud.destroy();
        });
        CommandRegistrationCallback.EVENT.register(this::registerTestCommands);
    }

    private final Identifier MODEL = id("model");
    private GambitHUD hud;

    public void registerTestCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("eTest")
                .requires(src -> src.hasPermissionLevel(4))
                .executes(ctx -> {
                    BlockPos p = ctx.getSource().getPlayer().getBlockPos();
                    World w = ctx.getSource().getWorld();

                    EndCrystalEntity entity = new EndCrystalEntity(w, p.getX(), p.getY(), p.getZ());
                    w.spawnEntity(entity);

                    ServerTickEvents.START_SERVER_TICK.register(t -> {
                        try {
                            ServerPlayerEntity player = t.getPlayerManager().getPlayer(ctx.getSource().getPlayer().getUuid());
                            EntityMeta.<EndCrystalEntityMeta>getMeta(entity).setBeamTarget(player.getBlockPos().mutableCopy().subtract(new Vec3i(0, 1, 0)));
                        } catch(CommandSyntaxException e) {
                            ctx.getSource().sendError(new LiteralText(e.getMessage()));
                        }
                    });

                    return 1;
                }));

        dispatcher.register(
                literal("sam")
                    .requires(src -> src.hasPermissionLevel(4))
                .then(literal("summon")
                    .executes(ctx -> {
                        Vec3d pos = ctx.getSource().getPlayer().getPos();
                        World w = ctx.getSource().getWorld();
                        SamManager sam = SamManager.INSTANCE;

                        if(sam.modelExists(MODEL))
                            sam.unregister(MODEL);
                        sam.register(MODEL, new Player1pxHeadModel(w, pos, 9, 0, 0, 16 * 8));
                        return 1;
                    }))
                .then(literal("start")
                    .executes(ctx -> {
                        SamManager sam = SamManager.INSTANCE;
                        if(!sam.modelExists(MODEL)) {
                            ctx.getSource().sendError(new LiteralText("Sam has not been spawned."));
                            return 1;
                        }
                        sam.addPlayer(MODEL, ctx.getSource().getPlayer());
                        return 1;
                    })));

        dispatcher.register(
                literal("testhud")
                    .requires(src -> src.hasPermissionLevel(4))
                    .then(literal("hide")
                        .executes(ctx -> hideHud()))
                    .then(literal("show")
                        .executes(ctx -> showHud(ctx.getSource().getWorld())))
                    .then(literal("random")
                        .executes(ctx -> updateHudRandom(ctx.getSource().getWorld()))));

        dispatcher.register(
                literal("testitem")
                    .requires(src -> src.hasPermissionLevel(4))
                    .executes(ctx -> {
                        ServerPlayerEntity e = ctx.getSource().getPlayer();
                        try {
                            DyeColor d1 = DyeColor.BLACK;
                            DyeColor d2 = DyeColor.PURPLE;
                            e.inventory.setStack(14, NumericBanners.ZERO.getBannerItem(d2, d1));
                            e.inventory.setStack(15, NumericBanners.TWO.getBannerItem(d2, d1));
                            e.inventory.setStack(16, NumericBanners.EIGHT.getBannerItem(d2, d1));
                            e.inventory.setStack(17, NumericBanners.FIVE.getBannerItem(d2, d1));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return 1;
                    })
        );
    }

    private int showHud(World w) {
        if(hud == null)
            hud = new GambitHUD(w, false);
        hud.show();
        hud.update();
        return 1;
    }

    private int hideHud() {
        if(hud == null)
            return 1;
        hud.destroy();
        return 1;
    }

    private int updateHudRandom(World w) {
        if(hud == null)
            hud = new GambitHUD(w, false);

        Random r = new Random();
        int score1 = r.nextInt(100);
        int score2 = r.nextInt(100);

        hud.setLeftScore(score1)
        .setRightScore(score2)
        .setLeftBlockers(hud.getRandomBlockers())
        .setRightBlockers(hud.getRandomBlockers())
        .update();

        return 1;
    }
}
