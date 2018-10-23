package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GUIFrame extends JFrame {
    private JFrame jFrame = new JFrame();
    private JPanel jPanel = new JPanel();


    private JTextField path = new JTextField("D:\\Test",20);
    private JTextField  fileType = new JTextField(".txt",10);
    private JTextArea someText = new JTextArea("text",10,10);
    private JTextArea finalText = new JTextArea("finaltext",20,70);
    private JButton acceptButton = new JButton("Accept");
    private DefaultListModel listModel = new DefaultListModel();
    private JList<String> rootFolder = new JList<>(listModel);
    private List<File> arrayfiles = new ArrayList<>();
    private JButton nextButton = new JButton("Next");
    private JButton prevButton = new JButton("Prev");


    private FileReader jFileReader;

    public String strFileType;
    public String strPath;
    public String strSomeText;

    public GUIFrame () {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        jFrame.add(jPanel);
        jPanel.setLayout(null);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane jScrollPaneSomeText = new JScrollPane(someText, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane jScrollPaneFinalText = new JScrollPane(finalText, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane jScrollPaneRootFolder = new JScrollPane(rootFolder, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        jScrollPaneFinalText.setBounds(700,50,520,500);
        jScrollPaneSomeText.setBounds(300,100,200,100);
        jScrollPaneRootFolder.setBounds(300,200,200,250);
        fileType.setBounds(700,20,100,20);
        path.setBounds(450,20,200,20);
        acceptButton.setBounds(600,50,75,50);
        prevButton.setBounds(700,550,75,50);
        nextButton.setBounds(800,550,75,50);

        finalText.setWrapStyleWord(true);
        finalText.setLineWrap(true);
        someText.setWrapStyleWord(true);
        someText.setLineWrap(true);


        jFrame.setBounds(dimension.width/2 - 600,dimension.height/2 - 360, 1400, 720);
        jFrame.setTitle("Test1");


        acceptButtonAction();
        listSelectionListener();
        nextButtonActtion();
        prevButtonAction();

        jPanel.add(prevButton);
        jPanel.add(nextButton);
        jPanel.add(fileType);
        jPanel.add(jScrollPaneSomeText);
        jPanel.add(path);
        jPanel.add(jScrollPaneRootFolder);
        jPanel.add(jScrollPaneFinalText);
        jPanel.add(acceptButton);
    }
    public void nextButtonActtion(){
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    finalText.setText(jFileReader.readText(false));
                }catch (IOException e1){
                    e1.printStackTrace();
                }

            }
        });
    }

    public void prevButtonAction(){
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    finalText.setText(jFileReader.readText(true));
                }catch (IOException e1){
                    e1.printStackTrace();
                }

            }
        });
    }

    public void acceptButtonAction (){
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strFileType = fileType.getText();
                strPath = path.getText();
                strSomeText = someText.getText();
                listModel.removeAllElements();
                jPanel.revalidate();
                arrayfiles.clear();
                try {
                    arrayfiles = FileManager.findArrayFiles(strPath,strFileType);
                    for (File s: arrayfiles) {
                        if(FileManager.findFiles(s,strSomeText)) {
                            listModel.addElement(s.getAbsolutePath());
                            jFileReader = new FileReader(s.getAbsolutePath());
                            finalText.setText(jFileReader.readText(false));
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    public void listSelectionListener (){
        rootFolder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rootFolder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount() == 2){
                    try {
                        jFileReader = new FileReader(rootFolder.getSelectedValue());
                        finalText.setText(jFileReader.readText(true));
                    } catch ( IOException e1){
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
