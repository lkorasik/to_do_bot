/*
    Lev
 */
package com.telegrambot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartBot {
    public static void main(String[] args){
        System.out.println("Bot started!");

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        Bot bot = new Bot();

        try{
            botsApi.registerBot(bot);
        } catch (TelegramApiException exception){
            exception.printStackTrace();
        }
    }
}
