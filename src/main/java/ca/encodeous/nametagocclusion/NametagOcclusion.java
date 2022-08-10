package ca.encodeous.nametagocclusion;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public final class NametagOcclusion extends JavaPlugin {
    static Team invisibleTeam;
    protected static NametagOcclusion instance;
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        var sb = Bukkit.getScoreboardManager().getMainScoreboard();
        if(sb.getTeam("nto-team") == null){
            invisibleTeam = sb.registerNewTeam("nto-team");
        }else{
            invisibleTeam = sb.getTeam("nto-team");
        }
        invisibleTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(var entity : EventListener.stands.values()){
            entity.remove();
        }
        for(var entity : EventListener.fish.values()){
            entity.remove();
        }
    }
}
