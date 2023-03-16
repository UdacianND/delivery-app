package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.md.shopapp.service.contract.TelegramBotService;
import uz.md.shopapp.utils.AppConstants;

import static uz.md.shopapp.utils.AppConstants.GET_CHAT_ID_CMD;

@RestController
@RequestMapping(TelegrambotController.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Telegrambot", description = "Endpoints for Telegram bot")
@Slf4j
public class TelegrambotController {
    public static final String BASE_URL = AppConstants.BASE_URL + "telegrambot";
    private final TelegramBotService telegrambotService;

    @PostMapping
    public void getUpdates(@RequestBody Update update) {
        Message message = update.getMessage();
        if (message == null || !message.isUserMessage())
            return;
        Long chatId = message.getChatId();
        if(message.getText().equals(GET_CHAT_ID_CMD))
            telegrambotService.sendChatId(chatId);
        else
            telegrambotService.sendBotWebApp(chatId);
    }
}

