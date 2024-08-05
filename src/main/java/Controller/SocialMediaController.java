package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getUserMessagesHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler to post/register a new account.
     * If all the conditions are met, the response body should contain a JSON of the Account, 
     * including its account_id. The response status should be 200 OK, which is the default. 
     * The new account should be persisted to the database.
     * If the registration is not successful, the response status should be 400. (Client error)
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to verify login.
     * The login will be successful if and only if the username and password provided in the 
     * request body JSON match a real account existing on the database. If successful, the 
     * response body should contain a JSON of the account in the response body, including its 
     * account_id. The response status should be 200 OK, which is the default.
     * If the login is not successful, the response status should be 401. (Unauthorized)
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account findAccount = accountService.SearchAccount(account);
        if(findAccount != null){
            ctx.json(mapper.writeValueAsString(findAccount));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }

    /**
     * Handler to create a new message.
     * If successful, the response body should contain a JSON of the message, including its message_id. The 
     * response status should be 200, which is the default. The new message should be persisted to the database.
     * If the creation of the message is not successful, the response status should be 400. (Client error)
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postNewMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage != null){
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to get all messages.
     * The response body should contain a JSON representation of a list containing all messages retrieved 
     * from the database. It is expected for the list to simply be empty if there are no messages. The 
     * response status should always be 200, which is the default.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getAllMessageHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * Handler to get a message given its message_id.
     * The response body should contain a JSON representation of the message identified by the message_id. It 
     * is expected for the response body to simply be empty if there is no such message. The response status 
     * should always be 200, which is the default.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message gotMessage = messageService.getMessage(messageId);
        if (gotMessage != null) {
            ctx.json(gotMessage);
        }
        ctx.status(200);
    }

    /**
     * Handler to delete a message given its message_id.
     * The deletion of an existing message should remove an existing message from the database. If 
     * the message existed, the response body should contain the now-deleted message. The response 
     * status should be 200, which is the default.
     * If the message did not exist, the response status should be 200, but the response body should 
     * be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to 
     * the DELETE endpoint should respond with the same type of response.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        }
        ctx.status(200);
    }

    /**
     * Handler to update a message given its message_id.
     * The request body should contain a new message_text values to replace the message identified by message_id.
     * If the update is successful, the response body should contain the full updated message (including message_id, 
     * posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. 
     * The message existing on the database should have the updated message_text.
     * 
     * If the update of the message is not successful for any reason, the response status should be 400. (Client error)
     */
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.patchMessage(messageService.getMessage(messageId), message);
        if(updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to get all messages posted by a particular user
     * The response body should contain a JSON representation of a list containing all messages posted by a particular user, 
     * which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The 
     * response status should always be 200, which is the default
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getUserMessagesHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getUserMessages(accountId);
        ctx.json(messages);
        ctx.status(200);
    }

}