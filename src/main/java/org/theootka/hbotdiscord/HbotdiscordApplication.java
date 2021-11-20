package org.theootka.hbotdiscord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class HbotdiscordApplication {

	@Autowired
	private Environment env;
	public static void main(String[] args) {
		SpringApplication.run(HbotdiscordApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(value="discord-api")
	public DiscordApi discordApi(){
		String token = env.getProperty("TOKEN");
		DiscordApi api = new DiscordApiBuilder().setToken(token)
				.setAllNonPrivilegedIntents()
				.login()
				.join();
		api.addMessageCreateListener(event->{
			if(event.getMessageContent().equals("hb/ping")){
				event.getChannel().sendMessage("pong");
			}
		});
		api.addMessageCreateListener(event->{
			if(event.getMessageContent().equals("hb/amdtop")){
				event.getChannel().sendMessage("ИДИ НАХУЙ АМДЩНИК ЕБАНЫЙ");
			}
		});
		api.getTextChannelById("790861544467464232").get().sendMessage("test");
		return api;
	}
}
