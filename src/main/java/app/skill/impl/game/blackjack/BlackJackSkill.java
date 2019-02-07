package app.skill.impl.game.blackjack;

import app.handler.IHandlerInput;
import app.skill.DefaultSkillImpl;
import app.skill.ISkill;
import app.skill.impl.game.hangman.HangmanSkill;

import java.util.*;

public class BlackJackSkill extends DefaultSkillImpl {

    static class Game{
        String[] cards;
        boolean[] selected;
        public Set<Integer> value(){
            Set<Integer> vsA = new HashSet<>();
            Set<Integer> vsB = new HashSet<>();
            for (int i = 0; i < cards.length; i++) {
                if(selected[i]){
                    // faces
                    if(cards[i].charAt(1) == 'Q' || cards[i].charAt(1) == 'K' || cards[i].charAt(1) == 'J'){
                        if(vsA.isEmpty()){
                            vsA.add(10);
                        }else {
                            vsB.clear();
                            for (Integer v : vsA) {
                                vsB.add(v + 10);
                            }
                            vsA.clear();
                            vsA.addAll(vsB);
                        }
                    }
                    // ace
                    else if(cards[i].charAt(1) == 'A'){
                        if(vsA.isEmpty()){
                            vsA.add(1);
                            vsA.add(11);
                        }else{
                            vsB.clear();
                            for (Integer v : vsA) {
                                vsB.add(v + 1);
                                vsB.add(v + 11);
                            }
                            vsA.clear();
                            vsA.addAll(vsB);
                        }
                    }
                    // 2 - 10
                    else{
                        int val = Integer.parseInt(cards[i].substring(1));
                        if(vsA.isEmpty()) {
                            vsA.add(val);
                        }else {
                            vsB.clear();
                            for (Integer v : vsA) {
                                vsB.add(v + val);
                            }
                            vsA.clear();
                            vsA.addAll(vsB);
                        }
                    }
                }
            }
            vsB.clear();
            for(Integer v : vsA){
                if(v <= 21 ){
                    vsB.add(v);
                }
            }
            return vsB;
        }
    }

    private static Random RANDOM = new Random(System.currentTimeMillis());
    private static Map<String, Game> GAMES_IN_PROGRESS = new HashMap<>();

    public BlackJackSkill(){
        addRequestHandler(new StartGameRequestHandler());
        addRequestHandler(new HitMeRequestHandler());
    }

    public static void startGame(String userID) {
        // setup hangman
        BlackJackSkill.Game g = new BlackJackSkill.Game();
        g.cards = new String[]{"♠A","♠K","♠Q","♠J","♠10","♠9","♠8","♠7","♠6","♠5","♠4","♠3","♠2",
                                "♥A","♥K","♥Q","♥J","♥10","♥9","♥8","♥7","♥6","♥5","♥4","♥3","♥2",
                                "♦A","♦K","♦Q","♦J","♦10","♦9","♦8","♦7","♦6","♦5","♦4","♦3","♦2",
                                "♣A","♣K","♣Q","♣J","♣10","♣9","♣8","♣7","♣6","♣5","♣4","♣3","♣2"
                                };
        g.selected = new boolean[52];
        GAMES_IN_PROGRESS.put(userID, g);
        drawCard(userID);
        drawCard(userID);
    }

    public static Game getGame(String userID){
        return GAMES_IN_PROGRESS.get(userID);
    }

    public static void drawCard(String userID){
        Game g = GAMES_IN_PROGRESS.get(userID);
        if(g == null)
            return;
        while(true){
            int j = RANDOM.nextInt(52);
            if(g.selected[j])
                continue;
            g.selected[j] = true;
            break;
        }
    }

    public static void stopGame(String userID) {
        GAMES_IN_PROGRESS.remove(userID);
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        String userID = input.getUserID();

        boolean ch = super.canHandle(input);
        if (!ch && GAMES_IN_PROGRESS.containsKey(userID))
            GAMES_IN_PROGRESS.remove(userID);

        return ch;
    }
}
