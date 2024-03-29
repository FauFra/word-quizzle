import Database.DBMS;
import User.User;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

public class RMIRegistrationImpl extends RemoteObject implements RMIRegistrationInterface {
    private static RMIRegistrationImpl serverRMI;
    private DBMS dbms;

    private RMIRegistrationImpl(){
        dbms = DBMS.getIstance();
    }

    public static RMIRegistrationImpl getServerRMI(){
        if(serverRMI==null) serverRMI = new RMIRegistrationImpl();
        return serverRMI;
    }

    @Override
    public int registra_utente(String nick, String pwd) throws RemoteException {
        if(nick == null || nick.equals("")) return INVALID_NICK;
        if(nick.contains(" ")) return SPACE_IN_NICK;
        if(pwd ==  null || pwd.equals("")) return INVALID_PWD;
        if(pwd.length()<5) return TOO_SHORT_PWD;
        if(pwd.length()>20) return TOO_LONG_PWD;
        if(existOnDB(nick)) return EXISTS_NICK;
        if(registerOnDB(nick,pwd)) return OK;

        return GENERAL_ERROR;
    }

    private boolean existOnDB(String nick){
        return dbms.existUser(nick);
    }

    private boolean registerOnDB(String nick, String pwd){
        return dbms.registerUser(new User(nick,pwd));
    }
}
