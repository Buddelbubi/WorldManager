package de.buddelbubi.listener;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import cn.nukkit.utils.Config;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import de.buddelbubi.WorldManager;
import de.buddelbubi.utils.Updater;

public class Addons implements Listener {

    public final static String url = "https://buddelbubi.xyz/cdn/worldmanager/nukkit/indexes.json";

    public static JsonObject json;

    @SuppressWarnings("deprecation")
    public static void initJson() {
        new Thread(() -> {
            try {
                json = new JsonParser().parse(getText(url)).getAsJsonObject();

                //Disabling the Auto-Updater is not recommended unless your host disables file downloads or your host is blocked from cloudburstmc.org.

                File file = new File(Server.getInstance().getPluginPath(), "worldmanager.yml");
                if(file.exists()) {
                    if(!new Config(file).getBoolean("autoupdate")) return;
                }

                Updater.checkAndDoUpdateIfAvailable();

            } catch (Exception e) {
                WorldManager.get().getLogger().error("Could not load the addon page.");
            }
        }).start();
    }

    public static void showAddonUI(Player p) {

        if (json == null) {
            p.sendMessage(WorldManager.prefix + "§cAddons are not available right now. It may be caused by gson.");
            return;
        }

        FormWindowSimple fw = new FormWindowSimple("§3WorldManager §cAddon Marketplace", "§7Here you can download Addons and extentions for WorldManager and other World-Related plugins.");
        JsonObject categories = json.get("categories").getAsJsonObject();
        for (String s : categories.keySet()) {
            JsonObject section = categories.get(s).getAsJsonObject();
            JsonObject settings = section.get("settings").getAsJsonObject();
            fw.addButton(new ElementButton(settings.get("name").getAsString(), new ElementButtonImageData("path", settings.get("thumbnail").getAsString())));

        }
        p.showFormWindow(fw, "addonsections".hashCode());
    }

    @EventHandler
    public void on(PlayerFormRespondedEvent e) {

        if (e.getWindow() instanceof FormWindowSimple && e.getResponse() != null) {

            FormWindowSimple fws = (FormWindowSimple) e.getWindow();
            FormWindowSimple fw = new FormWindowSimple("", "");
            if (e.getFormID() == "addonsections".hashCode()) {
                JsonObject categories = json.get("categories").getAsJsonObject();
                JsonObject section = categories.get(fws.getResponse().getClickedButton().getText().toLowerCase().replace(" ", "_")).getAsJsonObject();
                JsonObject settings = section.get("settings").getAsJsonObject();
                fw.setTitle("§3" + settings.get("name").getAsString());
                for (String plugin: section.keySet())
                    if (!plugin.equals("settings")) fw.addButton(new ElementButton(plugin, new ElementButtonImageData("path", section.get(plugin).getAsString())));
                e.getPlayer().showFormWindow(fw, "addonsection".hashCode());
            } else if (e.getFormID() == "addonsection".hashCode()) {
                JsonObject plugins = json.get("plugins").getAsJsonObject();
                JsonObject plugin = plugins.get(fws.getResponse().getClickedButton().getText()).getAsJsonObject();
                fw.addButton(new ElementButton("Install", new ElementButtonImageData("path", "textures/ui/free_download.png")));
                fw.setTitle("§3" + fws.getResponse().getClickedButton().getText() + " by " + plugin.get("author").getAsString());
                fw.setContent(plugin.get("description").getAsString().replace("&", "§"));
                e.getPlayer().showFormWindow(fw, "installaddon".hashCode());
            } else if (e.getFormID() == "installaddon".hashCode()) installAddon(fws.getTitle().replace("§3", "").split(" ")[0], e.getPlayer());
        }
    }

    private static String getText(String url) {
        try {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            return response.toString();
        } catch (Exception e) {
            Server.getInstance().getLogger().warning(WorldManager.prefix + "§cCould't fetch addon page. (" + e.getMessage() + ")");
            return null;
        }
    }

    public static void installAddon(String name, CommandSender arg0) {

        arg0.sendMessage(WorldManager.prefix + "§aStarting the download...");
        try {
            JsonObject section = json.get("plugins").getAsJsonObject();
            JsonObject plugin = section.get(name).getAsJsonObject();
            URL url = new URL(plugin.get("link").getAsString());
            File file = new File(Server.getInstance().getPluginPath(), name + ".jar");
            InputStream in = url.openStream();
            Files.copy( in , file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            arg0.sendMessage(WorldManager.prefix + "§aDownload successful.");

            Server.getInstance().enablePlugin(Server.getInstance().getPluginManager().loadPlugin(file));

        } catch (IOException e) {
            arg0.sendMessage(WorldManager.prefix + "§cDownload failed...  (" + e.getMessage() + ")");
        }

    }
}