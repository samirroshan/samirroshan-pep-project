package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
        public static List<Message> getAllMessages() {
            List<Message> messages = new ArrayList<>();
            try(Connection connection = ConnectionUtil.getConnection()){
                String sql = "SELECT * FROM message";

                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    messages.add(new Message(
                        rs.getInt("message_id"), 
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }                
            return messages;
        }
    

    
        public static Message getMessageById(int id) {
        
            try(Connection connection = ConnectionUtil.getConnection()){
                String sql = "SELECT * FROM message WHERE message_id = ?";

                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Message message = new Message(
                        rs.getInt("message_id"), 
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                        return message;
                }
            }catch (Exception e){
                e.printStackTrace();
            }                
            return null;
        }

        public static List<Message> getMessageFromUserMessage(int accountId) {
            List<Message> messages   = new ArrayList<>();
            try(Connection connection = ConnectionUtil.getConnection()){
                String sql = "SELECT * FROM message WHERE posted_by = ?";

                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, accountId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    
                
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")); 
                messages.add(message);
                }
            }catch (Exception e){
                e.printStackTrace();
            }                
            return messages;
        }

        public static Message createNewMessage(Message message){
            try(Connection conn = ConnectionUtil.getConnection()){

            
                String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setInt(1, message.getPosted_by());
                ps.setString(2, message.getMessage_text());
                ps.setLong(3, message.getTime_posted_epoch());
                int affectedrows = ps.executeUpdate();
                //ps.executeUpdate();
                if (affectedrows > 0) {
                    ResultSet resultSet = ps.getGeneratedKeys();
                    if (resultSet.next()) {
                        int msgid = resultSet.getInt(1);

                        message.setMessage_id(msgid);
                        return message;
                    }
                }
                
                /* 
                if (resultSet.next()) {
                    message.setMessage_id(resultSet.getInt(1));
                }
                    */
                //return message;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        public static Message deleteMessageById(int id){
            Message message = getMessageById(id);
            if (message == null){
                return null;
            }
        
            try (Connection conn = ConnectionUtil.getConnection()){
                String sql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setInt(1, id);
                ps.executeUpdate();

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return message;
        }

        public static Message updateMessage(Message message){
            try(Connection conn = ConnectionUtil.getConnection()){
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, message.getMessage_text());
                ps.setInt(2, message.getMessage_id());
                ps.executeUpdate();

                int r=ps.executeUpdate();
                if(r > 0) {
                    return getMessageById(message.getMessage_id());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return message;
        }
}
