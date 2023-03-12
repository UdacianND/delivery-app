package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.bot.OrderSendToBotDto;

public interface TelegrambotService {
    void sendBotWebApp( Long chatId);
    void sendOrderToGroup(OrderSendToBotDto order);
}
