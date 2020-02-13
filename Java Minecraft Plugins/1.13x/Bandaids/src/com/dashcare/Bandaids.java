
// Author: Dashie
// Version: 1.0

package com.dashcare;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Bandaids extends JavaPlugin
{
    public static FileConfiguration config;
    public static JavaPlugin plugin;
    
    
    public static CommandsHandler commands = new CommandsHandler();
    public static EventsHandler events = new EventsHandler();
    
    
    @Override
    public void onEnable()
    {
        Lunaris.print("Loading the plugin ....");
        
        saveDefaultConfig();
        
        plugin = this;
               
        commands.refresh_data();
        events.refresh_data();
        
        refresh_data();        
        
        Lunaris.print("Plugin has been loaded!");
    };
    
    
    @Override
    public void onDisable()
    {
        Lunaris.print("Plugin has been disabled!");
    };
  
    
    static List<String> bandaid_lore = new ArrayList<>();
    static Material bandaid_material;    
    static String bandaid_name;
    
    
    public void refresh_data()
    {
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        bandaid_lore = config.getStringList("bandaid-properties.bandaid-lore");
        for(Integer id = 0; id < bandaid_lore.size(); id += 1)
        {
            bandaid_lore.set(id, Lunaris.colors(bandaid_lore.get(id)));
        };
        
        bandaid_material = Material.getMaterial(config.getString("bandaid-properties.bandaid-material").toUpperCase().replace(" ", "_"));
        if(bandaid_material == null)
        {
            Lunaris.print("The given material is invalid, setting it to PAPER!");
            bandaid_material = Material.PAPER;
        };
        
        bandaid_name = Lunaris.colors(config.getString("bandaid-properties.bandaid-name"));
    };
    
    public static ItemStack get_bandaid(Integer amount)
    {
        ItemStack bandaid = new ItemStack(bandaid_material, amount);
        ItemMeta bandaid_meta = bandaid.getItemMeta();
        
        bandaid_meta.setCustomModelData(2020);
        bandaid_meta.setDisplayName(bandaid_name);
        bandaid_meta.setLore(bandaid_lore);
        
        return bandaid;
    };
};


class CommandsHandler implements CommandExecutor
{
    boolean t = true, f, developer_support;
    
    String admin_permission, receive_message, give_message, get_message, correct_syntax_message, player_offline_message, invalid_amount_message, plug_message, permission_denied_message;
    
    
    public void refresh_data()
    {
        Bandaids.plugin.reloadConfig();
        Bandaids.config = Bandaids.plugin.getConfig();
    
        FileConfiguration config = Bandaids.config;
        
        developer_support = config.getBoolean("optional-properties.dev-support");
        
        // Work on dis \/ <---
        permission_denied_message = Lunaris.colors("");                
        player_offline_message = Lunaris.colors("");
        invalid_amount_message = Lunaris.colors("");
        
        correct_syntax_message = Lunaris.colors("");
        plug_message = Lunaris.colors("");        
        
        receive_message = Lunaris.colors(config.getString("optional-properties."));
        give_message = Lunaris.colors(config.getString("optional-properties."));
        get_message = Lunaris.colors(config.getString("optional-properties."));        
    
        admin_permission = config.getString("optional-properties.admin-permission");               
    };
    
    
    @Override
    public boolean onCommand(CommandSender s, Command c, String a, String[] as)
    {
        if((s instanceof Player))
            return f;
        
        Player p = (Player) s;
        
        if(!(p.hasPermission(admin_permission)))
        {
            if(developer_support)
            {
                p.sendMessage(plug_message);
            }
            
            else
            {
                p.sendMessage(permission_denied_message);
            };
            
            return f;
        }
        
        else if(as.length < 1)
        {
            p.sendMessage(correct_syntax_message);
            return f;
        };
        
        a = as[0].toLowerCase();
        
        // Syntax: /bandaids [get | give] <player> <amount>
        
        if((a.equals("get")) || (a.equals("give")))
        {
            Player target = p;
            
            if((a.equals("give")) && (as.length<3))
            {
                p.sendMessage(correct_syntax_message);
                return f;
            };
            
            Integer amount = 1;
            
            if((a.equals("give")) && (as.length>=3))
            {
                target = Bukkit.getPlayerExact(as[1]);
                
                if(target == null)
                {
                    p.sendMessage(player_offline_message);
                    return f;
                };
                
                amount = Integer.valueOf(as[2]);                
            }
            
            else if((a.equals("get")) && (as.length>=2))
            {
                amount = Integer.valueOf(as[2]);
            };
            
            if((amount == null) || (amount < 1))
            {
                p.sendMessage(invalid_amount_message);
                return f;
            };            
            
            ItemStack bandaid = Bandaids.get_bandaid(amount);
            target.getInventory().addItem(bandaid);
            
            if(target != p)
            {
                target.sendMessage(receive_message);
                p.sendMessage(give_message);                
            }
            
            else
            {
                p.sendMessage(get_message);
            };
        }
        
        else
        {
            p.sendMessage(correct_syntax_message);
            return f;
        };
        
        return t;
    };
};


