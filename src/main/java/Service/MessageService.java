package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    public static List<Message> getAllMessages(){
        return MessageDAO.getAllMessages();
    }

    public static Message getMessageById(int id){
        return MessageDAO.getMessageById(id);
    }

    public static List<Message> getMessageFromUserMessages(int accountId){
        return MessageDAO.getMessageFromUserMessage(accountId);
    }

    public static Message createNewMessage(Message message) {
        return MessageDAO.createNewMessage(message);
    }

    public static Message deleteMessageById(int id){
        return MessageDAO.deleteMessageById(id);
    }

    public static Message updateMessage(Message message){
        return MessageDAO.updateMessage(message);
    }
}

