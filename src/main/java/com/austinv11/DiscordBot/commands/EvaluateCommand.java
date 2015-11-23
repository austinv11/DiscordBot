package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import com.austinv11.DiscordBot.handler.Logger;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.Optional;
import java.util.TreeSet;

public class EvaluateCommand implements ICommand {
	
	@Override
	public String getCommand() {
		return "evaluate";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"eval"};
	}
	
	@Override
	public boolean removesCommandMessage() {
		return false;
	}
	
	@Override
	public String getUsage() {
		return getCommand()+" [optional:language [snippet]]";
	}
	
	@Override
	public String getHelpMessage() {
		return "Evaluate runs a script and will retrieve its output, running it without parameters will list the available language options available";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.ADMINISTRATOR;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		String result = "";
		if (parameters == null || parameters.isEmpty()) {
			TreeSet<String> languages = new TreeSet<>();
			for (ScriptEngineFactory factory : DiscordBot.scriptEngineManager.getEngineFactories()) {
				String languageMessage = "*"+factory.getLanguageName()+" (Can be called using: ";
				for (String extension : factory.getExtensions())
					languageMessage += extension+", ";
				for (String name : factory.getNames())
					languageMessage += name+", ";
				languageMessage = languageMessage.substring(0, languageMessage.length()-2)+")";
				languages.add(languageMessage);
			}
			result += languages.size()+" languages loaded:\n";
			for (String lang : languages)
				result += lang+"\n";
		} else {
			String language = parameters.split(" ")[0];
			String snippet = parameters.replaceFirst(language+" ", "");
			String response = "null";
			long currentTime = System.currentTimeMillis();
			ScriptEngine engine = DiscordBot.getScriptEngineForLang(language);
			language = engine == null ? "null" : engine.getFactory().getLanguageName();
			if (engine != null) {
				ScriptContext context = engine.getContext();
				StringWriter writer = new StringWriter();
				context.setWriter(writer);
				context.setErrorWriter(writer);
				engine.setContext(context);
				try {
					response = "Evaluation: "+engine.eval(snippet, context)+"\nLog output: "+writer.toString()+"\n";
				} catch (ScriptException e) {
					response = "ERROR: "+e.getMessage();
				}
			}
			long executionTime = System.currentTimeMillis()-currentTime;
			Logger.log(response);
			result = "Result: "+response+"\nLanguage: "+language+"\nExecution took "+executionTime+" ms";
		}
		return Optional.ofNullable(result.isEmpty() ? null : "```"+result+"```");
	}
}
