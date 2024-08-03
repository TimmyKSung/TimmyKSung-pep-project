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
     * The creation of the message will be successful if and only if the message_text is not blank, 
     * is not over 255 characters, and posted_by refers to a real, existing user. If successful, 
     * the response body should contain a JSON of the message, including its message_id. The response 
     * status should be 200, which is the default. The new message should be persisted to the database.
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

}