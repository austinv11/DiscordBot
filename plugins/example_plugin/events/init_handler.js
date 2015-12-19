//Generates and handles a custom config for the plugin
var CONFIG_FILE = "ExamplePlugin.cfg";
if (!FileIO.exists(CONFIG_FILE)) {
	Log.info(CONFIG_FILE+" does not exist, making it");
	var writer = FileIO.open(CONFIG_FILE, IOMode.WRITE);
	writer.writeLine("# The amount of times to repeat a message with the !spam command");
	writer.writeLine("10");
	writer.writeLine("# The amount of times to repeat the animation with the !animate command");
	writer.writeLine("3");
	writer.writeLine("# The time in milliseconds to wait between frames with the !animate command");
	writer.writeLine("500");
	writer.flush();
	writer.close();
}

var reader = FileIO.open(CONFIG_FILE, IOMode.READ);
reader.readLine();
ISM.putVariable(CONTEXT.plugin.plugin_id + "_spamTimes", +reader.readLine());
reader.readLine();
ISM.putVariable(CONTEXT.plugin.plugin_id + "_animationRepeat", +reader.readLine());
reader.readLine();
ISM.putVariable(CONTEXT.plugin.plugin_id + "_frameTime", +reader.readLine());
reader.close();
Log.debug(CONTEXT.plugin.plugin_id+" initialized");
