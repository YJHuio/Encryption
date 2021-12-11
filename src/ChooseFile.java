import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

class FileChooser extends JButton implements ActionListener {

    File file=null;

    public FileChooser() {
        this.setText("选择文件");
        this.setBackground(new Color(242, 242, 242));
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
        jfc.showDialog(new JLabel(), "选择");
        File file = jfc.getSelectedFile();
        System.out.println("file");
        if(file!=null){
            this.file=file;
        }

    }
//    返回打开的文件

    public File getFile() {
        return file;
    }
}