package app.skill.impl.game.blackjack;

import app.handler.IHandlerInput;
import app.skill.DefaultSkillImpl;

import java.util.*;

public class BlackJackSkill extends DefaultSkillImpl {

    static class Game{
        String[] cards;
        int[] selected;
        public Set<Integer> valueForPlayer(){ return value(1); }
        public Set<Integer> valueForBot(){ return value(-1); }
        private Set<Integer> value(int player){
            Set<Integer> vsA = new HashSet<>();
            Set<Integer> vsB = new HashSet<>();
            for (int i = 0; i < cards.length; i++) {
                if(selected[i] == 1){
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
        addRequestHandler(new StandRequestHandler());
    }

    public static void startGame(String userID) {
        // setup hangman
        BlackJackSkill.Game g = new BlackJackSkill.Game();
        g.cards = new String[]{"♠A","♠K","♠Q","♠J","♠10","♠9","♠8","♠7","♠6","♠5","♠4","♠3","♠2",
                                "♥A","♥K","♥Q","♥J","♥10","♥9","♥8","♥7","♥6","♥5","♥4","♥3","♥2",
                                "♦A","♦K","♦Q","♦J","♦10","♦9","♦8","♦7","♦6","♦5","♦4","♦3","♦2",
                                "♣A","♣K","♣Q","♣J","♣10","♣9","♣8","♣7","♣6","♣5","♣4","♣3","♣2"
                                };
        g.selected = new int[52];
        GAMES_IN_PROGRESS.put(userID, g);
        // draw cards for player
        drawCardForPlayer(userID);
        drawCardForPlayer(userID);
        // draw cards for bot
        drawCardForBot(userID);
        drawCardForBot(userID);
    }

    public static Game getGame(String userID){
        return GAMES_IN_PROGRESS.get(userID);
    }

    public static void drawCardForPlayer(String userID){
        Game g = GAMES_IN_PROGRESS.get(userID);
        if(g == null)
            return;
        while(true){
            int j = RANDOM.nextInt(52);
            if(g.selected[j] != 0)
                continue;
            g.selected[j] = 1;
            break;
        }
    }

    public static void drawCardForBot(String userID){
        Game g = GAMES_IN_PROGRESS.get(userID);
        if(g == null)
            return;
        while(true){
            int j = RANDOM.nextInt(52);
            if(g.selected[j] != 0)
                continue;
            g.selected[j] = -1;
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
