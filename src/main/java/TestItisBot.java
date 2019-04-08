import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.*;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TestItisBot extends TelegramLongPollingBot {
    private Map<String, BiConsumer<SendMessage, Update>> replies = Map.of(
            "/start", this::start,
            "/myname", this::sayMyName,
            "/mylastname", this::sayLastName,
            "Inline buttons please!", this::sendInlineButtons,
            "Help!", this::sendHelp
    );

    private void sendHelp(SendMessage message, Update update) {
        message.setText("Your help!");

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(update.getMessage().getChatId());
        sendPhoto.setNewPhoto(new File("src/pic.jpeg"));
        try {
            sendPhoto(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void start(SendMessage message, Update update) {
        message.setText("Our commands: \n/myname - returns your name;\n/mylastname - returns your lastname");
        setButtons(message);
    }

    private void sayMyName(SendMessage message, Update update) {
        message.setText(
                update.getMessage().getFrom().getFirstName()
        );
    }

    private void sayLastName(SendMessage message, Update update) {
        String lastname = update.getMessage().getFrom().getLastName();
        if (lastname != null) {
            message.setText(lastname);
        } else {
            message.setText("You have no lastname!");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        if (update.hasMessage()) {
            message.setChatId(update.getMessage().getChatId());

            String command = update.getMessage().getText();
            final BiConsumer<SendMessage, Update> replier = replies.get(command);
            if (replier != null) {
                replier.accept(message, update);
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (update.hasCallbackQuery()) {
            try {
                execute(new SendMessage().setText(
                        update.getCallbackQuery().getData())
                        .setChatId(update.getCallbackQuery().getMessage().getChatId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void setButtons(SendMessage sendMessage) {
        // Create keyboard:
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);

        // 1st row:
        KeyboardRow keyboardFirstRow = new KeyboardRow() {{
            add(new KeyboardButton("Inline buttons please!"));
        }};

        // 2nd row:
        KeyboardRow keyboardSecondRow = new KeyboardRow() {{
            add(new KeyboardButton("Help!"));
        }};

        // Keyboard rows:
        List<KeyboardRow> keyboard = List.of(keyboardFirstRow, keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void sendInlineButtons(SendMessage message, Update update) {
        // Create inline keyboard:
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        //Create rows:
        List<InlineKeyboardButton> row1 = List.of(
                new InlineKeyboardButton().setText("Hello!").setCallbackData("You pressed Hello!"),
                new InlineKeyboardButton().setText("Hi!").setCallbackData("You pressed Hi!")
        );
        List<InlineKeyboardButton> row2 = List.of(
                new InlineKeyboardButton().setText("Bye!").setCallbackData("You pressed Bye!")
        );

        inlineKeyboardMarkup.setKeyboard(List.of(row1, row2));
        message.setText("Choose:").setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public String getBotUsername() {
        return "test_bot";
    }

    @Override
    public String getBotToken() {
        return "857360280:AAEfWB6uYYIRtd8TRYuDuoJkWqnSB7uz8cU";
    }
}
