package Tasks;

import Server.ConKey;
import User.User;
import com.google.gson.Gson;

import java.nio.channels.SelectionKey;
import java.util.Vector;

public class ShowFriends implements Runnable{
    private User user;
    private SelectionKey key;

    public ShowFriends(User user, SelectionKey key){
        this.user = user;
        this.key = key;
    }

    @Override
    public void run() {
        Vector<String> friendList;
        StringBuilder string = new StringBuilder();
        Gson gson = new Gson();
        ConKey keyAttachment = (ConKey) key.attachment();

        synchronized (friendList= user.getFriends()){
            string.append("OK\n");
            string.append(gson.toJson(friendList));
            string.append("\n");

            keyAttachment.response = string.toString();
        }

        try {
            key.interestOps(SelectionKey.OP_WRITE);
        }catch (Exception e){
            user.decrementUse();
            e.printStackTrace();
            return;
        }

        user.decrementUse();

    }
}

