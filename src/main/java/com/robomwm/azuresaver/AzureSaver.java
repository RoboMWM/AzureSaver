package com.robomwm.azuresaver;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created on 11/28/2019.
 *
 * @author RoboMWM
 */
public class AzureSaver extends Plugin
{
    @EventHandler
    public void onJoin(PostLoginEvent event)
    {
        if (this.getProxy().getPlayers().size() > 1)
            return;
        //TODO: turn on VM
        //wait for VM to start??
        ServerInfo server = this.getProxy().getServerInfo("mlg");
        event.getPlayer().connect(server);
    }

    @EventHandler
    public void onLastLeave(PlayerDisconnectEvent event)
    {
        if (this.getProxy().getPlayers().size() > 0) //is player removed from this list before or after this event?
            return;

        getProxy().getScheduler().schedule(this, new Runnable()
        {
            @Override
            public void run()
            {
                if (getProxy().getPlayers().size() > 0)
                    return;
                //turn off VM
            }
        }, 1, TimeUnit.MINUTES);
    }
}
