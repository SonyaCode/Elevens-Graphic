import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.awt.Rectangle;

public class Card {
    private String suit;
    private String rank;
    private int value;
    private String imageFileName;
    private String backImageFileName;
    private boolean show;
    private BufferedImage image;
    private Rectangle cardBox;
    private boolean highlight;

    public static ArrayList<Card> deck = Card.buildDeck();

    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
        this.imageFileName = "images/card_"+suit+"_"+rank+".png";
        this.show = true;
        this.backImageFileName = "images/card_back.png";
        this.image = readImage();
        this.cardBox = new Rectangle(-100, -100, image.getWidth(), image.getHeight());
        this.highlight = false;
    }

    public Rectangle getCardBox() {
        return cardBox;
    }

    public String getSuit() {
        return suit;
    }

    public void setRectangleLocation(int x, int y) {
        cardBox.setLocation(x, y);
    }

    public String getRank() {
        return rank;
    }
    public int getValue() { return value; }

    public String getImageFileName() {
        return imageFileName;
    }

    public String toString() {
        return suit + " " + rank;
    }

    public void flipCard() {
        show = !show;
        this.image = readImage();
    }

    public void flipHighlight() {
        highlight = !highlight;
    }

    public boolean getHighlight() {
        return highlight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage readImage() {
        try {
            BufferedImage image;
            if (show) {
                image = ImageIO.read(new File(imageFileName));
            }
            else {
                image = ImageIO.read(new File(backImageFileName));
            }
            return image;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static ArrayList<Card> buildDeck() {
        ArrayList<Card> deck = new ArrayList<Card>();
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        String[] rank = {"02", "03", "04", "05", "06", "07", "08", "09", "10", "A", "J", "K", "Q"};
        int[] value = {2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 0, 0, 0};
        for (String s : suits) {
            for (int i = 0; i < rank.length; i++) {
                Card c = new Card(s, rank[i], value[i]);
                deck.add(c);
            }
        }
        return deck;
    }

    public static ArrayList<Card> buildHand() {
        ArrayList<Card> hand = new ArrayList<Card>();

        for (int i = 0; i < 9; i++) {
            int r = (int)(Math.random()*deck.size());
            Card c = deck.remove(r);
            hand.add(c);
        }
        return hand;
    }

    public static void switchOneCard(ArrayList<Card> hand, int index) {
        if (deck.size() > 0) {
            int randomCardIndex = (int) (Math.random() * deck.size());
            Card randomCard = deck.remove(randomCardIndex);
            hand.set(index, randomCard);
        } else {
            hand.remove(index);
        }

    }

    public static void containsSum11(ArrayList<Card> hand, ArrayList<Card> selectedCards, ArrayList<Integer> indexList) {
        Card selectedCard1 = selectedCards.get(0);
        Card selectedCard2 = selectedCards.get(1);

        if (selectedCards.size() == 2 && selectedCard1.getValue() + selectedCard2.getValue() == 11) {
            if (deck.size() == 0) {
                Card.switchOneCard(hand, indexList.get(0));
                indexList.set(1, indexList.get(1) - 1);
                Card.switchOneCard(hand, indexList.get(1));
            } else {
                Card.switchOneCard(hand, indexList.get(0));
                Card.switchOneCard(hand, indexList.get(1));
            }

        }
    }

    public static void containsJQK(ArrayList<Card> hand, ArrayList<Card> selectedCards, ArrayList<Integer> indexList) {
        Card selectedCard1 = selectedCards.get(0);
        Card selectedCard2 = selectedCards.get(1);
        Card selectedCard3 = selectedCards.get(2);

        if (selectedCard1.getValue() + selectedCard2.getValue() + selectedCard3.getValue() == 0
                && !selectedCard1.getRank().equals(selectedCard2.getRank())
                && !selectedCard2.getRank().equals(selectedCard3.getRank())
                && !selectedCard3.getRank().equals(selectedCard1.getRank())) {

            if (deck.size() == 0) {
                Card.switchOneCard(hand, indexList.get(0));
                indexList.set(1, indexList.get(1) - 1);
                indexList.set(2, indexList.get(2) - 1);
                Card.switchOneCard(hand, indexList.get(1));
                indexList.set(2, indexList.get(2) - 1);
                Card.switchOneCard(hand, indexList.get(2));
            } else {
                Card.switchOneCard(hand, indexList.get(0));
                Card.switchOneCard(hand, indexList.get(1));
                Card.switchOneCard(hand, indexList.get(2));
            }

        }
    }

    public static boolean hasPossiblePlay(ArrayList<Card> hand) {
        // check to see if the card values add up to 11
        ArrayList<Integer> valueList = new ArrayList<Integer>();

        for (Card card : hand) {
            valueList.add(card.getValue());
        }

        for (int i = 0; i < valueList.size(); i++) {
            int cardValue = valueList.get(i);
            if (valueList.contains(11 - cardValue)) {
                return true;
            }
        }

        // check to see if all JQK are present
        boolean foundJ = false;
        boolean foundQ = false;
        boolean foundK = false;

        for (Card card : hand) {
            if (card.getRank().equals("J")) {
                foundJ = true;
            } else if (card.getRank().equals("Q")) {
                foundQ = true;
            } else if (card.getRank().equals("K")) {
                foundK = true;
            }
        }

        if (foundJ && foundQ && foundK) {
            return true;
        }

        return false;
    }

}
