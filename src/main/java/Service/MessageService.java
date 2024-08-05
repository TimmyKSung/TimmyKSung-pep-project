package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    /**
     * no-args constructor for creating a new MessageService with a new MessageDAO.
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /**
     * Constructor for a MessageService when a MessageDAO is provided.
     * @param MessageDAO
     */
    public MessageService(MessageDAO MessageDAO){
        this.messageDAO = MessageDAO;
    }
    /**
     * Use the MessageDAO to retrieve all messages.
     *
     * @return all Messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    /**
     * Use the MessageDAO to persist a message. The given message will not have an id provided.
     *
     * @param Message a Message object.
     * @return The persisted Message if the persistence is successful.
     */
    public Message addMessage(Message Message) {
        return messageDAO.insertMessage(Message);
    }

    /**
     * Use the MessageDAO to get a message given its message_id.
     *
     * @param messageId the identification number of a message in the database
     * @return The identified Message if it is found in the database.
     */
    public Message getMessage(int messageId) {
        return messageDAO.findMessage(messageId);
    }

    /**
     * Use the MessageDAO to delete a message given its message_id.
     *
     * @param messageId the identification number of a message in the database
     * @return The identified Message if it is found in the database.
     */
    public Message deleteMessage(int messageId) {
        return messageDAO.removeMessage(messageId);
    }

    /**
     * Use the MessageDAO to patch a message given its message_id.
     *
     * @param updatedMessage a Message object recieving an update.
     * @param newMessage a Message object containing the updating message_text.
     * @return The patched Message in the database.
     */
    public Message patchMessage(Message updatedMessage, Message newMessage) {
        return messageDAO.updateMessage(updatedMessage, newMessage);
    }

    /**
     * Use the MessageDAO to get all the messages of a particuler user given their account_id.
     *
     * @param accountId the identification number of an account in the database
     * @return A list of Messages associated with the identified Account if it is found in the database.
     */
    public List<Message> getUserMessages(int accountId) { 
        return messageDAO.findUserMessages(accountId);
    }
}
