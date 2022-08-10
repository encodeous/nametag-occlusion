package ca.encodeous.nametagocclusion;

import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.entity.Pose;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EventListener implements Listener {
    static ConcurrentHashMap<UUID, ArmorStand> stands = new ConcurrentHashMap<>();
    static ConcurrentHashMap<UUID, Silverfish> fish = new ConcurrentHashMap<>();
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event){
        var p = event.getPlayer();
        NametagOcclusion.invisibleTeam.addPlayer(p);
        var entity = p.getWorld().spawn(p.getLocation().add(0, 4, 0), ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setCustomNameVisible(true);
            stand.setCustomName(p.getName());
            stand.setGravity(false);
            stand.setMarker(true);
            var entity2 = p.getWorld().spawn(p.getLocation().add(0, 4, 0), Silverfish.class, sf -> {
                sf.setAI(false);
                sf.setSilent(true);
                sf.setGravity(false);
                sf.setCollidable(false);
                sf.setInvisible(true);
                sf.setInvulnerable(true);
                p.hideEntity(NametagOcclusion.instance, sf);
            });
            fish.put(p.getUniqueId(), entity2);
            var cbStand = (CraftArmorStand) stand;
            cbStand.getHandle().setShiftKeyDown(true);
            entity2.addPassenger(stand);
            p.addPassenger(entity2);
            p.hideEntity(NametagOcclusion.instance, stand);
        });
        stands.put(p.getUniqueId(), entity);
    }
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event){
        var p = event.getPlayer();
        var stand = stands.get(p.getUniqueId());
        var sf = fish.get(p.getUniqueId());
        stand.remove();
        sf.remove();
        stands.remove(p.getUniqueId());
        fish.remove(p.getUniqueId());
        NametagOcclusion.invisibleTeam.removePlayer(p);
    }
}
