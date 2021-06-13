package utils;

import java.util.Random;

public class Emoji {
   static Random r = new Random(System.currentTimeMillis());
    private final static String[] emojis={"✌","😂","😝","😁","😱","👉","🙌","🍻",
            "🔥","🌈","☀","🎈","🌹","💄","🎀","⚽","🎾","🏁","😡","👿","🐻","🐶","" +
            "🐬","🐟","🍀","👀","🚗","🍎","💝","💙","👌","❤","😍","😉","😓","😳",
            "💪","💩","🍸","🔑","💖","🌟","🎉","🌺","🎶","👠","🏈","⚾","🏆","👽",
            "💀","🐵","🐮","🐩","🐎","💣","👃","👂","🍓","💘","💜","👊","💋","😘",
            "😜","😵","🙏","👋","🚽","💃","💎","🚀","🌙","🎁","⛄","🌊","⛵","🏀","" +
            "🎱","💰","👶","👸","🐰","🐷","🐍","🐫","🔫","👄","🚲","🍉","💛","💚"};

    public static String getRandom() {
        int max=0x1F64F;
        int min=0x1F601;
        return String.valueOf((char) Math.floor(Math.random() * (max - min + 1) + min));

    }
}
