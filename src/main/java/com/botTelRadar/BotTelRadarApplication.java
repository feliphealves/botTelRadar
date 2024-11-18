package com.botTelRadar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.botTelRadar.bot.TelegramBot;

@SpringBootApplication
public class BotTelRadarApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotTelRadarApplication.class, args);
	}
	
	
	@Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot bot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
        return telegramBotsApi;
    }

}
