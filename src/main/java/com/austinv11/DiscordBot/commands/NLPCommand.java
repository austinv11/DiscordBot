package com.austinv11.DiscordBot.commands;

import com.austinv11.DiscordBot.DiscordBot;
import com.austinv11.DiscordBot.api.commands.CommandSyntaxException;
import com.austinv11.DiscordBot.api.commands.ICommand;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import sx.blah.discord.handle.obj.Channel;
import sx.blah.discord.handle.obj.Message;
import sx.blah.discord.handle.obj.User;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class NLPCommand implements ICommand {
	
	private static Properties properties = new Properties();
	private static StanfordCoreNLP pipeline;
	
	static {
		properties.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment, ner, dcoref");
		pipeline = new StanfordCoreNLP(properties);
	}
	
	@Override
	public String getCommand() {
		return "nlp";
	}
	
	@Override
	public String[] getAliases() {
		return new String[0];
	}
	
	@Override
	public boolean removesCommandMessage() {
		return false;
	}
	
	@Override
	public String getUsage() {
		return getCommand()+" <message>";
	}
	
	@Override
	public String getHelpMessage() {
		return "Performs a natural language processing analysis on the provided text";
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return ICommand.DEFAULT;
	}
	
	@Override
	public Optional<String> executeCommand(String parameters, User executor, Channel channel, Message commandMessage) throws CommandSyntaxException {
		String text = commandMessage.getContent().replace(DiscordBot.CONFIG.commandDiscriminator+getCommand()+" ", "");
		String message = "```";
		Annotation annotation = pipeline.process(text);
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
			message += "Sentence '"+sentence+"' has the sentiment: "+sentiment+"\n";
			
			for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String word = token.get(CoreAnnotations.TextAnnotation.class);
				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
				String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
				message += "Word: " + word + ", part of speech: " + pos + ", named entity tag:" + ne + "\n";
			}
			
			Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
			message += "Parse tree:\n" + tree + "\n";
			SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
			message += "Dependency graph:\n" + dependencies + "\n";
		}
		
		message += "```";
		return Optional.of(message);
	}
}
