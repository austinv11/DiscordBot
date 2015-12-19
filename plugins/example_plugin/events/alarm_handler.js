// Handles the timing for frame changes with animated messages
var id = CONTEXT.event.id;
var data = JSON.parse(ISM.getVariable("TIMER_"+id));
var channel = Bot.getChannelByID(data.channel);
var message = channel.getMessageByID(data.message);
var nextFrame = data.currentFrame+1;
var frameTime = ISM.getVariable(CONTEXT.plugin.plugin_id+"_frameTime");
var repeats = data.timesToRepeat;
if (nextFrame >= data.frames.length) {
	nextFrame = 0;
	repeats = data.timesToRepeat-1;
}
var newData = {
"currentFrame": nextFrame,
"timesToRepeat": repeats,
"frames": data.frames,
"channel": data.channel,
"message": data.message
};
if (repeats > -1) {
	message.edit(newData.frames[nextFrame]);
	var newID = Timer.startAlarm(frameTime);
	ISM.putVariable("TIMER_"+newID, JSON.stringify(newData));
}
