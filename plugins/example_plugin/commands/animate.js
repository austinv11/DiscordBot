// Handles the !animate command
var parameters = CONTEXT.parameters;
var channel = CONTEXT.channel;
var repeats = ISM.getVariable(CONTEXT.plugin.plugin_id+"_animationRepeat");
var frameTime = ISM.getVariable(CONTEXT.plugin.plugin_id+"_frameTime");
var frames = parameters.split(";");

var message = channel.sendMessage(frames[0]);
var data = {
"currentFrame": 0,
"timesToRepeat": repeats-1,
"frames": frames,
"channel": channel.getID(),
"message": message.getID()
};
var timerId = Timer.startAlarm(frameTime);
ISM.putVariable("TIMER_"+timerId, JSON.stringify(data));
