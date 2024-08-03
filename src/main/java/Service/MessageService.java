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
     * @param Message an Message object.
     * @return The persisted Message if the persistence is successful.
     */
    public Message addMessage(Message Message) {
        return messageDAO.insertMessage(Message);
    }

    /**
     * Use the MessageDAO to get a message given its message_id.
     *
     * @param Message an Message object.
     * @return The identified Message if it is found in the database.
     */
    public Message getMessage(int messageId) {
        return messageDAO.findMessage(messageId);
    }
}
