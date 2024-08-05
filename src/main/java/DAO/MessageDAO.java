package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    /**
     * Retrieve all messages from the message table.
     * @return all messages.
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Insert an message into the message table.
     * The creation of the message will be successful if and only if the message_text is not blank, 
     * is not over 255 characters, and posted_by refers to a real, existing user. 
     * @param message a Message object
     * @return the Message added into the database.
     */
    public Message insertMessage(Message message){
        if (message == null) {
            return null;
        }
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()|| message.getMessage_text().length() > 255) {
            return null;
        }

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?;";
            PreparedStatement checkStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            checkStatement.setInt(1, message.getPosted_by());

            ResultSet rs = checkStatement.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return null;
            }

            sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_Message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_Message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve a message from the message table based on its message_id.
     * @return the identified message.
     */
    public Message findMessage(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        Message returnMessage = null;
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ? LIMIT 1;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                returnMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return returnMessage;
    }

    /**
     * Delete a message from the message table based on its message_id.
     * @return the deleted message.
     */
    public Message removeMessage(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        Message returnMessage = null;
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ? LIMIT 1;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                returnMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }

            if (returnMessage != null) {
                sql = "DELETE FROM Message WHERE message_id = ?;";
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1, messageId);

                preparedStatement.executeUpdate();
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return returnMessage;
    }

    /**
     * Update a message from the message table based on its message_id.
     * The update of a message should be successful if and only if the message id already exists and the new 
     * message_text is not blank and is not over 255 characters. 
     * @return the updated message.
     */
    public Message updateMessage(Message updatedMessage, Message newMessage){
        Connection connection = ConnectionUtil.getConnection();
        Message returnMessage = null;
        if (updatedMessage == null || newMessage == null) {
            return returnMessage;
        }
        else if (newMessage.getMessage_text() == null) {
            return returnMessage;
        }
        else if (newMessage.getMessage_text().length() > 255 || 
                newMessage.getMessage_text().trim().isEmpty()) {
            return returnMessage;
        }
        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ? LIMIT 1;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, newMessage.getMessage_text());
            preparedStatement.setInt(2, updatedMessage.getMessage_id());

            preparedStatement.executeUpdate();

            returnMessage = this.findMessage(updatedMessage.getMessage_id());
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return returnMessage;
    }

    /**
     * Retrieve a list of messages from the message table based on their posted_by/account_id.
     * @return all messages from a particular user.
     */
    public List<Message> findUserMessages(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

}