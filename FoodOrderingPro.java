import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.DecimalFormat;

/**
 * FoodOrderingPro.java
 * Fixed version: avoids capturing non-final local variables in lambdas.
 *
 * Put images under ./images/ (pizza.png, burger.png, momos.png, friedrice.png)
 */
public class FoodOrderingPro extends JFrame {

    // Data model for menu item
    static class MenuItem {
        String id;
        String name;
        double price;
        String imagePath;
        MenuItem(String id, String name, double price, String imagePath) {
            this.id = id; this.name = name; this.price = price; this.imagePath = imagePath;
        }
    }

    // Cart entry
    static class CartEntry {
        MenuItem item;
        int qty;
        CartEntry(MenuItem item, int qty) { this.item = item; this.qty = qty; }
    }

    private java.util.List<MenuItem> menu = new ArrayList<>();
    private java.util.List<CartEntry> cart = new ArrayList<>();
    private JLabel cartBadge;
    private JPanel mainGrid;
    private JFrame thisFrame;

    public FoodOrderingPro() {
        thisFrame = this;
        setTitle("TasteHub — Professional Ordering");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // sample menu
        menu.add(new MenuItem("m1","Pizza",199,"pizza.png"));
        menu.add(new MenuItem("m2","Burger",149,"burger.png"));
        menu.add(new MenuItem("m3","Momos",99,"momos.png"));
        menu.add(new MenuItem("m4","Fried Rice",129,"friedrice.png"));

        // top bar
        add(createTopBar(), BorderLayout.NORTH);

        // center: cards
        mainGrid = createCardsPanel();
        JScrollPane sc = new JScrollPane(mainGrid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sc.getVerticalScrollBar().setUnitIncrement(14);
        sc.setBorder(null);
        add(sc, BorderLayout.CENTER);

        // show
        setVisible(true);
    }

    // ----------------- TOP NAVBAR -----------------
    private JPanel createTopBar() {
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(1000,70));
        top.setBackground(new Color(34, 40, 49)); // dark theme
        top.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(220,220,220,10)));

        JLabel brand = new JLabel("TasteHub");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        brand.setBounds(20, 12, 300, 40);
        top.add(brand);

        // subtitle
        JLabel sub = new JLabel("Restaurant Ordering");
        sub.setForeground(new Color(200,200,200));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setBounds(22, 50, 300, 20);
        top.add(sub);

        // Cart badge
        JButton cartBtn = new RoundedButton(" View Cart ");
        cartBtn.setBounds(820, 18, 140, 36);
        cartBtn.setBackground(new Color(255, 99, 72));
        cartBtn.setForeground(Color.WHITE);
        cartBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartBtn.addActionListener(e -> toggleSidebar());

        // badge (count)
        cartBadge = new JLabel("0");
        cartBadge.setOpaque(true);
        cartBadge.setBackground(new Color(255,220,220));
        cartBadge.setForeground(new Color(180,30,30));
        cartBadge.setHorizontalAlignment(SwingConstants.CENTER);
        cartBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cartBadge.setBounds(957, 10, 26, 26);
        cartBadge.setBorder(BorderFactory.createLineBorder(new Color(255,200,200)));
        cartBadge.setVisible(true);

        top.add(cartBtn);
        top.add(cartBadge);
        return top;
    }

    // ----------------- CARDS PANEL -----------------
    private JPanel createCardsPanel() {
        JPanel grid = new JPanel(new GridLayout(0,2,24,24));
        grid.setBorder(new EmptyBorder(24,24,24,24));
        grid.setBackground(new Color(245,245,245));
        for (MenuItem mi : menu) grid.add(makeFoodCard(mi));
        return grid;
    }

    // create a single food card
    private JPanel makeFoodCard(MenuItem mi) {
        JPanel card = new ShadowPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(420, 260));

        // image area
        JLabel img = new JLabel();
        ImageIcon ic = loadImageIcon(mi.imagePath, 220, 140);
        if (ic!=null) img.setIcon(ic);
        else {
            img.setText("[image]");
            img.setHorizontalAlignment(SwingConstants.CENTER);
            img.setForeground(Color.GRAY);
        }
        img.setBounds(20, 15, 220, 140);
        card.add(img);

        // title
        JLabel name = new JLabel(mi.name);
        name.setFont(new Font("Segoe UI", Font.BOLD, 20));
        name.setBounds(260, 30, 200, 28);
        card.add(name);

        // desc sample
        JTextArea desc = new JTextArea("Delicious " + mi.name + " - freshly prepared. Add to cart and enjoy!");
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setOpaque(false);
        desc.setEditable(false);
        desc.setFocusable(false);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setBounds(260, 60, 150, 60);
        card.add(desc);

        // price
        JLabel price = new JLabel("₹" + new DecimalFormat("#0.00").format(mi.price));
        price.setFont(new Font("Segoe UI", Font.BOLD, 18));
        price.setForeground(new Color(34, 40, 49));
        price.setBounds(260, 128, 120, 28);
        card.add(price);

        // quantity controls
        int spinnerWidth = 120;
        JPanel qty = new JPanel(null);
        qty.setBounds(260, 160, spinnerWidth, 36);
        qty.setOpaque(false);

        JButton minus = smallIconButton("-");
        JButton plus = smallIconButton("+");
        JLabel qtyLabel = new JLabel("1", SwingConstants.CENTER);
        qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        qtyLabel.setBounds(40, 0, 40, 36);
        qtyLabel.setOpaque(true);
        qtyLabel.setBackground(new Color(250,250,250));
        qtyLabel.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        minus.setBounds(0,0,36,36);
        plus.setBounds(80,0,36,36);

        qty.add(minus); qty.add(qtyLabel); qty.add(plus);
        card.add(qty);

        // Add to cart (big gradient)
        GradientButton add = new GradientButton("Add to Cart");
        add.setBounds(20, 170, 220, 44);
        add.setFont(new Font("Segoe UI", Font.BOLD, 15));
        add.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add.addActionListener(e -> {
            int quantity = Integer.parseInt(qtyLabel.getText());
            addToCart(mi, quantity);
            // small "added" animation style using javax.swing.Timer to avoid ambiguity
            final JWindow w = new JWindow();
            JLabel l = new JLabel("Added " + mi.name + " x" + quantity);
            l.setBorder(new EmptyBorder(8,12,8,12));
            l.setBackground(new Color(34,40,49));
            l.setForeground(Color.WHITE);
            l.setOpaque(true);
            w.add(l);
            w.pack();
            Point p = getLocationOnScreen();
            w.setLocation(p.x + getWidth()/2 - w.getWidth()/2, p.y + 80);
            w.setVisible(true);
            javax.swing.Timer t = new javax.swing.Timer(900, ev->{ w.setVisible(false); w.dispose(); });
            t.setRepeats(false);
            t.start();
        });

        // plus / minus actions
        minus.addActionListener(e -> {
            int v = Integer.parseInt(qtyLabel.getText());
            if (v>1) qtyLabel.setText(String.valueOf(v-1));
        });
        plus.addActionListener(e -> {
            int v = Integer.parseInt(qtyLabel.getText());
            qtyLabel.setText(String.valueOf(v+1));
        });

        card.add(add);

        return card;
    }

    // ----------------- SMALL UI HELPERS -----------------
    private JButton smallIconButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(new Color(250,250,250));
        b.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // load image with safe scaling
    private ImageIcon loadImageIcon(String path, int w, int h) {
        try {
            ImageIcon ic = new ImageIcon(path);
            Image im = ic.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(im);
        } catch (Exception ex) {
            return null;
        }
    }

    // add to cart model and update badge
    private void addToCart(MenuItem mi, int qty) {
        // combine if exists
        for (CartEntry ce : cart) {
            if (ce.item.id.equals(mi.id)) { ce.qty += qty; updateBadge(); return; }
        }
        cart.add(new CartEntry(mi, qty));
        updateBadge();
    }

    private void updateBadge() {
        int totalItems = 0;
        for (CartEntry ce : cart) totalItems += ce.qty;
        cartBadge.setText(String.valueOf(totalItems));
    }

    // show cart dialog
    private void toggleSidebar() {
        // open a cart dialog (better compatibility)
        JDialog d = new JDialog(thisFrame, "Your Cart", true);
        d.setSize(420, 520);
        d.setLocationRelativeTo(thisFrame);
        d.setLayout(new BorderLayout());

        // header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBorder(new EmptyBorder(12,12,12,12));
        hdr.setBackground(new Color(245,245,245));
        JLabel t = new JLabel("Your Order");
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        hdr.add(t, BorderLayout.WEST);

        d.add(hdr, BorderLayout.NORTH);

        // content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(12,12,12,12));
        content.setBackground(Color.WHITE);

        if (cart.isEmpty()) {
            JLabel empty = new JLabel("Your cart is empty. Add tasty items!");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            empty.setForeground(Color.GRAY);
            empty.setBorder(new EmptyBorder(20,20,20,20));
            content.add(empty);
        } else {
            for (CartEntry ce : new ArrayList<>(cart)) { // iterate copy for safe removal
                JPanel row = new JPanel(new BorderLayout());
                row.setMaximumSize(new Dimension(380, 70));
                row.setBackground(new Color(250,250,250));
                row.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

                JLabel nm = new JLabel(ce.item.name + "  x" + ce.qty);
                nm.setFont(new Font("Segoe UI", Font.BOLD, 14));
                row.add(nm, BorderLayout.WEST);

                JLabel pr = new JLabel("₹" + new DecimalFormat("#0.00").format(ce.item.price * ce.qty));
                pr.setFont(new Font("Segoe UI", Font.BOLD, 14));
                row.add(pr, BorderLayout.EAST);

                // remove button
                JButton rem = new JButton("Remove");
                rem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                rem.addActionListener(ae -> { cart.remove(ce); d.dispose(); updateBadge(); toggleSidebar(); });
                row.add(rem, BorderLayout.SOUTH);

                content.add(row);
                content.add(Box.createRigidArea(new Dimension(0,8)));
            }
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        d.add(scroll, BorderLayout.CENTER);

        // footer (total + checkout)
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(12,12,12,12));
        footer.setBackground(new Color(245,245,245));

        // compute total here inside method (so not captured by lambda)
        double totalNow = 0;
        for (CartEntry ce : cart) totalNow += ce.qty * ce.item.price;

        JLabel totalLabel = new JLabel("Total: ₹" + new DecimalFormat("#0.00").format(totalNow));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        footer.add(totalLabel, BorderLayout.WEST);

        GradientButton checkout = new GradientButton("Checkout");
        checkout.setPreferredSize(new Dimension(160,40));
        checkout.addActionListener(ae -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(d, "Please add items first.");
                return;
            }
            // recompute total inside handler to avoid capturing non-final outer vars
            double recomputedTotal = 0;
            for (CartEntry ce : cart) recomputedTotal += ce.qty * ce.item.price;

            int confirm = JOptionPane.showConfirmDialog(d, "Place order for ₹" + new DecimalFormat("#0.00").format(recomputedTotal) + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm==JOptionPane.YES_OPTION) {
                // mock order placed
                JOptionPane.showMessageDialog(d, "Order placed! Thank you 🍽️");
                cart.clear();
                updateBadge();
                d.dispose();
            }
        });
        footer.add(checkout, BorderLayout.EAST);

        d.add(footer, BorderLayout.SOUTH);

        d.setVisible(true);
    }

    // ----------------- CUSTOM UI CLASSES -----------------
    // Rounded gradient button
    class GradientButton extends JButton {
        Color c1 = new Color(255, 120, 90);
        Color c2 = new Color(255, 87, 51);
        GradientButton(String text){ super(text); setContentAreaFilled(false); setForeground(Color.WHITE); setBorder(null); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(0,0,c1,0,h,c2);
            g2.setPaint(gp);
            g2.fillRoundRect(0,0,w,h,18,18);
            super.paintComponent(g);
            g2.dispose();
        }
        public void setColors(Color a, Color b){ c1=a; c2=b; }
    }

    // rounded small button
    class RoundedButton extends JButton {
        RoundedButton(String t){ super(t); setOpaque(false); setFocusPainted(false); setBorderPainted(false); setBorder(null); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // panel with soft shadow effect
    class ShadowPanel extends JPanel {
        ShadowPanel(){ setOpaque(false); }
        protected void paintComponent(Graphics g) {
            int arc = 18;
            int shadow = 8;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // shadow
            g2.setColor(new Color(0,0,0,20));
            g2.fillRoundRect(shadow, shadow, getWidth()-shadow*2, getHeight()-shadow*2, arc, arc);

            // background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-shadow, getHeight()-shadow, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ----------------- MAIN -----------------
    public static void main(String[] args) {
        // Use system look for nicer fonts rendering
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored){}
        SwingUtilities.invokeLater(() -> new FoodOrderingPro());
    }
}
