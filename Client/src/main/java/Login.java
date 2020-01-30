import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Login extends JPanel implements ActionListener{
    private JTextField nickInput, pwdInput;
    private JLabel answer,connectAnswer;
    private JFrame window;
    private SocketChannel client;
    private int BUF_SIZE = 512;
//    private int socketPort;

    public Login(JFrame window){
        this.window = window;

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        nickInput = new JTextField("fausto",10);
        nickInput.setBackground(Color.WHITE);
        pwdInput = new JTextField("faustofausto",10);
        pwdInput.setBackground(Color.WHITE);

        JPanel nickPanel = new JPanel();
        nickPanel.add(new JLabel("Nickname = "));
        nickPanel.add(nickInput);
        JPanel pwdPanel = new JPanel();
        pwdPanel.add(new JLabel("Password = "));
        pwdPanel.add(pwdInput);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,4));

        JButton go = new JButton("GO");
        JButton connect = new JButton("CONNECT");

        go.addActionListener(this);
        connect.addActionListener(this);

        buttonPanel.add(go);
        buttonPanel.add(connect);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(1,2));

        answer = new JLabel("", JLabel.CENTER);
        answer.setForeground(Color.BLACK);
        answer.setBackground(Color.WHITE);
        answer.setOpaque(true);
        connectAnswer = new JLabel("", JLabel.CENTER);
        connectAnswer.setForeground(Color.BLACK);
        connectAnswer.setBackground(Color.WHITE);
        connectAnswer.setOpaque(true);
        answerPanel.add(answer);
        answerPanel.add(connectAnswer);

        setLayout(new GridLayout(4,1,3,3));
        add(nickPanel);
        add(pwdPanel);
        add(buttonPanel);
        add(answerPanel);

        this.serverConnection();
        this.window.validate();
    }

    //Connessione TCP col server
    private void serverConnection(){
        SocketAddress address = new InetSocketAddress(TCPConnection.HOSTNAME,TCPConnection.PORT);
        try{
            if(client!= null && client.isOpen()) {
                System.out.println("[RECONNECTION] Chiudo la socket");
                client.close();// todo elimina
            }
            client = SocketChannel.open(address);
        } catch (IOException e) {
            System.out.println("[ERROR] Server non disponibile");
            connectAnswer.setText("Server non disponibile");
            return;
        }

//        socketPort = client.socket().getLocalPort();
//        Thread t = new Thread(new UDPListener(socketPort,window));
//        t.start(); //TODO elimina

        System.out.println("[OK] Connessione col server stabilita");
        connectAnswer.setText("Connessione stabilita");
    }


    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("GO")){

            String nick, pwd;
            nick = nickInput.getText();
            pwd = pwdInput.getText();

            //Controllo che i campi di input non siano vuoti
            if(nick.equals("")) {
                answer.setText("Inserire un nickname");
                return;
            }
            if(pwd.equals("")){
                answer.setText("Inserire una password");
                return;
            }

            String request = "LOGIN\n"+nick+"\n"+pwd+"\n"; //Creo la stringa del protocollo TODO eliminare new string

            ByteBuffer buffer = ByteBuffer.allocate(request.length());

            buffer.put(request.getBytes());
            buffer.flip();

            //Invio la stringa di registrazione al server con nick e password
            while (buffer.hasRemaining()) {
                try {
                    client.write(buffer);
                } catch (Exception e) {
                    System.out.println("[ERROR] Errore scrittura del buffer nella socket del server (LOGIN)");
                    connectAnswer.setText("Errore di connessione col server");
                    return;
                }
            }

            buffer = ByteBuffer.allocate(BUF_SIZE);

            try {
                int read = client.read(buffer);

                if(read == -1){
                    System.out.println("[ERROR] Errore lettura della socket del server (LOGIN)");
                    connectAnswer.setText("Impossibile comunicare col server");
                    return;
                }
                else {
                    String aux[] = (new String(buffer.array())).split("\n");
                    System.out.println("[RESPONSE] "+aux[1]);
                    answer.setText(aux[1]);

                    if(aux[0].equals("OK")){ //se il login è andato a buon fine mostro la homepage
                        int socketPort = client.socket().getLocalPort();
                        Thread t = new Thread(new UDPListener(socketPort,window,client,nick));
                        t.start();

                        HomePage homePage = new HomePage(nick,window,client);
                        window.setContentPane(homePage);
                        window.validate();
                    }
                }
            } catch (IOException e) {
                System.out.println("[ERROR] Server chiuso");
                connectAnswer.setText("Impossibile comunicare col server");
                e.printStackTrace();
            }
        }
        else{
            this.serverConnection();
        }
    }
}
