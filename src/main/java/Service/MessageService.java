package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    public static Message createMessage(Message message) {
        return MessageDAO.insertMessage(message);
    }

    public static List<Message> getAllMessages() {
        return MessageDAO.getAllMessages();
    }

    public static Message getMessageById(int id) {
        return MessageDAO.getMessageById(id);
    }

    public static Message updateMessage(Message message) {
        return MessageDAO.updateMessage(message);
    }

    public static Message deleteMessageById(int id) {
        return MessageDAO.deleteMessageById(id);
    }

    public static List<Message> getMessagesByAccountId(int accountId) {
        return MessageDAO.getMessagesByAccountId(accountId);
    }
}
