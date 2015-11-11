package com.austinv11.DiscordBot.api.plugins.api.io;

import java.io.*;

/**
 * The object representing the contents of a file
 */
public class FileHandle {
	
	/**
	 * The mode the file handle is in
	 */
	public int ioMode;
	private File file;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean isOpen = true;
	
	public FileHandle(File file, int ioMode) throws IOException {
		this.file = file;
		this.ioMode = ioMode;
		if (ioMode == new IOMode().READ) {
			reader = new BufferedReader(new FileReader(file));
		} else {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(file, ioMode == new IOMode().APPEND)));
		}
	}
	
	/**
	 * Reads all the contents of the file. NOTE: This closes the handle!
	 * @return The file contents
	 * @throws IOException
	 */
	public String readAll() throws IOException {
		if (ioMode != new IOMode().READ) {
			throw new IOException("The file is in write-only mode!");
		} else {
			String all = null;
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				if (all == null)
					all = currentLine;
				else
					all += "\n"+currentLine;
			}
			close();
			return all;
		}
	}
	
	/**
	 * Reads a line from the handle
	 * @return The line
	 * @throws IOException
	 */
	public String readLine() throws IOException {
		if (ioMode != new IOMode().READ) {
			throw new IOException("The file is in write-only mode!");
		} else {
			return reader.readLine();
		}
	}
	
	/**
	 * Writes to the handle
	 * @param contents The content to write to the file
	 * @throws IOException
	 */
	public void write(String contents) throws IOException {
		if (ioMode == new IOMode().READ) {
			throw new IOException("The file is in read-only mode!");
		} else {
			writer.write(contents);
		}
	}
	
	/**
	 * Writes a line to the handle
	 * @param contents The content to write to the file
	 * @throws IOException
	 */
	public void writeLine(String contents) throws IOException {
		if (ioMode == new IOMode().READ) {
			throw new IOException("The file is in read-only mode!");
		} else {
			writer.println(contents);
		}
	}
	
	/**
	 * Flushes the file handle. NOTE: This can only be used when in a WRITE-ONLY mode
	 * @throws IOException
	 */
	public void flush() throws IOException {
		if (ioMode == new IOMode().READ) {
			throw new IOException("The file is in read-only mode!");
		} else {
			writer.flush();
		}
	}
	
	/**
	 * Closes the file handle
	 * @throws IOException
	 */
	public void close() throws IOException {
		isOpen = false;
		if (ioMode == new IOMode().READ) {
			reader.close();
		} else {
			writer.close();
		}
	}
	
	/**
	 * Checks whether the handle is open
	 * @return True if open, false if otherwise
	 */
	public boolean isOpen() {
		return isOpen;
	}
}
