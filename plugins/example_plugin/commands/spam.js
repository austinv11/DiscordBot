//Handles the !spam command, this spams the chat a set amount of times with the given message
var parameters = CONTEXT.parameters;
var channel = CONTEXT.channel;
var spamTimes = ISM.getVariable(CONTEXT.plugin.plugin_id+"_spamTimes");
Log.info("Spamming '"+parameters+"' "+spamTimes+" times");

for (var i = 0; i < spamTimes; i++) {
	channel.sendMessage(parameters);
}
