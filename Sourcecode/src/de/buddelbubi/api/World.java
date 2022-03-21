package de.buddelbubi.api;

import java.io.File;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;

public class World {

	private String level;
	private Config config;
	private boolean loadonstart;
	private boolean useowngamemode;
	private int gamemode;
	private String respawnworld;
	private String thumbnail;
	private boolean fly;
	private boolean protect;
	private String note;
	
	//Uncached World Object. It is recommended to use Cache.getWorld(Level level);
	public World(Level level) {
		
		this.level = level.getName();
		refreshData();
		
	}
	
	public Level getAsLevel() {
		return Server.getInstance().getLevelByName(this.level);
	}
	
	public Config getWorldConfiguration() {
		return this.config;
	}
	
	public Config getConfig() {
		return this.config;
	}
	
	public boolean doesLoadOnStart() {
		return this.loadonstart;
	}
	
	public boolean isUsingOwnGamemode() {
		return this.useowngamemode;
	}
	
	public int getOwnGamemode() {
		return this.gamemode;
	}
	
	public String getRespawnWorld() {
		return this.respawnworld;
	}
	
	public String getThumbnail() {
		return this.thumbnail;
	}
	
	public boolean isFlyAllowed() {
		return this.fly;
	}
	
	public boolean isProtected() {
		return this.protect;
	}
	
	
	public String getNote() {
		return this.note;
	}
	
	public void setLoadOnStart(boolean loadOnStart) {
		this.loadonstart = loadOnStart;
		this.config.set("LoadOnStart", loadOnStart);
		this.config.save();
	}
	
	public void enableLoadOnStart() {
		this.loadonstart = true;
		this.config.set("LoadOnStart", true);
		this.config.save();
	}
	
	public void disableLoadOnStart() {
		this.loadonstart = false;
		this.config.set("LoadOnStart", false);
		this.config.save();
	}
	
	public void setUsingOwnGamemode(boolean usingOwnGamemode) {
		this.useowngamemode = usingOwnGamemode;
		this.config.set("UseOwnGamemode", usingOwnGamemode);
		this.config.save();
	}
	
	public void enableOwnGamemode() {
		this.useowngamemode = true;
		this.config.set("UseOwnGamemode", true);
		this.config.save();
	}
	
	public void disableOwnGamemode() {
		this.useowngamemode = false;
		this.config.set("UseOwnGamemode", false);
		this.config.save();
	}
	
	public void setGamemode(int gamemode) {
		if(gamemode < 0 || gamemode > 3) throw new IndexOutOfBoundsException("Unknown Gamemode");
		this.gamemode = gamemode;
		this.config.set("Gamemode", gamemode);
		this.config.save();
	}
	
	public void setFly(boolean fly) {
		this.fly = fly;
		this.config.set("fly", fly);
		this.config.save();
	}
	
	public void enableFly() {
		this.fly = true;
		this.config.set("fly", true);
		this.config.save();
	}
	
	public void disableFly() {
		this.fly = false;
		this.config.set("fly", false);
		this.config.save();
	}
	
	public void setProtected(boolean protect) {
		this.protect = protect;
		this.config.set("protected", protect);
		this.config.save();
	}
	
	public void enableProtection() {
		this.protect = true;
		this.config.set("protected", true);
		this.config.save();
	}
	
	public void disableProtection() {
		this.protect = false;
		this.config.set("protected", false);
		this.config.save();
	}
	
	public void setRespawnWorld(String respawnworld) {
		this.respawnworld = respawnworld;
		this.config.set("respawnworld", respawnworld);
		this.config.save();
	}
	
	public void setRespawnWorld(Level respawnworld) {
		this.respawnworld = respawnworld.getName();
		this.config.set("respawnworld", respawnworld.getName());
		this.config.save();
	}
	
	public void setRespawnWorld(World respawnworld) {
		this.respawnworld = respawnworld.getAsLevel().getName();
		this.config.set("respawnworld", respawnworld.getAsLevel().getName());
		this.config.save();
	}
	
	public void setThumbnail(String thumbnail) {
		this.respawnworld = thumbnail;
		this.config.set("thumbnail", thumbnail);
		this.config.save();
	}
	
	public void setPathThumbnail(String path) {
		setThumbnail("path::" + path);
	}
	
	public void setUrlThumbnail(String url) {
		setThumbnail("url::" + url);
	}
	
	public void setNote(String note) {
		this.note = note;
		this.config.set("note", note);
		this.config.save();
	}
	
	public void refreshData() {
		
		// If you are using the api, execute this to refresh the data. It may be changed by UI changes or manual config overwrites.
		
		this.config = new Config(new File(Server.getInstance().getDataPath() + "/worlds/" + this.getAsLevel().getFolderName(), "config.yml"));
		this.loadonstart = this.config.getBoolean("LoadOnStart");
		this.useowngamemode = this.config.getBoolean("UseOwnGamemode");
		this.gamemode = this.config.getInt("Gamemode");
		this.protect = this.config.getBoolean("protected");
		this.fly = this.config.getBoolean("fly");
		this.respawnworld = this.config.getString("respawnworld");
		this.thumbnail = this.config.getString("thumbnail");
		this.note = this.config.getString("note");
		
	}
	
	
	
}
