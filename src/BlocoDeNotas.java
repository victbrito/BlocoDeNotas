import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.undo.*;

public class BlocoDeNotas extends JFrame implements ActionListener {
    JTextArea textArea;
    JFileChooser fileChooser;
    UndoManager undoManager;

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

        // Menu Arquivo
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

        // Menu Editar
        JMenu editMenu = new JMenu("Editar");
        menuBar.add(editMenu);

        JMenuItem cut = new JMenuItem("Cortar");
        cut.addActionListener(e -> textArea.cut());
        editMenu.add(cut);

        JMenuItem copy = new JMenuItem("Copiar");
        copy.addActionListener(e -> textArea.copy());
        editMenu.add(copy);

        JMenuItem paste = new JMenuItem("Colar");
        paste.addActionListener(e -> textArea.paste());
        editMenu.add(paste);

        JMenuItem undo = new JMenuItem("Desfazer");
        undo.addActionListener(e -> {
            try {
                undoManager.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }
        });
        editMenu.add(undo);

        JMenuItem redo = new JMenuItem("Refazer");
        redo.addActionListener(e -> {
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                ex.printStackTrace();
            }
        });
        editMenu.add(redo);

        JMenuItem find = new JMenuItem("Localizar");
        find.addActionListener(e -> localizarTexto());
        editMenu.add(find);

        JMenuItem replace = new JMenuItem("Substituir");
        replace.addActionListener(e -> substituirTexto());
        editMenu.add(replace);

        fileChooser = new JFileChooser();

        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
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

    private void localizarTexto() {
        String termo = JOptionPane.showInputDialog(this, "Digite o texto a ser localizado:");
        if (termo != null && !termo.isEmpty()) {
            String conteudo = textArea.getText();
            int index = conteudo.indexOf(termo);
            if (index >= 0) {
                textArea.setCaretPosition(index);
                textArea.select(index, index + termo.length());
                textArea.grabFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Texto não encontrado.");
            }
        }
    }

    private void substituirTexto() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField findField = new JTextField(10);
        JTextField replaceField = new JTextField(10);

        panel.add(new JLabel("Localizar:"));
        panel.add(findField);
        panel.add(new JLabel("Substituir por:"));
        panel.add(replaceField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Localizar e Substituir", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String findText = findField.getText();
            String replaceText = replaceField.getText();
            if (!findText.isEmpty()) {
                textArea.setText(textArea.getText().replace(findText, replaceText));
            }
        }
    }
}