package com.austinv11.DiscordBot.api.plugins.api.io;

import java.io.File;
import java.io.IOException;

/**
 * An standardized file io api for scripting–this keeps file io within a standardized data folder automatically
 */
public class FileIO {
	
	public static final String PLUGIN_DATA_DIR = "./plugin_data/";
	
	/**
	 * Opens a {@link FileHandle} for the specified file in the given mode
	 * @param filePath The path leading to the file
	 * @param ioMode The {@link IOMode} the {@link FileHandle} will be opened with
	 * @return The {@link FileHandle}
	 * @throws IOException
	 */
	public FileHandle open(String filePath, int ioMode) throws IOException {
		IOMode temp = new IOMode();
		if (ioMode != temp.READ && ioMode != temp.APPEND && ioMode != temp.WRITE)
			throw new IOException("IO Mode "+ioMode+" is unknown!");
		return new FileHandle(new File(PLUGIN_DATA_DIR+filePath), ioMode);
	}
	
	/**
	 * Lists the files in the base plugin directory
	 * @return The files
	 */
	public String[] listFiles() {
		return new File(PLUGIN_DATA_DIR).list();
	}
	
	/**
	 * Lists the files in the specified directory
	 * @param path The directory
	 * @return The files
	 * @throws IOException
	 */
	public String[] listFiles(String path) throws IOException {
		if (!exists(path))
			throw new IOException("File in the path "+path+" does not exist!");
		if (!isDir(path))
			throw new IOException("File in the path "+path+" is not a directory!");
		return new File(PLUGIN_DATA_DIR+path).list();
	}
	
	/**
	 * Deletes the specified file
	 * @param path The file
	 * @return Whether the action was successful
	 * @throws IOException
	 */
	public boolean delete(String path) throws IOException {
		if (!exists(path))
			throw new IOException("File in the path "+path+" does not exist!");
		return new File(PLUGIN_DATA_DIR+path).delete();
	}
	
	/**
	 * Makes a directory in the specified path
	 * @param path The path
	 * @return Whether the action was successful
	 * @throws IOException
	 */
	public boolean mkDir(String path) throws IOException {
		if (exists(path))
			throw new IOException("File in the path "+path+" already exists!");
		return new File(PLUGIN_DATA_DIR+path).mkdir();
	}
	
	/**
	 * Checks if the specified file is a directory
	 * @param path The file path
	 * @return True if the file is a directory, false if otherwise
	 * @throws IOException
	 */
	public boolean isDir(String path) throws IOException {
		if (!exists(path))
			throw new IOException("File in the path "+path+" does not exist!");
		return new File(PLUGIN_DATA_DIR+path).isDirectory();
	}
	
	/**
	 * Checks if the specified file exists
	 * @param path The file path
	 * @return True if the file exists, false if otherwise
	 */
	public boolean exists(String path) {
		return new File(PLUGIN_DATA_DIR+path).exists();
	}
	
	/**
	 * Moves a file to the specified path
	 * @param oldFilePath The file to move
	 * @param newFilePath The file path to move the original file to
	 * @return Whether the action is successful
	 * @throws IOException
	 */
	public boolean move(String oldFilePath, String newFilePath) throws IOException {
		if (!exists(oldFilePath))
			throw new IOException("File in the path "+oldFilePath+" does not exist!");
		if (exists(newFilePath))
			throw new IOException("File in the path "+newFilePath+" already exists!");
		return new File(PLUGIN_DATA_DIR+oldFilePath).renameTo(new File(PLUGIN_DATA_DIR+newFilePath));
	}
}
