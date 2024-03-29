import Costanti.Costanti;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ShowRank extends JPanel implements ActionListener,Costanti {
    private JFrame window;
    private SocketChannel client;
    private String nickname;

    public ShowRank(JFrame window, SocketChannel client, String nickname, Map<String, Integer> listaAmici){
        this.window = window;
        this.client = client;
        this.nickname = nickname;

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JPanel headPanel = new JPanel();
        headPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("CLASSIFICA");
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN,25));

        headPanel.add(title);

        JTextPane friendList = new JTextPane();
        friendList.setEditable(false);

        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(attribs,title.getFont().getName());
        StyleConstants.setFontSize(attribs,15);
        StyleConstants.setBold(attribs,false);

        friendList.setCharacterAttributes(attribs,true);
        friendList.setParagraphAttributes(attribs, true);
        StyledDocument doc = friendList.getStyledDocument();

        int i = 1;
        for(String username : listaAmici.keySet()){
            try {
                doc.insertString(doc.getLength(),i+") "+username+"  "+listaAmici.get(username)+"\n\n",attribs);
                i++;
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        JScrollPane friendPanel = new JScrollPane(friendList);
        friendPanel.setBorder(BorderFactory.createEmptyBorder());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton home = new JButton("HOME");

        home.addActionListener(this);
        home.setIcon(new ImageIcon(new ImageIcon(IMAGEPATH+"home.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT)));
        home.setPreferredSize(new Dimension(BUTTONWIDTH,BUTTONHEIGHT));


        buttonPanel.add(home);

        setLayout(new GridLayout(3,1,3,3));
        add(headPanel);
        add(friendPanel);
        add(buttonPanel);
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getActionCommand().equals("HOME")){
            HomePage startGUI = new HomePage(nickname,window,client);
            window.setContentPane(startGUI);
            window.validate();
        }
    }
}
