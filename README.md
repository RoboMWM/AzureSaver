# AzureSaver

This is a simple Waterfall/Bungeecord plugin that automatically turns on an Azure VM when players connect to the Waterfall/Bungeecord proxy, and turns it off 10 minutes after the last person has disconnected.

It'll attempt to connect the player to the server named "lobby" in your bungeecord config when the player connects.

Since the VM doesn't consume credits when it's stopped and deallocated, this provides a way to save credits when your server isn't serving anybody, hence the "saver" name part of the plugin. I'm not very good with names.

Support is limited (unless you're a [patron](https://r.robomwm.com/patreon)) and it's not very configurable, but if there's a demand I'll see what I can do.

## Config:

### config.yml

```yml
resourceGroup: # Resource name your VM exists in
vmName: # Name of the VM
```

### creds.properties

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

To fill out creds.properties, basically follow this documentation: https://docs.microsoft.com/en-us/azure/developer/java/sdk/get-started#set-up-authentication

It's been a while since I've done this, but if you wish to create a service principle outside of Azure CLI:
1. [Create a service principle application](https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-create-service-principal-portal#register-an-application-with-azure-ad-and-create-a-service-principal).
1. Create a client secret for the service principle. The value that is returned to you will be the "`authentication-key`."
1. In the VM blade in Access Control (IAM), add a role assignment for the service principle that gives it permissions to turn on and off the VM. There are a few roles that can do this, such as the "Contributor" role as well as the new experimental "Desktop Virtualization Power On Off" role.
    1. You will need to type in the name of the service principle in the "Select" field when clicking "Select members." Then it will show up to be selected.
1. Then you somehow find those credentials and put it in the file. I think I did some expermentation here, and not sure if I have it documented anywhere but the docs should get you in the right direction. (If via the CLI, it appears it'll spit out those values as the output of the command).
