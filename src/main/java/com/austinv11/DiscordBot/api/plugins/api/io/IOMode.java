package com.austinv11.DiscordBot.api.plugins.api.io;

/**
 * A collection of constants for determining the mode a {@link FileHandle} should be opened as
 */
public class IOMode {
	
	/**
	 * Sets the mode to <b>READ-ONLY</b>, only allowing you to read the contents of a file
	 */
	public int READ = -1;
	/**
	 * Sets the mode to <b>APPEND</b>, meaning that when you write to the file, it is appended to the end of the original
	 * file contents rather than overwriting the original contents. This mode is <b>WRITE-ONLY</b>
	 */
	public int APPEND = 0;
	/**
	 * Sets the mode to <b>WRITE</b>, which clears the contents of the file before writing to the file. This mode is
	 * <b>WRITE-ONLY</b>
	 */
	public int WRITE = 1;
}
