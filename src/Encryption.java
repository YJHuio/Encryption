import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Encryption extends JFrame {
    public static void main(String[] args) {
        new Encryption();
    }

    public Encryption() {
        this.setSize(800, 600);
        this.setLocation(400, 200);
        this.setLayout(new GridLayout(3, 1));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        setComponent();
        addComponent();
        this.show();

    }

    class myJPanel extends JPanel {
        JLabel j = new JLabel();
        JProgressBar p = new JProgressBar();

        myJPanel() {
            setSize(800,50);
            setLayout(new GridLayout(1, 2));
            p.setForeground(new Color(112, 173, 71));
            this.add(j);
            this.add(p);
        }
    }

    private final JPanel p1 = new JPanel();
    private final JPanel p2 = new JPanel();
    private final JPanel p3 = new JPanel();


    private final JButton fileChooser = new JButton("选择文件");
    private final JButton en_decode = new JButton("加/解密");


    private myJPanel[] progress = new myJPanel[30];

    private final JTextField key = new JTextField("24位密钥(支持数字和字母)");

    private final File[] file = new File[30];

    JTextArea message = new JTextArea();

    Box vBox = Box.createVerticalBox();


    private void addComponent() {

        JScrollPane jScrollPane = new JScrollPane(vBox);

        p2.add(jScrollPane);


        p1.add(fileChooser);
        p1.add(key);
        p1.add(en_decode);

        p3.add(message);


        this.add(p1);
        this.add(p2);
        this.add(p3);

    }

    private void setComponent() {

        p1.setLayout(new GridLayout(1, 3));


        p2.setLayout(new GridLayout(1, 1));
//        p2_1.setLayout(new GridLayout(3, 1));
//        p2_2.setLayout(new GridLayout(3, 1));

        p3.setLayout(new GridLayout(1, 1));

        Font f = new Font(null, Font.BOLD, 30);
        en_decode.setFont(f);
        fileChooser.setFont(f);

        key.setHorizontalAlignment(JTextField.CENTER);
        key.setFont(new Font(null, Font.BOLD, 16));

        message.setBorder(BorderFactory.createLineBorder(Color.gray, 5));
        message.setEditable(false);

        en_decode.setBackground(new Color(0, 112, 192));

        addActionListener(fileChooser);
        addActionListener(en_decode);

    }

    public void addActionListener(JButton button) {

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (button.getText()) {
                    case "选择文件": {
                        for (int i = 0; i < 30; i++) {
                            if (file[i] == null) {
                                JFileChooser jfc = new JFileChooser();
                                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                                jfc.showDialog(new JLabel(), "选择");
                                File f = jfc.getSelectedFile();
//                               取消选择
                                if (f == null) return;
                                else {
                                    int j = 0;
                                    while (j < 30) {
                                        if (progress[j++] == null) continue;
                                        j--;
                                        if (progress[j++].j.getText().equals(f.getName())) {
                                            JOptionPane.showMessageDialog(null, "文件已存在！");
                                            return;

                                        }
                                    }

                                    myJPanel m = new myJPanel();
                                    m.j.setText(f.getName());
                                    m.setVisible(true);
//                                    pack();
//                                    setSize(800,600);

                                    vBox.add(m);



                                    vBox.revalidate();

                                    file[i] = f;
                                    progress[i] = m;
//                                    fileName[i].setText(f.getName());]

                                    return;


                                }
                            }
                        }
                        JOptionPane.showMessageDialog(null, "文件数量限制");
                        break;
                    }

                    case "加/解密": {
                        int n = 0;
                        for (int i = 0; i < 30; i++) if (file[i] != null) n++;
                        if (n < 1) {
                            JOptionPane.showMessageDialog(null, "请选择文件");
                            break;
                        } else if (!isKey(key.getText())) {
                            JOptionPane.showMessageDialog(null, "密钥不合法!");
                            break;
                        } else {
                            for (int i = 0; i < 30; i++) {
                                if (file[i] != null) {
//                                    progress[i].setVisible(true);
                                    D_En_code_progress p = new D_En_code_progress();
                                    p.setFile(file[i], i);
                                    p.start();
                                }
                            }

                        }
                        break;
                    }
                }
            }
        });

    }

    private boolean isKey(String s) {

        String key = "(?i)([a-z]|\\d){24}";

        Pattern p1 = Pattern.compile(key);
        Matcher ffMing = p1.matcher(s);
        if (ffMing.find() && s.length() < 25) {
            return true;
        } else {
            return false;
        }
    }

    class D_En_code_progress extends Thread {
        File f = null;
        int f_id = -1;

        public void setFile(File file, int id) {
            f = file;
            f_id = id;
        }

        @Override
        public void run() {
            String type = "(?i)enc$";
            Pattern p1 = Pattern.compile(type);
            Matcher ffMing = p1.matcher(f.getName());
//            解密
            if (ffMing.find()) {
                try {
                    FileInputStream fis = new FileInputStream(f.getAbsoluteFile());
                    byte[] data_byte = new byte[fis.available()];
                    fis.read(data_byte);
                    fis.close();

                    data_byte = DESUtil.deCode(key.getText().getBytes(), data_byte);
                    String path = f.getAbsolutePath().replace(".enc", "");
                    FileOutputStream fos = new FileOutputStream(path);


//                    延时
                    Random rand = new Random();
                    while (progress[f_id].p.getValue() != 100) {
                        try {
                            Thread.sleep(rand.nextInt(3) * 1000);
                            progress[f_id].p.setValue(progress[f_id].p.getValue() + 10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    fos.write(data_byte);
                    String mes = message.getText();
                    message.setText(mes + "文件：" + f.getName() + "解密成功，输出：" + path + "\n");

                    init();

                    fos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            加密
            else {
                try {
                    FileInputStream fis = new FileInputStream(f.getAbsoluteFile());
                    byte[] data_byte = new byte[fis.available()];
                    fis.read(data_byte);
                    fis.close();

                    data_byte = DESUtil.enCode(key.getText().getBytes(), data_byte);

//                    延时
                    Random rand = new Random();
                    while (progress[f_id].p.getValue() != 100) {
                        try {
                            Thread.sleep(rand.nextInt(3) * 1000);
                            progress[f_id].p.setValue(progress[f_id].p.getValue() + 10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    FileOutputStream fos = new FileOutputStream(f.getAbsoluteFile() + ".enc");
                    fos.write(data_byte);
                    String mes = message.getText();
                    message.setText(mes + "文件：" + f.getName() + "加密成功，输出：" + f.getAbsoluteFile() + ".enc\n");

                    init();

                    fos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        private void init() {
//            vBox.remove();/
            vBox.remove(progress[f_id]);
            progress[f_id]=null;
            file[f_id] = null;
        }
    }
}

