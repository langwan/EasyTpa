package com.brageast.www.EasyTpa.Command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.brageast.www.EasyTpa.EasyTpaMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class EasyTpaCmd implements CommandExecutor {
	private HashMap<String,Integer> si = new HashMap<String,Integer>();
	private final EasyTpaMain plugin;
	public EasyTpaCmd(EasyTpaMain plugin) {
		this.plugin = plugin;;
	}
	private static final String t = "&7[&bEasyTpa&7] &3: &r".replace("&", "§");	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(t+"这是玩家指令,后台无法执行");
			return true;
		} else if(!(sender.hasPermission("etpa.use"))) {
			sender.sendMessage(t+"您没有足够的权限");
			return true;
		} else if(args.length == 0){
			Player p1 = (Player)sender;
			onHelp(p1, args);
			return true;
		} else {
			Player p1 = (Player)sender;
//			Player p2 = Bukkit.getPlayer(args[1]);
			switch (args[0]) {
			case "help":
				onHelp(p1, args);
				return true;
			case "a":
				Player p2 = Bukkit.getPlayer(args[1]);
				onCs(p1, p2, args);
				return true;
			case "agree":
				onArgee(p1, args);
				return true;
			case "disagree":
				onDiagree(p1, args);
				return true;
			case "invite":
				onInvite(p1, args);
				return true;
			default:
				onHelp(p1, args);
				return true;
			}
//			if(args[0].equals("help")) {
//				onHelp(p1, args);
//				return true;
//			} else if(args[0].equals("a")) {
//				Player p2 = Bukkit.getPlayer(args[1]);
//				onCs(p1, p2, args);
//				return true;
//			} else {
//				onHelp(p1, args);
//				return true;
//			}
		}
	}
	private void onInvite(Player p1, String[] args) {
		Player p2 = Bukkit.getPlayer(args[1]);
		if(args.length != 2) {
			p1.sendMessage(t+"您的指令不正确,正确格式:/tpa invite [玩家名称]!");
		} else if(p1 == p2) {
			p1.sendMessage(t+"你不能跟自己交易!");
		} else if(p2 == null) {
			p1.sendMessage(t+args[1]+"不在线或者不存在!");
		} else if(si.get(p2.getName()) != null) {
			p1.sendMessage(t+"很抱歉你或者"+p2.getDisplayName()+"有请求,请稍后在尝试!或者点击下方的文字再次尝试!");
			TextComponent hp2 = new TextComponent(t+"再次骚扰 "+p2.getDisplayName()+"!");
			hp2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa invite "+p2.getDisplayName()));
			hp2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("再次骚扰!").color(ChatColor.BLUE).create()));
			p1.spigot().sendMessage(hp2);
		} else {
			Location l1 = p1.getLocation();
//			Location l2 = p2.getLocation();
			TextComponent a1 = new TextComponent(("&7[&aYES&7]   &m&l|&b"+p2.getDisplayName()+"&7请求你传送到他位置!").replace("&", "§"));
			a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/etpa agree"));
			a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("同意请求!").color(ChatColor.BLUE).create()));
			TextComponent a2 = new TextComponent(("&7[&aNO&7]    &m&l|&7鼠标点击Yes或No!").replace("&", "§"));
			a2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/etpa disagree"));
			a2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("拒绝请求!").color(ChatColor.BLUE).create()));
			p1.sendMessage(t+"正在请求"+p2.getDisplayName()+"同意!");
			p2.sendMessage(("&6&m&l|&a&m&l---&8&m&l---------&7&l[&bEasyTpa&7&l]&8&m&l----------&a&m&l---&6&m&l|").replace("&", "§"));
			p2.spigot().sendMessage(a1);
			p2.spigot().sendMessage(a2);
			p2.sendMessage(("&6&m&l|&a&m&l--------------------------------&6&m&l|").replace("&", "§"));
			new BukkitRunnable() {
				
				int time = 10;
				@Override
				public void run() {
					if(si.get(p2.getName()) == null) {
						si.put(p2.getName(), 1);
					}
					if(si.get(p2.getName()) == 2) {
						p2.teleport(l1);
						si.remove(p2.getName());
						p1.sendMessage(t+p2.getDisplayName()+"已经同意了您的请求,请稍等!");
						cancel();
					} else if(si.get(p2.getName()) == 3) {
						p1.sendMessage(t+p2.getDisplayName()+"拒绝了这次传送!");
						si.remove(p2.getName());
						cancel();
					} else if(time == 0) {
						p1.sendMessage(t+"终止了这次穿送!");
						p2.sendMessage(t+"终止了传送!");
						si.remove(p2.getName());
						cancel();
					}
					time--;
				}
			}.runTaskTimer(this.plugin, 0L, 200);
		}
	}
	private void onDiagree(Player p1, String[] args) {
		if(args.length != 1) {
			TextComponent a1 = new TextComponent(t+"不要手输入,请点击我!");
//			hp1.setColor(ChatColor.GRAY); //灰色
			a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/etpa disagree"));
			a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("拒绝请求!").color(ChatColor.BLUE).create()));
			p1.spigot().sendMessage(a1);
		} else if(si.get(p1.getName()) == null) {
			p1.sendMessage(t+"您没有任何请求");
		} else {
			si.remove(p1.getName());
			si.put(p1.getName(), 3);
			p1.sendMessage(t+"您拒绝了传送请求!");
		}
		
		
	}
	//拒绝
	private void onArgee(Player p1, String[] args) {
		if(args.length != 1) {
			TextComponent a1 = new TextComponent(t+"不要手输入,请点击我!");
			a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/etpa agree"));
			a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("同意请求!").color(ChatColor.BLUE).create()));
			p1.spigot().sendMessage(a1);
		} else if(si.get(p1.getName()) == null) {
			p1.sendMessage(t+"您没有任何请求");
		} else {
			si.remove(p1.getName());
			si.put(p1.getName(), 2);
			p1.sendMessage(t+"您接受了传送请求");
		}
		
	}
	private void onCs(Player p1, Player p2, String[] args) {
		if(args.length != 2) {
			p1.sendMessage(t+"您的指令不正确,正确格式:/tpa [玩家名称]!");
		} else if(p1 == p2) {
			p1.sendMessage(t+"你不能跟自己交易!");
		} else if(p2 == null) {
			p1.sendMessage(t+args[1]+"不在线或者不存在!");
		} else if(si.get(p2.getName()) != null) {
			p1.sendMessage(t+"很抱歉你或者"+p2.getDisplayName()+"有请求,请稍后在尝试!或者点击下方的文字再次尝试!");
			TextComponent hp2 = new TextComponent(t+"再次骚扰 "+p2.getDisplayName()+"!");
			hp2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa "+p2.getDisplayName()));
			hp2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("再次骚扰!").color(ChatColor.BLUE).create()));
			p1.spigot().sendMessage(hp2);
		} else {
//			Location l1 = p1.getLocation();
			Location l2 = p2.getLocation();
			TextComponent a1 = new TextComponent(("&7[&aYES&7]   &m&l|&b"+p2.getDisplayName()+"&7请求传送到你的位置!").replace("&", "§"));
			a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/etpa agree"));
			a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("同意请求!").color(ChatColor.BLUE).create()));
			TextComponent a2 = new TextComponent(("&7[&aNO&7]    &m&l|&7鼠标点击Yes或No!").replace("&", "§"));
			a2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/etpa disagree"));
			a2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("拒绝请求!").color(ChatColor.BLUE).create()));
			p1.sendMessage(t+"正在请求"+p2.getDisplayName()+"同意!");
			p2.sendMessage(("&6&m&l|&a&m&l---&8&m&l---------&7&l[&bEasyTpa&7&l]&8&m&l----------&a&m&l---&6&m&l|").replace("&", "§"));
			p2.spigot().sendMessage(a1);
			p2.spigot().sendMessage(a2);
			p2.sendMessage(("&6&m&l|&a&m&l--------------------------------&6&m&l|").replace("&", "§"));
			new BukkitRunnable() {
				
				int time = 10;
				@Override
				public void run() {
					if(si.get(p2.getName()) == null) {
						si.put(p2.getName(), 1);
					}
					if(si.get(p2.getName()) == 2) {
						p1.teleport(l2);
						si.remove(p2.getName());
						p1.sendMessage(t+p2.getDisplayName()+"已经同意了您的请求,请稍等!");
						cancel();
					} else if(si.get(p2.getName()) == 3) {
						p1.sendMessage(t+p2.getDisplayName()+"拒绝了这次传送!");
						si.remove(p2.getName());
						cancel();
					} else if(time == 0) {
						p1.sendMessage(t+"终止了这次穿送!");
						p2.sendMessage(t+"终止了传送!");
						si.remove(p2.getName());
						cancel();
					}
					time--;
				}	
			}.runTaskTimer(this.plugin, 0L, 200);
		} 		
	}
	private void onHelp(Player p1, String[] args) {
		TextComponent hp1 = new TextComponent("/eTpa a [Player] 传送玩家,默认替换/tpa");
		hp1.setColor(ChatColor.GRAY); //灰色
		hp1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpa [Player]"));
		hp1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Tp传送莫个人!").color(ChatColor.BLUE).create()));
		p1.sendMessage("§m§a---§m§6--------§3 : "+t+"§m§a--------§m§6---");
		p1.spigot().sendMessage(hp1);
		TextComponent hp2 = new TextComponent("/eTpa agree 同意请求!");
		hp2.setColor(ChatColor.GRAY); //灰色
		hp2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/etpa agree"));
		hp2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("同意请求!").color(ChatColor.BLUE).create()));
		p1.spigot().sendMessage(hp2);
		TextComponent hp3 = new TextComponent("/eTpa disagree 拒绝请求!");
		hp3.setColor(ChatColor.GRAY); //灰色
		hp3.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/etpa disagree"));
		hp3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("拒绝请求!").color(ChatColor.BLUE).create()));
		p1.spigot().sendMessage(hp3);
		TextComponent hp4 = new TextComponent("/eTpa invite [Player]将玩家传送到你的位置,默认替换/tpahere!");
		hp4.setColor(ChatColor.GRAY); //灰色
		hp4.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/eTpa invite [Player]"));
		hp4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("邀请莫个人!").color(ChatColor.BLUE).create()));
		p1.spigot().sendMessage(hp4);
	}
	
}
