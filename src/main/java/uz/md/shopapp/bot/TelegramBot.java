package uz.md.shopapp.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new TelegramBot());
            log.info("bot successfully register");
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return "delivery_06bot";
    }

    @Override
    public String getBotToken() {
        return "5952282927:AAF1mkgP9IVyZyfXGWn-S-ZCwtoMcbcSwUI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Location location = update.getMessage().getLocation();
            var msg = update.getMessage();
            var chatId = msg.getChatId();
            try {
                sendLocation(String.valueOf(chatId), location);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendLocation(String chatId, Location location) throws TelegramApiException {
        SendLocation sendLocation = new SendLocation(chatId,location.getLatitude(),location.getLongitude());
        execute(sendLocation);
    }


    private void sendNotification(String chatId,Location location, String msg) throws TelegramApiException {
        SendLocation sendLocation = new SendLocation(chatId,location.getLatitude(),location.getLongitude());
        SendMessage sendMessage = new SendMessage(chatId, msg);
        execute(sendLocation);
    }
}