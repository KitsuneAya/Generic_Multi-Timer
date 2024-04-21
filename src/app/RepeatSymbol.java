package app;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static app.GlobalConstants.ROW_HEIGHT;


/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @version 1.0
 * @apiNote This script generates an ImageIcon of the repeat/loop symbol 🔁,
 * but scales the symbol so it more closely resembles a circle rather than a horizontally elongated oval.
 * If this ImageIcon is being used more than once, it should be instantiated and made accessible in a way
 * that doesn't require it to be generated multiple times.
 */

public class RepeatSymbol extends ImageIcon {

    private static final int LARGE_SYMBOL_SIZE = 256;

    public RepeatSymbol(Color backgroundColor, Color foregroundColor) {

        // Instantiate the BufferedImage that will hold the 1:1 scaled repeat symbol
        // Use this instance of BufferedImage to calculate the repeat symbol's default sizes
        // to make a large BufferedImage to scale the symbol down from
        BufferedImage repeatSymbol = new BufferedImage(ROW_HEIGHT, ROW_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D repeatSymbolGraphics = repeatSymbol.createGraphics();
        repeatSymbolGraphics.setFont(new Font("", Font.PLAIN, LARGE_SYMBOL_SIZE));
        String symbol = "\uD83D\uDD01";

        int symbolWidth = repeatSymbolGraphics.getFontMetrics().stringWidth(symbol);
        int symbolHeight = repeatSymbolGraphics.getFontMetrics().getHeight();

        // Instantiate the BufferedImage that holds the large version of the non 1:1 scale repeat symbol
        BufferedImage unscaledRepeatSymbol = new BufferedImage(symbolWidth, symbolHeight*715/1000, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D unscaledRepeatSymbolGraphics = unscaledRepeatSymbol.createGraphics();

        // The draw string method will fill the whole canvas
        // and draws with the origin in the bottom left
        int stringDrawXPos = 0;
        int stringDrawYPos = symbolHeight*635/1000;

        // Set the colors of the symbol
        unscaledRepeatSymbolGraphics.setBackground(backgroundColor);
        unscaledRepeatSymbolGraphics.setColor(foregroundColor);

        // Draw the non 1:1 scale repeat symbol
        unscaledRepeatSymbolGraphics.setFont(repeatSymbolGraphics.getFont());
        unscaledRepeatSymbolGraphics.clearRect(0, 0, symbolWidth, symbolHeight);
        unscaledRepeatSymbolGraphics.drawString(symbol, stringDrawXPos, stringDrawYPos);

        // Draw the non 1:1 scale symbol to the 1:1 scale canvas
        repeatSymbolGraphics.setBackground(backgroundColor);
        repeatSymbolGraphics.clearRect(0, 0, ROW_HEIGHT, ROW_HEIGHT);
        repeatSymbolGraphics.drawImage(unscaledRepeatSymbol.getScaledInstance(ROW_HEIGHT*70/100, ROW_HEIGHT*85/100, BufferedImage.SCALE_SMOOTH), ROW_HEIGHT*20/100, ROW_HEIGHT*10/100, null);


        this.setImage(repeatSymbol);

    }

}
