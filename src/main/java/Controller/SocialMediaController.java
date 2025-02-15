package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;  

import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should  
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{id}", this::getMessageByIdHandler);
        app.patch("/messages/{id}", this::updateMessageHandler);
        app.delete("/messages/{id}", this::deleteMessageHandler);
        app.get("/accounts/{id}/messages", this::getMessagesForUserHandler);

        return app;
    }

    private void registerHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        if (account.getUsername().isBlank() || account.getPassword().length() < 4)
        {
            context.status(400).result("");
            return;
        }

        if (AccountService.isUsernameTaken(account.getUsername())) 
        {
            context.status(400).result("");
            return;
        }

        Account createdAccount = AccountService.registerAccount(account);
        context.status(200).json(createdAccount);
    }

    private void loginHandler(Context context) {
        Account credentials = context.bodyAsClass(Account.class);
        Account authenticatedAccount = AccountService.authenticate(credentials.getUsername(), credentials.getPassword());

        if (authenticatedAccount != null) {
            context.status(200).json(authenticatedAccount);
        } else {
            context.status(401).result("");
        }
    }

    private void createMessageHandler(Context context) {
        Message message = context.bodyAsClass(Message.class);
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255 ||
            !AccountService.isAccountExists(message.getPosted_by())) {
            context.status(400).result("");
            return;
        }

        Message createdMessage = MessageService.createNewMessage(message);
        context.status(200).json(createdMessage);
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = MessageService.getAllMessages();
        context.status(200).json(messages);
    }

    private void getMessageByIdHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("id"));
        Message message = MessageService.getMessageById(id);

        if (message != null) {
            context.status(200).json(message);
        } else {
            context.status(200).result("");
        }
    }

    private void updateMessageHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("id"));
        Message existingMessage = MessageService.getMessageById(id);

        if (existingMessage == null) {
            context.status(400).result("");
            return;
        }

        Message messageUpdate = context.bodyAsClass(Message.class);
        if (messageUpdate.getMessage_text().isBlank() || messageUpdate.getMessage_text().length() > 255) {
            context.status(400).result("");
            return;
        }

        existingMessage.setMessage_text(messageUpdate.getMessage_text());
        Message updatedMessage = MessageService.updateMessage(existingMessage);
        context.status(200).json(updatedMessage);
    }

    private void deleteMessageHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("id"));
        Message deletedMessage = MessageService.deleteMessageById(id);

        if (deletedMessage != null) {
            context.status(200).json(deletedMessage);
        } else {
            context.status(200).result("");
        }
    }

    private void getMessagesForUserHandler(Context context) {
        int userId = Integer.parseInt(context.pathParam("id"));
        List<Message> messages = MessageService.getMessageFromUserMessages(userId);
        //getMessageById(userId);
        //getMessagesByAccountId(userId);
        context.status(200).json(messages);
    }
}
