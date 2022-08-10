package mc.photon.hidenametag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import me.clip.placeholderapi.PlaceholderAPI;

public final class core extends JavaPlugin implements Listener {
	private Team team;
	private Scoreboard scoreboard;
	@Override
	public void onEnable() {
		saveDefaultConfig();
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		team = scoreboard.registerNewTeam("HideNameTag");
		team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		team.setCanSeeFriendlyInvisibles(false);
		Bukkit.getPluginManager().registerEvents(this, this);
		if (!Bukkit.getOnlinePlayers().isEmpty())
			Bukkit.getOnlinePlayers().forEach(player -> hideNameTag(player));
	}
	public void hideNameTag(Player player) {
		player.setScoreboard(scoreboard);
		team.addEntry(player.getName());
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		hideNameTag(event.getPlayer());
	}
	@EventHandler
	public void onClick(PlayerInteractAtEntityEvent event) {
		if (!(event.getRightClicked() instanceof Player)) return;
		String msgClick = getConfig().getString("messages.onClick");
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
			msgClick = PlaceholderAPI.setPlaceholders(((Player) event.getRightClicked()).getPlayer(), msgClick);
		String ActionBarText = msgClick;
		new BukkitRunnable() {
			private int i = 0;
			@Override
			public void run() {
				if (i >= 1) cancel();
				++i;
				event.getPlayer().sendActionBar('&', ActionBarText);
			}
		}.runTaskTimer(core.this, 0L, 20L);
	}
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}