import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class BlocoDeNotas extends JFrame implements ActionListener {
    JTextArea textArea;
    JFileChooser fileChooser;

    public BlocoDeNotas() {
        // Configuração da Janela
        setTitle("Bloco de Notas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Área de Texto
        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Arquivo");
        menuBar.add(fileMenu);

        JMenuItem newFile = new JMenuItem("Novo");
        newFile.addActionListener(this);
        fileMenu.add(newFile);

        JMenuItem openFile = new JMenuItem("Abrir");
        openFile.addActionListener(this);
        fileMenu.add(openFile);

        JMenuItem saveFile = new JMenuItem("Salvar");
        saveFile.addActionListener(this);
        fileMenu.add(saveFile);

        JMenuItem saveAsFile = new JMenuItem("Salvar Como");
        saveAsFile.addActionListener(this);
        fileMenu.add(saveAsFile);

        JMenuItem exit = new JMenuItem("Sair");
        exit.addActionListener(this);
        fileMenu.add(exit);

        fileChooser = new JFileChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Novo":
                textArea.setText("");
                break;
            case "Abrir":
                abrirArquivo();
                break;
            case "Salvar":
                salvarArquivo();
                break;
            case "Salvar Como":
                salvarArquivoComo();
                break;
            case "Sair":
                System.exit(0);
                break;
        }
    }

    private void abrirArquivo() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                textArea.read(br, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo: " + ex.getMessage());
            }
        }
    }

    private void salvarArquivo() {
        if (fileChooser.getSelectedFile() != null) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                textArea.write(bw);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + ex.getMessage());
            }
        } else {
            salvarArquivoComo();
        }
    }

    private void salvarArquivoComo() {
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                textArea.write(bw);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + ex.getMessage());
            }
        }
        
    }
    
}

