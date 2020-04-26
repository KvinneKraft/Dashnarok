
// Author: Dashie
// Version: 1.0

package com.philosophy;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor
{
    public static final double x = 246.490, y = 71.48689, z = -236.497;
    private final List<Player> players = new ArrayList<>();
    
    
    @Override public boolean onCommand(final CommandSender s, final Command c, final String a, final String[] as)
    {
        if (!(s instanceof Player))
        {
            return false;
        };
        
        final Player p = (Player) s;
        
        if (players.contains(p))
        {
            p.sendMessage(Freya.color("&cA teleportation is already commencing!"));
            return false;
        }
        
        else if (p.isOp())
        {
            p.teleport
            (
                new Location
                (
                    p.getWorld(), x, y, z
                )
            );
            
            return true;
        };
        
        p.sendMessage(Freya.color("&aYou will be teleported to spawn in 4 seconds ...."));
        
        Bukkit.getServer().getScheduler().runTaskLater
        (
            Consilience.plugin, 
                
            new Runnable() 
            { 
                @Override public void run() 
                { 
                    if (p.isOnline())
                    {
                        p.teleport
                        (
                            new Location
                            (
                                p.getWorld(), x, y, z
                            )
                        );
                        
                        p.sendMessage(Freya.color("&aYou have been teleported to safety!"));
                    };
                    
                    players.remove(p);
                }; 
            }, 
            
            4 * 20
        );
        
        return true;
    };
};