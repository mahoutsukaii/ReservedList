package jadon.mahoutsukaii.plugins.reservedlist;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ReservedListPlayerListener extends PlayerListener {
	
	ReservedList plugin;
	
	public ReservedListPlayerListener(ReservedList instance)
	{
		this.plugin = instance;
	}





	public void onPlayerLogin(PlayerLoginEvent event) {
		
		if(plugin.getRemainingSlots() <= 0)
		{
			if(!plugin.isPlayerVIP(event.getPlayer()))
			{
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "This server is full!");
			}
			else
			{
				if(plugin.getRemainingVIPSlots() > 0)
				{
					return;
				}
				else
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "This server is full!");
			}
		}
	
	}



}
