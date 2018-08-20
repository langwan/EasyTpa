package com.brageast.www.EasyTpa;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.brageast.www.EasyTpa.Command.EasyTpaCmd;
import com.brageast.www.EasyTpa.Events.EasyTpaEvent;

public class EasyTpaMain extends JavaPlugin {
	public void onEnable() {
		getLogger().info("感谢您使用EasyTpa!作者:ChenMo");
		getLogger().info("如果出现BUG请QQ2010557767!");
		onRegisterEvents();
		onPluginCommand();
	}
	private void onRegisterEvents() {
		Bukkit.getPluginManager().registerEvents(new EasyTpaEvent(), this);
	}
	private void onPluginCommand() {
		Bukkit.getPluginCommand("etpa").setExecutor(new EasyTpaCmd(this));
	}
}
