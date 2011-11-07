package jadon.mahoutsukaii.plugins.reservedlist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import ru.tehkode.permissions.bukkit.PermissionsEx;


@SuppressWarnings("deprecation")
public class ReservedList extends JavaPlugin{

	Plugin permissionsEx; 
	
	private final ReservedListPlayerListener reservedListPlayerListener = new ReservedListPlayerListener(this);
	private final ReservedListServerListener reservedListServerListener = new ReservedListServerListener(this);

	public List<String> vips;
	public static int vipSlots;
	public static int maxSlots;
	
	public int actualMaxSlots;

	public Configuration properties = new Configuration(new File("plugins/ReservedList/config.yml"));
	public String maindir = "plugins/ReservedList/";
	
	public void onDisable() {

		System.out.print(this + " is now disabled!");
	}

	public void getData()
	{
		vipSlots = properties.getInt("vipSlots", 10);
		maxSlots = properties.getInt("maxSlots", 50);
		
		vips = properties.getStringList("vips", null);
		
		for(int i=0,l=vips.size();i<l;++i)
		{
		  vips.add(vips.remove(0).toLowerCase());
		} 
		
		 // System.out.print(vips);

	}

	public void onEnable() {
		
		new File(maindir).mkdir();
		createDefaultConfiguration("config.yml");
		
		permissionsEx = getServer().getPluginManager().getPlugin("PermissionsEx");
		
		//register events
		
		PluginManager pm = getServer().getPluginManager();
		

		
		pm.registerEvent(Event.Type.SERVER_LIST_PING, reservedListServerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, reservedListPlayerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_LOGIN, reservedListPlayerListener, Priority.Highest, this);

		properties.load();
		getData();
		this.actualMaxSlots = maxSlots;
	//	System.out.print(this + " is now enabled!");
		
		
	}
	
	
	

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if(command.getName().toLowerCase().equals("addvip"))
		{
			return addVIP(sender, args);
		}
		return false;
	}

	public boolean addVIP(CommandSender sender, String[] args)
	{
		
		if(args.length < 1)
		return false;
		
		
		String playerName = args[0];
		boolean auth;
		auth = false;
		if(sender instanceof Player)
		{
			if(sender.hasPermission("reservedlist.addvip"))
			{
				auth=true;
			}
		    if(permissionsEx!=null)
		           if( ((PermissionsEx) permissionsEx).getPermissionManager().has((Player)sender, "reservedlist.addvip")) auth=true;

		}
		else auth=true;
		if(!auth)
			return false;
		
		if(!vips.contains(playerName.toLowerCase()))
		{
			vips.add(playerName.toLowerCase());
			properties.load();
			properties.setProperty("vips", vips);
			properties.save();
			
			sender.sendMessage(ChatColor.GREEN + playerName + " was added to the VIP list.");
			return true;
		}
		sender.sendMessage(ChatColor.GREEN + playerName + " is already on the list!");
		return true;
	}
	
	public int getActualMax()
	{
		if(getRemainingSlots() >= 0)
		return actualMaxSlots;
		else
			return getServer().getOnlinePlayers().length;
	}
	

	public int getRemainingSlots()
	{
		return	maxSlots - getServer().getOnlinePlayers().length;
	}
	
	public int getRemainingVIPSlots()
	{
		return maxSlots + vipSlots - getServer().getOnlinePlayers().length;
		
	}
	
	public boolean isPlayerVIP(Player player)
	{
		if(player.hasPermission("reservedlist.vip"))
		{
			return true;
		}
		if(vips.contains(player.getName().toLowerCase()))
			return true;
		else
		return false;
	}
	
	protected void createDefaultConfiguration(String name) {
		File actual = new File(getDataFolder(), name);
		if (!actual.exists()) {

			InputStream input =
				this.getClass().getResourceAsStream("/defaults/" + name);
			if (input != null) {
				FileOutputStream output = null;

				try {
					output = new FileOutputStream(actual);
					byte[] buf = new byte[8192];
					int length = 0;
					while ((length = input.read(buf)) > 0) {
						output.write(buf, 0, length);
					}

					System.out.println(getDescription().getName()
							+ ": Default configuration file written: " + name);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (input != null)
							input.close();
					} catch (IOException e) {}

					try {
						if (output != null)
							output.close();
					} catch (IOException e) {}
				}
			}
		}
	}
	

}
