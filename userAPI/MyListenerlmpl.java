package userAPI;
import java.awt.*;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MyListenerlmpl implements ActionListener {

    public MyListenerlmpl() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 400, 400);
        Container container = frame.getContentPane();
        container.setLayout(null);

        JPanel card1 = new JPanel();
        JPanel card2 = new JPanel();
        JPanel lanabok = new JPanel();
        JPanel aterlamnaBok = new JPanel();
        JPanel avslutaKonto = new JPanel();
        JPanel registreraKonto = new JPanel();
        JPanel svarlistaMedlem = new JPanel();

        card1.setLayout(null);
        card2.setLayout(null);

        JButton btn = new JButton("Låna bok");
        btn.setBounds(650, 100,300, 70);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(card2);
                card2.revalidate();
            }
        });
        card1.add(btn);


        JButton btn2 = new JButton("Återlämna bok");
        btn2.setBounds(650, 200,300, 70);
        card1.add(btn2);
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(card2);
                card2.revalidate();
            }
        });


        JButton btn3 = new JButton("Avsluta konto");
        btn3.setBounds(650, 300,300, 70);
        card1.add(btn3);
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(avslutaKonto);
                avslutaKonto.revalidate();
            }
        });


        JButton btn4 = new JButton("Registrera konto");
        btn4.setBounds(650, 400,300, 70);
        card1.add(btn4);
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(registreraKonto);
                registreraKonto.revalidate();
            }
        });


        JButton btn5 = new JButton("Svartlista medlem");
        btn5.setBounds(650, 500,300, 70);
        card1.add(btn5);
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(card2);
                card2.revalidate();
            }
        });




        JLabel text = new JLabel("Ange bokens titel");
        text.setBounds(650, 10, 300, 100);
        card2.add(text);

        JTextField textInput = new JTextField(20);
        textInput.setBounds(650, 100, 500, 100);
        card2.add(textInput);

        JLabel aterlamnaText = new JLabel("Skriv in ditt personnuummer:");
        text.setBounds(650, 10, 300, 100);
        card2.add(text);
        JTextField textInput2 = new JTextField(20);
        textInput2.setBounds(650, 100, 500, 100);
        card2.add(textInput2);


        JLabel avslutaText = new JLabel("Medlem anger sin ID nummer");
        textInput.setBounds(600,100,500,100);
        card2.add(textInput);
        avslutaKonto.add(avslutaText);


        JLabel registreraText = new JLabel("Namn och efternamn:");
        //lägga till personnummer och roll
        textInput.setBounds(600,100,500,100);
        card2.add(textInput);
        registreraKonto.add(registreraText);

        JLabel svartlistaText = new JLabel("Skriv in personens personnummer:");
        text.setBounds(650, 10, 300, 100);
        card2.add(text);
        JTextField textInput4 = new JTextField(20);
        textInput2.setBounds(650, 100, 500, 100);
        card2.add(textInput4);



        textInput.setBounds(650, 100, 500, 100);
        registreraKonto.add(textInput);


        frame.setContentPane(card1);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        MyListenerlmpl listenerlmpl = new MyListenerlmpl();

    }

@Override
    public void actionPerformed (ActionEvent arg0){
        throw new UnsupportedOperationException("Det går inte");

}

}






