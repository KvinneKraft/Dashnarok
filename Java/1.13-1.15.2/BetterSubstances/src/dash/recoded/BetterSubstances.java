
package dash.recoded;

import com.sun.tools.javac.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BetterSubstances extends JavaPlugin
{
    FileConfiguration config;
    JavaPlugin plugin;
    
    final HashMap<ItemStack, Integer> substances = new HashMap<>(); 
    
    final List<List<PotionEffect>> substance_genetics = new ArrayList<>();    
    final List<Integer> substance_cooldown = new ArrayList<>();    
    final List<String> permission_sets = new ArrayList<>();
    
    String admin_permission;
    
    ItemStack ObtainSubstance(final String identity)
    {
        try
        {
            final Material substance_m = Material.getMaterial(identity.toUpperCase().replace(" ", "_"));            
            return new ItemStack(substance_m, 1);
        }
        
        catch (final Exception e)
        {
            return null;
        }
    };
    
    PotionEffect ObtainGenetics(final List<String> genetic)
    {
        try
        {
            if (genetic.size() < 3)
            {
                return null;
            };
            
            final PotionEffectType type = (PotionEffectType) PotionEffectType.getByName(genetic.get(0).toUpperCase().replace("EFFECT:", ""));
            final int strength = (int) Integer.parseInt(genetic.get(1).toUpperCase().replace("STRENGTH:", ""));
            final int duration = (int) Integer.parseInt(genetic.get(2).toUpperCase().replace("DURATION:", ""));
            
            return new PotionEffect(type, duration * 20, strength - 1); 
        }
        
        catch (final Exception e)
        {
            return null;
        }
    };
    
    Integer GetInteger(final String string)
    {
        try
        {
            return Integer.parseInt(string);
        }
        
        catch (final Exception e)
        {
            return 30;
        }
    };
    
    void LoadFancyConfiguration()
    {
        saveDefaultConfig();
        
        if (plugin != this)
            plugin = (JavaPlugin) this;
        
        plugin.reloadConfig();
        config = (FileConfiguration) plugin.getConfig();
        
        for (final String line : config.getStringList("substance-table"))// One Substance by itself
        {
            final List<String> pure = Arrays.asList(line.replace(" ", "").split(","));
            final ItemStack substance = ObtainSubstance(pure.get(0));
            
            if (pure.size() < 6 || substance == null)
            {
                print("Invalid substance syntax found in config.yml!\r\nSkipping ....");
                continue;
            };
            
            final String s_name = color(pure.get(1));
            final String s_lore = color(pure.get(2));
            
            substance.getItemMeta().setDisplayName(s_name);
            substance.getItemMeta().setLore(Arrays.asList(s_lore));          
            
            if (substances.containsKey(substance))
            {
                print("Duplicated substance found in config.yml!\r\nSkipping....");
                continue;
            };
            
            final List<PotionEffect> genetic_cache = new ArrayList<>();
            
            for (final String genetic_effect : pure.get(3).split("&"))
            {
                final PotionEffect s_genetic = ObtainGenetics(Arrays.asList(genetic_effect.split("-")));

                if (s_genetic == null)
                {
                    print("Invalid substance genetics found in config.yml!\r\nSkipping....");
                    continue;
                };

                genetic_cache.add(s_genetic);
            };
            
            substance_genetics.add(genetic_cache);
            
            final int cooldown = GetInteger(pure.get(4));
            
            switch (cooldown)
            {
                case 30:
                {
                    print("Invalid substance cooldown found in config.yml!\r\nUsing default (30s)....");                    
                };
                
                default:
                {
                    substance_cooldown.add(cooldown);                    
                    permission_sets.add(pure.get(5));                                                              
                };
            }; 
            
            substances.put(substance, substances.size());            
        };
    };    
    
    @Override public void onEnable()
    {
        print("I am loading ....");
        
        LoadFancyConfiguration();
        
        getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("bettersubstances").setExecutor(new Commands());
        
        final String greeting = 
        (
            "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\r\n" +
            "Author: KvinneKraft (Dashie)\r\n" +
            "Version: 1.0\r\n" +
            "Contact: KvinneKraft@protonmail.com\r\n" +
            "Source: https://github.com/KvinneKraft \r\n" +
            "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\r\n" 
        );        
        
        print(greeting + "I have been loaded!");
    };
    
    class Events implements Listener
    {
        final List<Pair<Player, ItemStack>> queue = new ArrayList<>();
        
        @EventHandler public void PotentialSubstanceUse(final PlayerInteractEvent e)
        {
            final ItemStack substance = (ItemStack) e.getItem();
            
            if (substance == null) 
                return;
            
            substance.setAmount(1);
            
            if (!substances.containsKey(substance))
                return;
            
            final int s_identifier = substances.get(substance);
            final Player p = (Player) e.getPlayer();
            
            if (!p.hasPermission(permission_sets.get(s_identifier)))
                return;
            
            final Pair s_pair = new Pair(p, substance);            
            
            if (queue.contains(s_pair))
            {
                p.sendMessage(color("&cYou are on a cooldown of " + substance_cooldown.get(s_identifier) + " !"));
                return;
            }
            
            p.addPotionEffects(substance_genetics.get(s_identifier));
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            
            if (!queue.contains(s_pair) && !p.hasPermission(admin_permission))
            {
                queue.add(s_pair);
                
                getServer().getScheduler().runTaskLaterAsynchronously
                (
                    plugin,

                    new Runnable()
                    {
                        @Override public void run()
                        {
                            if (queue.contains(s_pair))
                            {
                                queue.remove(s_pair);
                            };

                            if (p.isOnline())
                            {
                                p.sendMessage(color("&aYou may consume another one of those &d" + substance.getItemMeta().getDisplayName() + " &a!"));
                            };
                        };
                    },

                    substance_cooldown.get(s_identifier) * 20
                );
            };
        };
    };
    
    class Commands implements CommandExecutor
    {
        @Override public boolean onCommand(final CommandSender s, final Command c, String a, final String[] as)
        {
            if (!(s instanceof Player))
            {
                print("You may only use this command as a player!");
                return false;
            };
            
            final Player p = (Player) s;
            
            if (as.length >= 1)
            {
                a = as[0].toLowerCase();
                
                if (a.equals("reload"))
                {
                    p.sendMessage(color("&eLoading ...."));
                    
                    LoadFancyConfiguration();
                    
                    p.sendMessage(color("&eDone!"));
                    
                    return true;
                }
                
                else if (a.equals("give"))
                {
                    return true;
                }
                
                else if (a.equals("list"))
                {
                    return true;
                };
            };
            
            p.sendMessage(color("&cCorrect syntax: &4/bs [reload | list | give] <amount> <player>"));            
            return true;
        };
    };
    
    @Override public void onDisable()
    {
        print("Well, now I am dead!");
    };
    
    void print(final String line)
    {
        System.out.println("(Better Substances): " + line);
    };
    
    String color(final String line)
    {
        return ChatColor.translateAlternateColorCodes('&', line);
    };
};