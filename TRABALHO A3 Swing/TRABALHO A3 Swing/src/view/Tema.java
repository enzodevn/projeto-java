package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Tema {

    public static final Color BG_PRINCIPAL     = new Color(240, 242, 248);
    public static final Color BG_SIDEBAR       = new Color(30,  41,  59);
    public static final Color BG_CARD          = Color.WHITE;
    public static final Color BG_INPUT         = Color.WHITE;
    public static final Color ACCENT           = new Color(37, 99, 235);
    public static final Color ACCENT_HOVER     = new Color(29, 78, 216);
    public static final Color SUCESSO          = new Color(22, 163, 74);
    public static final Color PERIGO           = new Color(220, 38, 38);
    public static final Color AVISO            = new Color(202, 138, 4);
    public static final Color TEXTO_PRINCIPAL  = new Color(15,  23,  42);
    public static final Color TEXTO_SECUNDARIO = new Color(100, 116, 139);
    public static final Color TEXTO_SIDEBAR    = new Color(226, 232, 240);
    public static final Color BORDA            = new Color(203, 213, 225);
    public static final Color LINHA_TABELA     = new Color(241, 245, 249);
    public static final Color SEL_TABELA       = new Color(219, 234, 254);
    public static final Color SEL_TABELA_TEXT  = new Color(30,  64, 175);

    public static final Font FONTE_TITULO   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONTE_SECAO    = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONTE_NORMAL   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONTE_PEQUENA  = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONTE_MONO     = new Font("Consolas",  Font.PLAIN, 13);

    public static void aplicarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        UIManager.put("Panel.background",             BG_PRINCIPAL);
        UIManager.put("OptionPane.background",        BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXTO_PRINCIPAL);
        UIManager.put("Button.background",            ACCENT);
        UIManager.put("Button.foreground",            Color.WHITE);
        UIManager.put("Button.focus",                 new Color(0,0,0,0));
        UIManager.put("TextField.background",         BG_INPUT);
        UIManager.put("TextField.foreground",         TEXTO_PRINCIPAL);
        UIManager.put("TextField.caretForeground",    TEXTO_PRINCIPAL);
        UIManager.put("TextField.selectionBackground",ACCENT);
        UIManager.put("TextField.selectionForeground",Color.WHITE);
        UIManager.put("PasswordField.background",     BG_INPUT);
        UIManager.put("PasswordField.foreground",     TEXTO_PRINCIPAL);
        UIManager.put("PasswordField.caretForeground",TEXTO_PRINCIPAL);
        UIManager.put("ComboBox.background",          BG_INPUT);
        UIManager.put("ComboBox.foreground",          TEXTO_PRINCIPAL);
        UIManager.put("ComboBox.selectionBackground", ACCENT);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("List.background",              BG_INPUT);
        UIManager.put("List.foreground",              TEXTO_PRINCIPAL);
        UIManager.put("List.selectionBackground",     ACCENT);
        UIManager.put("List.selectionForeground",     Color.WHITE);
        UIManager.put("Table.background",             BG_CARD);
        UIManager.put("Table.foreground",             TEXTO_PRINCIPAL);
        UIManager.put("Table.selectionBackground",    SEL_TABELA);
        UIManager.put("Table.selectionForeground",    SEL_TABELA_TEXT);
        UIManager.put("Table.gridColor",              LINHA_TABELA);
        UIManager.put("TableHeader.background",       BG_PRINCIPAL);
        UIManager.put("TableHeader.foreground",       TEXTO_SECUNDARIO);
        UIManager.put("ScrollPane.background",        BG_CARD);
        UIManager.put("Viewport.background",          BG_CARD);
        UIManager.put("TabbedPane.background",        BG_PRINCIPAL);
        UIManager.put("TabbedPane.foreground",        TEXTO_PRINCIPAL);
        UIManager.put("TabbedPane.selected",          BG_CARD);
        UIManager.put("TabbedPane.focus",             new Color(0,0,0,0));
        UIManager.put("SplitPane.background",         BG_PRINCIPAL);
        UIManager.put("Label.foreground",             TEXTO_PRINCIPAL);
    }

    public static JButton botaoPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_SECAO);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(ACCENT_HOVER); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(ACCENT); }
        });
        return btn;
    }

    public static JButton botaoPerigo(String texto) {
        JButton btn = botaoPrimario(texto);
        btn.setBackground(PERIGO);
        Color hover = new Color(185, 28, 28);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(PERIGO); }
        });
        return btn;
    }

    public static JButton botaoSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_NORMAL);
        btn.setBackground(BG_CARD);
        btn.setForeground(TEXTO_PRINCIPAL);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA, 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BG_PRINCIPAL); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(BG_CARD); }
        });
        return btn;
    }

    public static JTextField campo() {
        JTextField tf = new JTextField();
        tf.setFont(FONTE_NORMAL);
        tf.setBackground(BG_INPUT);
        tf.setForeground(TEXTO_PRINCIPAL);
        tf.setCaretColor(TEXTO_PRINCIPAL);
        tf.setOpaque(true);
        tf.setEditable(true);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT, 2),
                    BorderFactory.createEmptyBorder(7, 9, 7, 9)));
            }
            public void focusLost(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDA, 1),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }
        });
        return tf;
    }

    public static JPasswordField campaSenha() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONTE_NORMAL);
        pf.setBackground(BG_INPUT);
        pf.setForeground(TEXTO_PRINCIPAL);
        pf.setCaretColor(TEXTO_PRINCIPAL);
        pf.setOpaque(true);
        pf.setEditable(true);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        pf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                pf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT, 2),
                    BorderFactory.createEmptyBorder(7, 9, 7, 9)));
            }
            public void focusLost(FocusEvent e) {
                pf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDA, 1),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }
        });
        return pf;
    }

    @SuppressWarnings("unchecked")
    public static <T> JComboBox<T> combo() {
        JComboBox<T> cb = new JComboBox<>();
        cb.setFont(FONTE_NORMAL);
        cb.setBackground(BG_INPUT);
        cb.setForeground(TEXTO_PRINCIPAL);
        cb.setOpaque(true);
        cb.setBorder(BorderFactory.createLineBorder(BORDA, 1));
        cb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(FONTE_NORMAL);
                if (isSelected) { setBackground(ACCENT); setForeground(Color.WHITE); }
                else            { setBackground(BG_INPUT); setForeground(TEXTO_PRINCIPAL); }
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
        return cb;
    }

    public static JLabel label(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONTE_NORMAL);
        lbl.setForeground(TEXTO_SECUNDARIO);
        return lbl;
    }

    public static JLabel titulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONTE_TITULO);
        lbl.setForeground(TEXTO_PRINCIPAL);
        return lbl;
    }

    public static JTable tabela(String[] colunas, Object[][] dados) {
        JTable table = new JTable(dados, colunas) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? BG_CARD : LINHA_TABELA);
                    c.setForeground(TEXTO_PRINCIPAL);
                }
                return c;
            }
        };
        table.setFont(FONTE_NORMAL);
        table.setBackground(BG_CARD);
        table.setForeground(TEXTO_PRINCIPAL);
        table.setGridColor(LINHA_TABELA);
        table.setRowHeight(34);
        table.setSelectionBackground(SEL_TABELA);
        table.setSelectionForeground(SEL_TABELA_TEXT);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(FONTE_SECAO);
        table.getTableHeader().setBackground(BG_PRINCIPAL);
        table.getTableHeader().setForeground(TEXTO_SECUNDARIO);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDA));
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    public static JScrollPane scroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(BORDA, 1));
        sp.getViewport().setBackground(BG_CARD);
        sp.setBackground(BG_CARD);
        return sp;
    }

    public static void erro(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    public static void sucesso(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    public static boolean confirmar(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
