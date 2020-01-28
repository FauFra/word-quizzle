import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Register extends JPanel implements ActionListener {
    private JTextField nickInput, pwdInput;
    private JLabel answer;
    private JFrame window;

    public Register(JFrame window){
        this.window = window;

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        nickInput = new JTextField("",10);
        nickInput.setBackground(Color.WHITE);
        pwdInput = new JTextField("",10);

        JPanel nickPanel = new JPanel();
        nickPanel.add(new JLabel("Nickname = "));
        nickPanel.add(nickInput);
        JPanel pwdPanel = new JPanel();
        pwdPanel.add(new JLabel("Password = "));
        pwdPanel.add(pwdInput);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,4));

        JButton go = new JButton("GO");

        go.addActionListener(this);

        buttonPanel.add(go);

        answer = new JLabel("", JLabel.CENTER);
        answer.setForeground(Color.BLACK);
        answer.setBackground(Color.WHITE);
        answer.setOpaque(true);

        setLayout(new GridLayout(4,1,3,3));
        add(nickPanel);
        add(pwdPanel);
        add(buttonPanel);
        add(answer);

    }

    public void actionPerformed(ActionEvent actionEvent) {
        String nick, pwd;
        nick = nickInput.getText();
        pwd = pwdInput.getText();

        try{

            Registry registry = LocateRegistry.getRegistry(RMIRegistrationInterface.PORT);
            RMIRegistrationInterface serverObject = (RMIRegistrationInterface) registry.lookup(RMIRegistrationInterface.REMOTE_OBJECT_NAME);

            String message = null;
            int response = serverObject.registra_utente(nick,pwd);
            switch(response){ //effettuo la registrazione e leggo la risposta
                case -101: message = "Nickname non valido";
                    break;
                case -102: message = "Nickname già esistente";
                    break;
                case -103: message = "Password non valida";
                    break;
                case -104: message = "Password troppo corta. Inserire una password di almeno 5 caratteri";
                    break;
                case -105: message = "Password troppo lunga. Inserire una password di massimo 20 caratteri";
                    break;
                case -106: message = "Il nickname non può contenere spazi";
                    break;
                case -1: message = "Errore generico";
                    break;
                case 1: message = "Ok";
                    break;
            }
            answer.setText(message);
            if(response>0){
                Login login = new Login(window);
                window.setContentPane(login);
                window.validate();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Errore durante la registrazione");
            answer.setText("Errore col server");
            e.printStackTrace();
        }
    }
}
