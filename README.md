# AzureSaver
This is a simple Waterfall/Bungeecord plugin that automatically turns on an Azure VM when players connect to the Waterfall/Bungeecord proxy, and turns it off 10 minutes after the last person has disconnected.

Since the VM doesn't consume credits when it's stopped and deallocated, this provides a way to save credits when your server isn't serving anybody, hence the "saver" name part of the plugin. I'm not very good with names.

Since I'm not using it anymore, support is very limited (unless you're a [patron](https://r.robomwm.com/patreon)) and it's not very configurable, but if there's a demand I'll see what I can do.

Config:

```yml
resourceGroup: # Resource name your VM exists in
vmName: # Name of the VM
```

To fill out creds.properties, basically follow this documentation: https://docs.microsoft.com/en-us/azure/virtual-machines/windows/java#create-credentials

It's been a while since I followed these steps, but to summarize:
1. [Create a service principle application](https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-create-service-principal-portal#create-an-azure-active-directory-application)
1. Then you somehow find those credentials and put it in the file. I think I did some expermentation here, and not sure if I have it documented anywhere but the docs should get you in the right direction.

creds.properties:

```
subscription=<subscription-id> your subscription identifier
client=<application-id> Active Directory application identifier
key=<authentication-key> application key
tenant=<tenant-id> tenant identifier
managementURI=https://management.core.windows.net/
baseURL=https://management.azure.com/
authURL=https://login.windows.net/
graphURL=https://graph.windows.net/
```

It'll attempt to teleport the player to the server named "lobby" in your bungeecord config when the player connects.
