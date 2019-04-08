import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class ZaurEduBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();

        SendMessage message = new SendMessage();

        if (command.equals("/start")) {
            message.setText("Please enter /myname or /mylastname!");
            setButtons(message);
        }

        if (command.equals("/myname")) {
            String name = update.getMessage().getFrom().getFirstName();
            message.setText(name);
        }

        if (command.equals("/mylastname")) {
            String lastname = update.getMessage().getFrom().getLastName();
            if (lastname != null) {
                message.setText(lastname);
            } else {
                message.setText("You have no lastname!");
            }
        }

        if (command.equals("Hello!")) {
            message.setText("Goodbye!");
        }

        if (command.equals("Help!")) {
            message.setText("How can I help you?");
        }
        message.setChatId(update.getMessage().getChatId());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Hello!"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Help!"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public String getBotUsername() {
        return "test_bot";
    }

    public String getBotToken() {
        return "857360280:AAEfWB6uYYIRtd8TRYuDuoJkWqnSB7uz8cU";
    }
}
