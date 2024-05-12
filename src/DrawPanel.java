import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.awt.Font;

class DrawPanel extends JPanel implements MouseListener {

    private ArrayList<Card> hand;
    private Rectangle button;
    private Rectangle replaceButton;

    public DrawPanel() {
        button = new Rectangle(155, 300, 160, 26);
        replaceButton = new Rectangle(155, 250, 160, 26);
        this.addMouseListener(this);
        hand = Card.buildHand();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 130;
        int y = 10;
        int count = 0;

        for (int i = 0; i < hand.size(); i++) {
            count++;
            Card c = hand.get(i);
            if (c.getHighlight()) {
                g.drawRect(x, y, c.getImage().getWidth(), c.getImage().getHeight());
            }
            c.setRectangleLocation(x, y);
            g.drawImage(c.getImage(), x, y, null);
            x = x + c.getImage().getWidth() + 10;

            if (count % 3 == 0) {
                x = 130;
                y = y + c.getImage().getHeight() + 10;
            }
        }


        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("RESTART", 190, 320);
        g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());

        g.drawString("Cards left: " + Card.deck.size(), 5, 450);

        g.drawString("REPLACE CARDS", 157, 270);
        g.drawRect((int) replaceButton.getX(), (int) replaceButton.getY(), (int) replaceButton.getWidth(), (int) button.getHeight());

        if (Card.deck.size() == 0 && hand.size() == 0) {
            g.drawString("You win!", 190, 360);
        } else if (!Card.hasPossiblePlay(hand)) {
            g.drawString("No available moves! GAME OVER!", 75, 360);
        }
    }

    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        if (e.getButton() == 1) {
            if (button.contains(clicked)) {
                Card.deck = Card.buildDeck();
                hand = Card.buildHand();
            } else if (replaceButton.contains(clicked)) { // when click the replace button, check the selected cards
                ArrayList<Card> selectedCards = new ArrayList<Card>();
                ArrayList<Integer> indexList = new ArrayList<Integer>();
                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i).getHighlight()) {
                        selectedCards.add(hand.get(i));
                        indexList.add(i);
                    }
                }
                if (selectedCards.size() == 2) {
                    Card.containsSum11(hand, selectedCards, indexList);
                } else if (selectedCards.size() == 3) {
                    Card.containsJQK(hand, selectedCards, indexList);
                }
            }

            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    hand.get(i).flipCard();
                }
            }
        }

        if (e.getButton() == 3) {
            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();

                if (box.contains(clicked)) {
                    hand.get(i).flipHighlight();
                }

                /* Class exercise
                if (box.contains(clicked) && hand.get(i).getHighlight()) {
                    Card.switchOneCard(hand, i);
                } else if (box.contains(clicked)) {
                    hand.get(i).flipHighlight();
                }
                 */

            }
        }

    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}