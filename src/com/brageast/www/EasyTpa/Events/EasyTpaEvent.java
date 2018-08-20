package com.brageast.www.EasyTpa.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EasyTpaEvent implements Listener {
//	private int a;
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().startsWith("/tpa ")) {
			e.setMessage(e.getMessage().replace("/tpa", "/etpa a"));
//			e.getPlayer().sendMessage("Tip:");
			
		} else if(e.getMessage().startsWith("/tpahere")) {
			e.setMessage(e.getMessage().replace("/tpahere", "/etpa invite"));
		}
	}
//	@EventHandler
//	public void onPCommand(PlayerCommandPreprocessEvent e) {
//		String Equ = "/argee";
//		e.getMessage().equals(Equ);
////		if(e.getMessage().equals("/argee")) {
////			a = 1;
////		}
//		switch(Equ) {
//		case "/argee":
//			a = 1;
//			e.setCancelled(true);
//			break;
//		case "/disagree":
//			a = 2;
//			e.setCancelled(true);
//			break;
//		}
//		
//	}
//	public int getA() {
//		return a;
//	}
//	
}
