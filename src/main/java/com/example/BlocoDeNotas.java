package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

public class BlocoDeNotas extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private UndoManager undoManager;

    public BlocoDeNotas() {
        // Aplicar tema escuro do FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        FlatLaf.updateUI();

        // Configuração da Janela
        setTitle("Bloco de Notas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       // Área de Texto

       textArea = new JTextArea();

       textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
       textArea.setBackground(new Color(43, 43, 43)); // Cor de fundo do texto
       textArea.setForeground(Color.WHITE); // Cor do texto
       textArea.setCaretColor(Color.WHITE); // Cor do cursor

       JScrollPane scrollPane = new JScrollPane(textArea);
       scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
       scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
       add(scrollPane, BorderLayout.CENTER);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BlocoDeNotas().setVisible(true);
        });
    }

    // Custom ScrollBar UI to match the dark theme and rounded edges
    static class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(100, 100, 100);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(new Color(60, 60, 60));
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            g.setColor(thumbColor);
            g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 8, 8);
        }
    }
}
