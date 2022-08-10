package ca.encodeous.nametagocclusion;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashSet;

public final class NametagOcclusion extends JavaPlugin {
    static Team invisibleTeam;
    private ProtocolManager protocolManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }
        var sb = Bukkit.getScoreboardManager().getMainScoreboard();
        if(sb.getTeam("nto-team") == null){
            invisibleTeam = sb.registerNewTeam("nto-team");
        }else{
            invisibleTeam = sb.getTeam("nto-team");
        }
        invisibleTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);

        protocolManager.addPacketListener(
                new PacketAdapter(this, ListenerPriority.NORMAL,
                        PacketType.Play.Server.SPAWN_ENTITY) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        var destPlayer = event.getPlayer();
                        if (event.getPacketType() ==
                                PacketType.Play.Server.SPAWN_ENTITY) {
                            var packet = (ClientboundAddEntityPacket)event.getPacket().getHandle();
                            if(EventListener.stands.get(destPlayer.getUniqueId()).getUniqueId() == packet.getUUID()){
                                event.setCancelled(true);
                            }
                            if(EventListener.fish.get(destPlayer.getUniqueId()).getUniqueId() == packet.getUUID()){
                                event.setCancelled(true);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
