package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.bot.OrderSendToBotDTO;

public interface TelegramBotService {
    void sendBotWebApp( Long chatId);
    void sendOrderToGroup(OrderSendToBotDTO order);
}
