package org.theootka.hbotdiscord;

import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.message.Message;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class HbotdiscordApplication {

	@Autowired
	private Environment env;
	public static void main(String[] args) {
		SpringApplication.run(HbotdiscordApplication.class, args);
	}
	@Bean
	@ConfigurationProperties(value="discord-api")
	public DiscordApi discordApi() throws IOException {
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

		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					new MessageBuilder()
							.append(getNsfw().toString())
							.send(api.getTextChannelById("790861544467464232").get());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		ScheduledExecutorService sch = Executors.newSingleThreadScheduledExecutor();
		sch.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);

		return api;
	}
	public String getNsfw() throws IOException {
		URL url = new URL("https://lewds.fun/api/v1/nsfw/hentai");
		String readLine = null;
		String str = null;
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setRequestProperty("Authorization", "b4ehgfaa0h708935892424982530egijjdjb0j");
		int responseCode = httpURLConnection.getResponseCode();
		if(responseCode == HttpURLConnection.HTTP_OK){
			BufferedReader in = new BufferedReader(
					new InputStreamReader(httpURLConnection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in .readLine()) != null){
				response.append(readLine);
			} in .close();
			JSONObject obj = new JSONObject(response.toString());
			str = obj.getString("result");
		}else {
			System.out.println("GET NOT WORKED");
		}
		return str;
	}

}
