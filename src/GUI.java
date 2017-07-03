import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by QuiteRiver on 28.11.16.
 */
public class GUI extends JFrame{
    private JMenuBar menuBar = new JMenuBar();
    private final Highlighter.HighlightPainter myhighlight = new MyHighLighter(Color.red);
    public GUI(){
        super("JavaPad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280,720);
        add(new Panel(),new BorderLayout().CENTER);
        pack();
        setJMenuBar(menuBar);
        setVisible(true);
    }
   private class Panel extends JPanel{
       private JFrame frameReplace = null;
       private JFrame frameFind =null;
       private JFrame frameChangeCharset = null;
       private JFrame frameAbout = null;
       private JFrame frameFont = null;
       private JFrame frameTextColor = null;
       private JFrame frameBackGroundColor = null;
       private JFrame frameSelectionColor = null;
       private JFrame frameC = null;
       private JFrame frameJ = null;
       private JFrame frameJJ = null;
       private JTextPane jTextPane = new JTextPane();
       private JFileChooser chooser;
       private JList charsets;
       private String path = null;
       private String currentCharset = "";
       private final String[] size = {"6","7","8","9","10","11","12","13","14","15","16","17","18","20","22","24","26","28","32","36","40","48","56","64","72"};
       private final String[] styles = {"Bold","Italic","Plain"};
       private final String[] color ={"White","Black","Cyan","Blue","Dark Gray","Gray","Light Gray","Green","Magenta","Orange","Pink","Red","Yellow"};
       private void replace(){
           if (frameReplace==null){
               frameReplace = new JFrame("Replace Text");
           }
           frameReplace.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           frameReplace.setSize(320,100);
           frameReplace.setLayout(new FlowLayout());
           JTextField field = new JTextField();
           JTextField field2 = new JTextField();
           field.setPreferredSize(new Dimension(100,20));
           field2.setPreferredSize(new Dimension(100,20));
           JPanel panel = new JPanel();
           JPanel panel2 = new JPanel();
           JPanel panel3 = new JPanel();
           panel.setPreferredSize(new Dimension(100,100));
           panel2.setPreferredSize(new Dimension(100,100));
           panel3.setPreferredSize(new Dimension(100,100));
           panel.add(new JLabel("Find What:"));
           panel.add(field);
           panel2.add(new JLabel("Replace with"));
           panel2.add(field2);
           JButton button = new JButton("Replace");
           panel3.add(new JLabel("Replace"));
           panel3.add(button);
           frameReplace.add(panel);
           frameReplace.add(panel2);
           frameReplace.add(panel3);
           button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   String s = jTextPane.getText().replaceAll(field.getText(),field2.getText());
                   jTextPane.setText(s);
               }
           });
           frameReplace.setResizable(false);
           frameReplace.setVisible(true);
       }
       private void find(){
           if (frameFind==null){
               frameFind = new JFrame("Find Text");
           }
           frameFind.setSize(new Dimension(320,200));
           JTextField field = new JTextField();
           field.setSize(new Dimension(200,200));
           frameFind.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           field.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   try {
                       findText(jTextPane,field.getText());
                   } catch (BadLocationException e1) {
                       e1.printStackTrace();
                   }
               }
           });
           frameFind.add(field,BorderLayout.NORTH);
           frameFind.setResizable(false);
           frameFind.setVisible(true);
       }
       private void changeCharsets(){
           if (frameChangeCharset==null){
               frameChangeCharset = new JFrame("Change charset");
           }
           frameChangeCharset.setSize(320,300);
           frameChangeCharset.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           Map<String,Charset> charsetsAvailable = Charset.availableCharsets();
           charsets = new JList(charsetsAvailable.keySet().toArray());
           JTextArea textArea = new JTextArea();
           textArea.setEditable(false);
           textArea.setLineWrap(true);
           textArea.setWrapStyleWord(true);
           textArea.setPreferredSize(new Dimension(320,120));
           JButton button = new JButton("OK");
           button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   jTextPane.setText(textArea.getText());
                   frameChangeCharset.dispose();
               }
           });
           charsets.addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {
                   try {
                       String s = new String(jTextPane.getText().getBytes(),charsets.getSelectedValue().toString());
                       currentCharset = new String("".getBytes(),charsets.getSelectedValue().toString());
                       textArea.setText(s);
                   } catch (UnsupportedEncodingException e1) {
                       e1.printStackTrace();
                   }
               }
           });
           charsets.setVisibleRowCount(12);
           frameChangeCharset.add(new JScrollPane(charsets),BorderLayout.WEST);
           frameChangeCharset.add(new JScrollPane(textArea),BorderLayout.CENTER);
           frameChangeCharset.add(button,BorderLayout.SOUTH);
           frameChangeCharset.setResizable(false);
           frameChangeCharset.setVisible(true);
       }
       private void about(){
           if (frameAbout==null){
               frameAbout = new JFrame("About");
           }
           frameAbout.setSize(new Dimension(320,100));
           frameAbout.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           frameAbout.setLayout(new FlowLayout());
           JLabel label = new JLabel("JavaPad 0.1.3");
           JLabel jLabel = new JLabel("Created by Quite River 28.11.2016");
           label.setFont(new Font(Font.SERIF,Font.BOLD,18));
           jLabel.setFont(new Font(Font.SANS_SERIF,Font.ITALIC,14));
           frameAbout.add(label);
           frameAbout.add(jLabel);
           frameAbout.setResizable(false);
           frameAbout.setVisible(true);
       }
       private void setFont(){
           if (frameFont==null){
               frameFont=new JFrame("Font");
           }
           frameFont.setSize(new Dimension(400,260));
           frameFont.setLayout(new FlowLayout());
           JPanel panelFamily = new JPanel();
           panelFamily.setPreferredSize(new Dimension(620,420));
           JList<String> listStyle = new JList<String>(styles);
           listStyle.setVisibleRowCount(12);
           JList<String> listSize = new JList<String>(size);
           listSize.setVisibleRowCount(12);
           String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
           JList<String> list = new JList(fonts);
           list.setVisibleRowCount(12);
           list.setLayoutOrientation(JList.VERTICAL);
           listSize.setSelectedIndex(12);
           list.addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {

                   jTextPane.setFont(new Font(list.getSelectedValue(), listStyle.getSelectedIndex(), listSize.getSelectedIndex()));
               }
           });
           listStyle.addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {

                   int[] index = listStyle.getSelectedIndices();
                   for (byte i = 0; i < index.length; i++) {
                       if (index[i] == 0)
                           jTextPane.setFont(new Font(list.getSelectedValue(), Font.BOLD, listSize.getSelectedIndex()));
                       if (index[i] == 1)
                           jTextPane.setFont(new Font(list.getSelectedValue(), Font.ITALIC, listSize.getSelectedIndex()));
                       if (index[i] == 2)
                           jTextPane.setFont(new Font(list.getSelectedValue(), Font.PLAIN, listSize.getSelectedIndex()));
                   }
               }
           });
           listSize.addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {

                   jTextPane.setFont(new Font(list.getSelectedValue(),listStyle.getSelectedIndex(),listSize.getSelectedIndex()));
               }
           });
           panelFamily.add(new JScrollPane(list));
           panelFamily.add(new JScrollPane(listStyle));
           panelFamily.add(new JScrollPane(listSize));
           frameFont.add(panelFamily);
           frameFont.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           frameFont.setResizable(false);
           frameFont.setVisible(true);
       }
       private void textColor(){
           if (frameTextColor==null){
               frameTextColor=new JFrame("Text Color");
           }
           frameTextColor.setSize(120,150);
           frameTextColor.setLayout(new FlowLayout());
           frameTextColor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           JList<String> colors = new JList<String>(color);
           colors.setVisibleRowCount(6);
           frameTextColor.add(new JScrollPane(colors));
           colors.addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {
                   byte index = (byte) colors.getSelectedIndex();
                   switch (index){
                       case 0: jTextPane.setForeground(Color.WHITE);
                           break;
                       case 1: jTextPane.setForeground(Color.BLACK);
                           break;
                       case 2: jTextPane.setForeground(Color.CYAN);
                           break;
                       case 3: jTextPane.setForeground(Color.BLUE);
                           break;
                       case 4: jTextPane.setForeground(Color.DARK_GRAY);
                           break;
                       case 5: jTextPane.setForeground(Color.GRAY);
                           break;
                       case 6: jTextPane.setForeground(Color.LIGHT_GRAY);
                           break;
                       case 7: jTextPane.setForeground(Color.GREEN);
                           break;
                       case 8: jTextPane.setForeground(Color.MAGENTA);
                           break;
                       case 9: jTextPane.setForeground(Color.ORANGE);
                           break;
                       case 10: jTextPane.setForeground(Color.PINK);
                           break;
                       case 11: jTextPane.setForeground(Color.RED);
                           break;
                       case 12: jTextPane.setForeground(Color.YELLOW);
                           break;

                   }
               }
           });
           frameTextColor.setResizable(false);
           frameTextColor.setVisible(true);
       }
       private void backGroundColor(){
           if (frameBackGroundColor==null){
               frameBackGroundColor = new JFrame("BackGround Color");
           }
           frameBackGroundColor.setSize(120,150);
           frameBackGroundColor.setLayout(new FlowLayout());
           frameBackGroundColor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           JList<String> colors = new JList<String>(color);
           colors.setVisibleRowCount(6);
           frameBackGroundColor.add(new JScrollPane(colors));
           colors.addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {
                   byte index = (byte) colors.getSelectedIndex();
                   switch (index){
                       case 0: jTextPane.setBackground(Color.WHITE);
                           break;
                       case 1: jTextPane.setBackground(Color.BLACK);
                           break;
                       case 2: jTextPane.setBackground(Color.CYAN);
                           break;
                       case 3: jTextPane.setBackground(Color.BLUE);
                           break;
                       case 4: jTextPane.setBackground(Color.DARK_GRAY);
                           break;
                       case 5: jTextPane.setBackground(Color.GRAY);
                           break;
                       case 6: jTextPane.setBackground(Color.LIGHT_GRAY);
                           break;
                       case 7: jTextPane.setBackground(Color.GREEN);
                           break;
                       case 8: jTextPane.setBackground(Color.MAGENTA);
                           break;
                       case 9: jTextPane.setBackground(Color.ORANGE);
                           break;
                       case 10: jTextPane.setBackground(Color.PINK);
                           break;
                       case 11: jTextPane.setBackground(Color.RED);
                           break;
                       case 12: jTextPane.setBackground(Color.YELLOW);
                           break;

                   }
               }
           });
           frameBackGroundColor.setResizable(false);
           frameBackGroundColor.setVisible(true);
       }
       private void selectionColor(){
           if (frameSelectionColor==null){
               frameSelectionColor = new JFrame("Selection Color");
           }
           frameSelectionColor.setSize(120,150);
           frameSelectionColor.setLayout(new FlowLayout());
           frameSelectionColor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           JList<String> colors = new JList<String>(color);
           colors.setVisibleRowCount(6);
           frameSelectionColor.add(new JScrollPane(colors));
           colors.addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {
                   byte index = (byte) colors.getSelectedIndex();
                   switch (index){
                       case 0: jTextPane.setSelectionColor(Color.WHITE);
                           break;
                       case 1: jTextPane.setSelectionColor(Color.BLACK);
                           break;
                       case 2: jTextPane.setSelectionColor(Color.CYAN);
                           break;
                       case 3: jTextPane.setSelectionColor(Color.BLUE);
                           break;
                       case 4: jTextPane.setSelectionColor(Color.DARK_GRAY);
                           break;
                       case 5: jTextPane.setSelectionColor(Color.GRAY);
                           break;
                       case 6: jTextPane.setSelectionColor(Color.LIGHT_GRAY);
                           break;
                       case 7: jTextPane.setSelectionColor(Color.GREEN);
                           break;
                       case 8: jTextPane.setSelectionColor(Color.MAGENTA);
                           break;
                       case 9: jTextPane.setSelectionColor(Color.ORANGE);
                           break;
                       case 10: jTextPane.setSelectionColor(Color.PINK);
                           break;
                       case 11: jTextPane.setSelectionColor(Color.RED);
                           break;
                       case 12: jTextPane.setSelectionColor(Color.YELLOW);
                           break;

                   }
               }
           });
           frameSelectionColor.setResizable(false);
           frameSelectionColor.setVisible(true);
       }
       private void save(){
           if (path ==null) {
               chooser = new JFileChooser();
               chooser.setDialogTitle("Save");
               chooser.setApproveButtonText("Save");
               chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
               chooser.setAcceptAllFileFilterUsed(false);


               switch (chooser.showOpenDialog(Panel.this)) {
                   case JFileChooser.APPROVE_OPTION:
                       try (BufferedWriter br = new BufferedWriter(new FileWriter(chooser.getSelectedFile()))) {
                           br.write(jTextPane.getText());
                       } catch (IOException exp) {
                           exp.printStackTrace();
                           JOptionPane.showMessageDialog(Panel.this, "Failed to save file", "Error", JOptionPane.ERROR_MESSAGE);
                       }
               }
           } else {
               try (BufferedWriter br = new BufferedWriter(new FileWriter(new File(path)))) {
                   br.write(jTextPane.getText());
               } catch (IOException exp) {
                   exp.printStackTrace();
                   JOptionPane.showMessageDialog(Panel.this, "Failed to save file", "Error", JOptionPane.ERROR_MESSAGE);
               }
           }
       }
       private void create(){
           if (chooser == null) {
               chooser = new JFileChooser();
               chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
               chooser.setAcceptAllFileFilterUsed(false);

           }
           chooser.setDialogTitle("Create");
           chooser.setApproveButtonText("Create");

           switch (chooser.showOpenDialog(Panel.this)) {
               case JFileChooser.APPROVE_OPTION:
                   if (!Files.exists(Paths.get(chooser.getSelectedFile().getAbsolutePath()))) {
                       try {
                           Files.createFile(Paths.get(chooser.getSelectedFile().getAbsolutePath()));
                           path = chooser.getSelectedFile().getAbsolutePath();
                       } catch (IOException exp) {
                           exp.printStackTrace();
                           JOptionPane.showMessageDialog(Panel.this, "Failed to create file", "Error", JOptionPane.ERROR_MESSAGE);
                       }
                       break;
                   }
           }
       }
       private void addTo(){
           if (chooser == null) {
               chooser = new JFileChooser();
               chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
               chooser.setAcceptAllFileFilterUsed(false);
           }
           chooser.setDialogTitle("Add to...");
           chooser.setApproveButtonText("Add to");

           switch (chooser.showOpenDialog(Panel.this)) {
               case JFileChooser.APPROVE_OPTION:
                   try (BufferedWriter br = new BufferedWriter(new FileWriter(chooser.getSelectedFile(),true))) {
                       path =chooser.getSelectedFile().getAbsolutePath();
                       br.write("\n");
                       br.write(jTextPane.getText());
                   } catch (IOException exp) {
                       exp.printStackTrace();
                       JOptionPane.showMessageDialog(Panel.this, "Failed add to file", "Error", JOptionPane.ERROR_MESSAGE);
                   }
                   break;
           }
       }
       private void saveAs(){
           if (chooser==null) {
               chooser = new JFileChooser();
               chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
               chooser.setAcceptAllFileFilterUsed(false);
           }
           chooser.setDialogTitle("Save As");
           chooser.setApproveButtonText("Save As");

           switch (chooser.showOpenDialog(Panel.this)) {
               case JFileChooser.APPROVE_OPTION:
                   try (BufferedWriter br = new BufferedWriter(new FileWriter(chooser.getSelectedFile()))) {
                       path =chooser.getSelectedFile().getAbsolutePath();
                       br.write(jTextPane.getText());
                   } catch (IOException exp) {
                       exp.printStackTrace();
                       JOptionPane.showMessageDialog(Panel.this, "Failed to save file", "Error", JOptionPane.ERROR_MESSAGE);
                   }
                   break;
           }
       }
       private void load(){
           if (chooser == null) {
               chooser = new JFileChooser();
               chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
               chooser.setAcceptAllFileFilterUsed(false);
//               chooser.addChoosableFileFilter(new FileFilter() {
//                   @Override
//                   public boolean accept(File f) {
//                       return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
//                   }
//
//                   @Override
//                   public String getDescription() {
//                       return "Text Files (*.txt)";
//                   }
//
//               });
           }
           chooser.setDialogTitle("Open");
           chooser.setApproveButtonText("Load");

           switch (chooser.showOpenDialog(Panel.this)) {
               case JFileChooser.APPROVE_OPTION:
                   try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
                       path =chooser.getSelectedFile().getAbsolutePath();
                       JFrame frame = new JFrame();
                       progress(frame);
                       jTextPane.setText(null);
                       String text = null;
                       StringBuilder builder = new StringBuilder();
                       while ((text = br.readLine()) != null) {
                           builder.append(text);
                           builder.append("\n");
                       }
                       jTextPane.setText(builder.toString());
                       jTextPane.setCaretPosition(0);
                       builder=null;
                       frame.dispose();
                   } catch (IOException exp) {
                       exp.printStackTrace();
                       JOptionPane.showMessageDialog(Panel.this, "Failed to read file", "Error", JOptionPane.ERROR_MESSAGE);
                   }
                   break;
           }
       }

       private void cCompiling(){
           if (frameC == null){
               frameC = new JFrame("C compiling");
           }
           frameC.setLayout(null);
           frameC.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           JTextField fieldIn = new JTextField();
           JTextField fieldOut = new JTextField();
           fieldIn.setSize(new Dimension(50,20));
           fieldOut.setSize(new Dimension(50,20));
           JLabel labelIn = new JLabel("In File");
           JLabel labelOut = new JLabel("Out File");
           JButton button = new JButton("Choose a compiling file");
           JButton buttonC = new JButton("Compile");
           buttonC.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   try {
                       Process proc = Runtime.getRuntime().exec("gcc " + fieldIn.getText()+" -o " + fieldOut.getText());
                       proc.waitFor();
                       BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                       BufferedReader bufError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                       String line = "";
                       while ((line=buf.readLine())!=null) {
                           System.out.println(line);
                       }
                       while ((line=bufError.readLine())!=null) {
                           System.out.print(line);
                       }
                   } catch (IOException e1) {
                       e1.printStackTrace();
                   } catch (InterruptedException e1) {
                       e1.printStackTrace();
                   }
                   frameC.dispose();
               }
           });
           button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   if (chooser==null) {
                       chooser = new JFileChooser();
                       chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                       chooser.setAcceptAllFileFilterUsed(false);
                   }
                   chooser.setDialogTitle("Choose a compiling file");
                   chooser.setApproveButtonText("Choose");

                   switch (chooser.showOpenDialog(Panel.this)) {
                       case JFileChooser.APPROVE_OPTION:
                           fieldIn.setText(chooser.getSelectedFile().getAbsolutePath());
                   }
               }
           });
           if (path!=null){
               fieldIn.setText(path);
           }
           frameC.setSize(new Dimension(340,100));
           labelIn.setBounds(5,5,50,20);
           fieldIn.setBounds(60,5,270,20);
           labelOut.setBounds(5,30,55,20);
           fieldOut.setBounds(65,30,265,20);
           button.setBounds(5,60,200,20);
           buttonC.setBounds(210,60,120,20);
           frameC.add(labelIn);
           frameC.add(fieldIn);
           frameC.add(labelOut);
           frameC.add(fieldOut);
           frameC.add(button);
           frameC.add(buttonC);
           frameC.setResizable(false);
           frameC.setVisible(true);
       }
       private void javaCompiling(){
           if (frameJ == null){
               frameJ = new JFrame("Java compiling");
           }
           frameJ.setLayout(null);
           frameJ.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           JTextField fieldIn = new JTextField();
           fieldIn.setSize(new Dimension(50,20));
           JLabel labelIn = new JLabel("File");
           JTextArea area = new JTextArea();
           area.setLineWrap(true);
           area.setWrapStyleWord(true);
           area.setEditable(false);
           JScrollPane scrollPane = new JScrollPane(area);
           JButton button = new JButton("Choose a compiling file");
           JButton buttonC = new JButton("Compile");
           buttonC.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   area.setText("");
                   String[] data;
                   data=fieldIn.getText().split(File.separator);
                   StringBuilder text=new StringBuilder();
                   for (int i=0;i<data.length;i++){
                       if (i==data.length-1){
                           break;
                       }
                       text.append(data[i] + File.separator);
                   }
                   try {
                       Process proc = Runtime.getRuntime().exec("javac -sourcepath " + text + " " + fieldIn.getText());
                       proc.waitFor();
                       BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                       BufferedReader bufError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                       String line = "";
                       while ((line=buf.readLine())!=null) {
                           area.append(line);
                           area.append("\n");
                       }
                       while ((line=bufError.readLine())!=null) {
                           area.append(line);
                           area.append("\n");
                       }
                   } catch (IOException e1) {
                       e1.printStackTrace();
                   } catch (InterruptedException e1) {
                       e1.printStackTrace();
                   }
                   if (area.getText().isEmpty())
                       frameJ.dispose();
               }
           });
           button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   if (chooser==null) {
                       chooser = new JFileChooser();
                       chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                       chooser.setAcceptAllFileFilterUsed(false);
                   }
                   chooser.setDialogTitle("Choose a compiling file");
                   chooser.setApproveButtonText("Choose");

                   switch (chooser.showOpenDialog(Panel.this)) {
                       case JFileChooser.APPROVE_OPTION:
                           fieldIn.setText(chooser.getSelectedFile().getAbsolutePath());
                   }
               }
           });
           if (path!=null){
               fieldIn.setText(path);
           }
           frameJ.setSize(new Dimension(340,240));
           labelIn.setBounds(5,5,50,20);
           fieldIn.setBounds(60,5,270,20);
           scrollPane.setBounds(5,35,330,160);
           button.setBounds(5,210,200,20);
           buttonC.setBounds(210,210,120,20);
           frameJ.add(labelIn);
           frameJ.add(fieldIn);
           frameJ.add(button);
           frameJ.add(buttonC);
           frameJ.add(scrollPane);
           frameJ.setResizable(false);
           frameJ.setVisible(true);
       }
        public Panel(){
            setLayout(new BorderLayout());
            jTextPane.setPreferredSize(new Dimension(1280,720));
            JMenu menuFile = new  JMenu("File");
            JMenu menuEditing = new JMenu("Editing");
            JMenu menuFind = new JMenu("Find");
            JMenu menuCode = new JMenu("Code");
            JMenu menuCharsets = new JMenu("Charsets");
            JMenu menuSettings = new JMenu("Settings");
            JMenu menuAbout = new JMenu("About");
            JMenuItem loadItem = new JMenuItem("Load File");
            JMenuItem fontItem = new JMenuItem("Font");
            JMenuItem fontColor = new JMenuItem("Text Color");
            JMenuItem backGroundColor = new JMenuItem("BackGround Color");
            JMenuItem selectionSetColor = new JMenuItem("Selection Color");
            JMenuItem findItem = new JMenuItem("Find Text");
            JMenuItem replaceItem = new JMenuItem("Replace Text");
            loadItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            load();
                        }
                    }).start();
                }
            });
            JMenuItem saveAsItem = new JMenuItem("Save as");
            saveAsItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveAs();
                        }
                    }).start();
                }
            });
            JMenuItem addToFile = new JMenuItem("Add to File");
            addToFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addTo();
                }
            });
            JMenuItem createItem = new JMenuItem("Create File");
            createItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    create();
                }
            });
            JMenuItem saveItem = new JMenuItem("Save File");
            saveItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            save();
                        }
                    }).start();
                }
            });
            selectionSetColor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameSelectionColor==null)
                    selectionColor();
                    else frameSelectionColor.setVisible(true);
                }
            });
            backGroundColor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameBackGroundColor==null)
                   backGroundColor();
                    else frameBackGroundColor.setVisible(true);
                }
            });
            fontColor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameTextColor==null)
                   textColor();
                    else frameTextColor.setVisible(true);
                }
            });
            fontItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameFont==null)
                    setFont();
                    else frameFont.setVisible(true);
                }
            });
            JMenuItem clearItem = new JMenuItem("Clear");
            clearItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        jTextPane.setText(currentCharset);
                }
            });
            JMenuItem removeItem = new JMenuItem("Remove HighLights");
            removeItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeHighLights(jTextPane);
                }
            });
            JMenuItem aboutItem = new JMenuItem("about");
            aboutItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameAbout==null)
                     about();
                    else frameAbout.setVisible(true);
                }
            });
            JMenuItem charsetsItem = new JMenuItem("Change charset");
            charsetsItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameChangeCharset==null)
                    changeCharsets();
                    else frameChangeCharset.setVisible(true);
                }
            });
            JMenuItem cCompileItem = new JMenuItem("C compiling");
            cCompileItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameC==null)
                        cCompiling();
                    else frameC.setVisible(true);
                }
            });
            JMenuItem jCompileItem = new JMenuItem("Java Compiling");
            jCompileItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameJ==null)
                    javaCompiling();
                    else frameJ.setVisible(true);
                }
            });
            menuFile.add(createItem);
            menuFile.add(loadItem);
            menuFile.add(saveItem);
            menuFile.add(saveAsItem);
            menuFile.add(addToFile);
            menuEditing.add(clearItem);
            menuEditing.add(removeItem);
            menuAbout.add(aboutItem);
            menuCharsets.add(charsetsItem);
            menuCode.add(cCompileItem);
            menuCode.add(jCompileItem);
            menuBar.add(menuFile);
            menuBar.add(menuEditing);
            menuBar.add(menuFind);
            menuBar.add(menuCode);
            menuBar.add(menuCharsets);
            menuSettings.add(fontItem);
            menuSettings.add(fontColor);
            menuSettings.add(backGroundColor);
            menuSettings.add(selectionSetColor);
            menuBar.add(menuSettings);
            menuBar.add(menuAbout);
            findItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameFind==null)
                    find();
                    else frameFind.setVisible(true);
                }
            });
            replaceItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frameReplace==null)
                   replace();
                    else frameReplace.setVisible(true);
                }
            });
            JScrollPane scroll = new JScrollPane(jTextPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            menuFind.add(findItem);
            menuFind.add(replaceItem);
            add(scroll,BorderLayout.CENTER);
        }
    }
   private class MyHighLighter extends DefaultHighlighter.DefaultHighlightPainter {
       public MyHighLighter(final Color color) {
           super(color);
       }
   }
        private void removeHighLights(final JTextComponent component){
            Highlighter highlighter = component.getHighlighter();
            Highlighter.Highlight[] highlights = highlighter.getHighlights();
            for (int i=0;i<highlights.length;i++){
                if (highlights[i].getPainter() instanceof MyHighLighter){
                    highlighter.removeHighlight(highlights[i]);
                }
            }
        }
        private void findText(final JTextComponent component,final String testString) throws BadLocationException {
            removeHighLights(component);
            Highlighter highlighter = component.getHighlighter();
            Document doc = component.getDocument();
            String text = doc.getText(0,doc.getLength());
            int pos = 0;
            while((pos=text.toUpperCase().indexOf(testString.toUpperCase(),pos))>=0){
                if (testString.isEmpty())
                    break;
                highlighter.addHighlight(pos,pos+testString.length(),myhighlight);
                pos+=testString.length();
            }
        }
        private void progress(JFrame frame){
            frame.setTitle("Waiting");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            ImageIcon icon = new ImageIcon(this.getClass().getResource("progress.gif"));
            JLabel label = new JLabel(icon);
            frame.add(label);
            frame.setSize(150,150);
            frame.setVisible(true);
        }
    }