package jadon.mahoutsukaii.plugins.reservedlist;

import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.server.ServerListener;

public class ReservedListServerListener extends ServerListener {

	ReservedList plugin;
	
	public ReservedListServerListener(ReservedList instance)
	{
		this.plugin = instance;
	}

	public void onServerListPing(ServerListPingEvent event) {

		event.setMaxPlayers(plugin.getActualMax());
		
	}

}
