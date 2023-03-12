package uz.md.shopapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import uz.md.shopapp.dtos.bot.OrderSendToBotDTO;
import uz.md.shopapp.dtos.institution.LocationDto;
import uz.md.shopapp.service.contract.TelegramBotService;

import java.util.List;

import static uz.md.shopapp.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImp implements TelegramBotService {

    private final RestTemplate restTemplate;

    @Override
    public void sendBotWebApp(Long chatId) {
        SendMessage webAppLink = getWebAppLink(chatId);
        restTemplate.postForObject(TELEGRAM_SEND_MESSAGE_URL, webAppLink, Object.class);
    }

    @Override
    public void sendOrderToGroup(OrderSendToBotDTO order) {

        SendMessage orderInfo = getOrderInfo(order);
        LocationDto locationDto = order.getLocation();
        Location location = new Location();
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());

        restTemplate.postForObject(TELEGRAM_SEND_MESSAGE_URL, orderInfo, Object.class);
        restTemplate.postForObject(TELEGRAM_SEND_LOCATION_URL, location, Object.class);
    }

    private SendMessage getWebAppLink(Long chatId){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("link");
        WebAppInfo webAppInfo = new WebAppInfo(TELEGRAM_BOT_WEBAPP_URL);
        button.setWebApp(webAppInfo);
        inlineKeyboardMarkup.setKeyboard(List.of(List.of(button)));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setChatId(chatId);
        sendMessage.setText(BOT_STARTER_MESSAGE);
        return sendMessage;
    }

    private SendMessage getOrderInfo(OrderSendToBotDTO order){
        StringBuilder orderMessage = new StringBuilder();
        orderMessage.append("=========BUYURTMA=========\n\n");
        orderMessage.append("MAHSULOTLAR : \n\n");
        order.getOrderProducts().forEach(product -> {
            orderMessage.append(product.getName())
                    .append("---").append(product.getCount()).append("X")
                    .append("---").append(product.getPrice()).append(" so'm")
                    .append("---").append(product.getInstitutionName())
                    .append("\n.........................................\n");
        });
        orderMessage.append("\n\n HAMMASI : ").append(order.getTotalPrice()).append(" So'm")
                .append("\n Mijoz raqami : ").append(order.getClientPhoneNumber())
                .append("\n Yetkazib berish vaqti : ").append(order.getDeliveryTime());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(GROUP_CHAT_ID);
        sendMessage.setText(orderMessage.toString());
        return sendMessage;
    }
}
