package com.robomwm.azuresaver;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.rest.LogLevel;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * Created on 11/28/2019.
 *
 * @author RoboMWM
 */
public class AzureSaver extends Plugin implements Listener
{
    private Azure azure;
    private VirtualMachine vm;
    private ScheduledTask task;

    @Override
    public void onEnable()
    {
        boolean ready = true;
        try
        {
            File credsFile = new File(getDataFolder() + File.separator + "creds.properties");
            credsFile.getParentFile().mkdirs();
            if (!credsFile.exists())
            {
                copyFile(credsFile, "creds.properties");
                ready = false;
            }
            azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credsFile).withDefaultSubscription();
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists())
            {
                copyFile(configFile, "config.yml");
                ready = false;
            }
            if (!ready)
            {
                getLogger().warning("Fill out the configurations");
                return;
            }
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            vm = azure.virtualMachines().getByResourceGroup(configuration.getString("resourceGroup"), configuration.getString("vmName"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        getProxy().getPluginManager().registerListener(this, this);
    }

    private void copyFile(File file, String name)
    {
        try (InputStream in = getResourceAsStream(name)) {
            Files.copy(in, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PostLoginEvent event)
    {
        if (this.getProxy().getPlayers().size() > 1)
            return;
        vm.start();
        ServerInfo server = this.getProxy().getServerInfo("lobby");
        event.getPlayer().connect(server, new Callback<Boolean>()
        {
            @Override
            public void done(Boolean result, Throwable error)
            {
                getLogger().info("k done");
            }
        }, true, 25);
    }

    @EventHandler
    public void onJoinServer(LoginEvent event)
    {
        getLogger().info("javadocs succ");
        if (event.isCancelled())
            event.setCancelReason("ok boomer");
    }

    @EventHandler
    public void onLastLeave(PlayerDisconnectEvent event)
    {
        getLogger().info(String.valueOf(this.getProxy().getPlayers().size()));

        if (this.getProxy().getPlayers().size() > 1) //player is removed from list after this event
            return;

        getLogger().info("scheduling");

        if (task != null)
            getProxy().getScheduler().cancel(task);

        task = getProxy().getScheduler().schedule(this, () ->
        {
            task = null;
            if (getProxy().getPlayers().size() > 0)
                return;
            //turn off VM
            getLogger().info("turning off");
            vm.deallocate();
        }, 10, TimeUnit.MINUTES);
    }
}
