package de.buddelbubi.utils;

import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.LevelException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class LevelNBT {

	public static CompoundTag getLevelData(File file) {
		
		try {
			
			 CompoundTag levelData = NBTIO.readCompressed(new FileInputStream(file), ByteOrder.BIG_ENDIAN);
	        if (levelData.get("Data") instanceof CompoundTag) {
	            return levelData.getCompound("Data");
	        } else {
	            throw new LevelException("Invalid level.dat");
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static boolean saveNBT(CompoundTag levelData, File file) {
		
			try {
	           NBTIO.writeGZIPCompressed(new CompoundTag().putCompound("Data", levelData), new FileOutputStream(file));
	           return true;
	       } catch (IOException e) {
	           throw new RuntimeException(e);
	       }
		
	}
}
