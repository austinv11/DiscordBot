/**
 * This package contains everything related to plugins. Here is the basic overview for how scripts work:
 * Bot start init. -> Scripting Engines loaded -> Plugin manifests read -> Bot finish init. -> Plugins initialized <br>
 * <br>
 * Languages supported depends on the scripting engines present, by default all environments have an ECMAScript engine
 * loaded (JavaScript). Additional engines can be added by simply putting their respective binaries in the same folder
 * as the bot.<br>
 * <br>
 * Plugins should be placed in the "plugins" folder within the bot's working directory. Each plugin should be "compiled"
 * into a .zip file. The content hierarchy should be as follows:<br>
 * Plugin.zip<br>
 *       --> /PLUGIN_MANIFEST.json<br>
 *       --> /path/to/scripts/<br>
 *                     ----> /script.extension<br>
 *                     ----> /script2.extension<br>
 * <br>
 * When a script is executed, there will be an object known as "CONTEXT", this represents what the script is supposed<br>
 * to be processing, for example if the script is an event handler the CONTEXT object will be the event object<br>
 * <br>
 * NOTE: Many scripting engines provide ways for scripts to use Java APIs or modify certain Java processes, this is
 * <b>HEAVILY</b> discouraged. This is because: the bot's code can change without warning, your actions may cause
 * unintended side effects and it shouldn't be necessary–if the scripting api is lacking, submit an issue on the
 * Github repository and it should get addressed.<br>
 */
package com.austinv11.DiscordBot.api.plugins;