class EventsHandler implements Listener
{
    List<PotionEffect> potion_effects = new ArrayList<>();
    
    boolean summon_lightning, summon_fireworks;
    
    String use_permission, deny_message, apply_message;
    
    
    public void refresh_data()
    {
        Bandaids.plugin.reloadConfig();;
        Bandaids.config = Bandaids.plugin.getConfig();
        
        FileConfiguration config = Bandaids.config;
        
        summon_lightning = config.getBoolean("bandaid-properties.summon-lightning");
        summon_fireworks = config.getBoolean("bandaid-properties.summon-fireworks");
        
        use_permission = Lunaris.colors(config.getString("bandaid-properties.use-permission"));
        apply_message = Lunaris.colors(config.getString("bandaid-properties.apply-message"));
        deny_message = Lunaris.colors(config.getString("bandaid-properties.permission-deny-message"));
        
        if(potion_effects.size() > 0)
        {
            potion_effects.clear();
        };
        
        for(String str : config.getStringList("bandaid-properties.bandaid-effects"))
        {
            String[] arr = str.toUpperCase().split(" ");
            
            if(arr.length < 3)
            {
                Lunaris.print("Found invalid bandaid effect format, skipping ....");
                continue;
            };
            
            PotionEffectType effect = PotionEffectType.getByName(arr[0]);
            
            if(effect == null)
            {
                Lunaris.print("Found invalid bandaid effect type, skipping ....");
                continue;
            };
            
            Integer amplifier = Integer.valueOf(arr[1]);
            Integer duration = Integer.valueOf(arr[2]);            
            
            if((duration == null) || (amplifier == null))
            {
                Lunaris.print("You must specify a correct amplifier and or duration.");
                continue;
            }            
            
            else if(amplifier < 1)
            {
                Lunaris.print("Amplifier for bandaid effect is too low, skipping ....");
                continue;
            }
            
            else if(duration < 1)
            {
                Lunaris.print("The duration is too low, skipping ....");
                continue;
            };
            
            potion_effects.add(new PotionEffect(effect, amplifier, duration));
        };
    };
    
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        ItemStack item = new ItemStack(e.getItem().getType(), 1);
        
        if((item == null) || (!item.hasItemMeta()) || (item.getItemMeta().getCustomModelData() != 2020))
        {
            return;
        };
        
        Player p = e.getPlayer();
        
        if(!p.hasPermission(use_permission))
        {
            p.sendMessage(deny_message);
            return;
        }
        
        else if(potion_effects.size() > 0)
        {
            p.addPotionEffects(potion_effects);
        };
        
        p.getInventory().remove(item);        
        
        if((summon_lightning) || (summon_fireworks))
        {
            Location location = p.getLocation();
            
            p.setInvulnerable(true);
            
            if(summon_lightning)
            {
                location.getWorld().strikeLightningEffect(location);
            };
            
            if(summon_fireworks)
            {
                Random rand = new Random();
                
                Lunaris.dashworks(new Integer[] { rand.nextInt(255), rand.nextInt(255), rand.nextInt(255) }, location);
            };
            
            p.setInvulnerable(false);
        };
        
        p.sendMessage(apply_message);
    };
};



class Lunaris
{
    public static void print(String str)
    {
        System.out.println("(Dash Bandaids): " + str);
    };
    
    
    public static String colors(String str)
    {
        return ChatColor.translateAlternateColorCodes('&', str);
    };
    
    
    static List<FireworkEffect.Type> firework_effect_types = new ArrayList<>(
        Arrays.asList(
            new FireworkEffect.Type[]
            {
                FireworkEffect.Type.BALL, 
                FireworkEffect.Type.BALL_LARGE,
                FireworkEffect.Type.BURST,
                FireworkEffect.Type.CREEPER,
                FireworkEffect.Type.STAR,
            }
        )
    );
    
    
    public static void dashworks(Integer[] rgb, Location location)
    {
        Color color = Color.fromRGB(rgb[0], rgb[1], rgb[2]);
        
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta firework_meta = firework.getFireworkMeta();
        
        FireworkEffect.Type firework_effect_type = firework_effect_types.get(new Random().nextInt(firework_effect_types.size()));
        firework_meta.addEffect(FireworkEffect.builder().withColor(color).withFlicker().withTrail().with(firework_effect_type).flicker(true).build());
        
        firework.setFireworkMeta(firework_meta);
        firework.detonate();
    };
};
