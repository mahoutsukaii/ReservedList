package jadon.mahoutsukaii.plugins.reservedlist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;


@SuppressWarnings("deprecation")
public class ReservedList extends JavaPlugin{

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
		
		System.out.print(vips);

	}

	public void onEnable() {
		
		new File(maindir).mkdir();
		createDefaultConfiguration("config.yml");
		
		//register events
		
		PluginManager pm = getServer().getPluginManager();
		

		
		pm.registerEvent(Event.Type.SERVER_LIST_PING, reservedListServerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, reservedListPlayerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_LOGIN, reservedListPlayerListener, Priority.Highest, this);

		properties.load();
		getData();
		this.actualMaxSlots = maxSlots;
		System.out.print(this + " is now enabled!");
		
		

		
	}
	
	public int getActualMax()
	{
		return actualMaxSlots;
	}
	
	public void addNewSpot()
	{
		actualMaxSlots++;
	}
	
	public void takeAwaySpot()
	{
		actualMaxSlots--;
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
