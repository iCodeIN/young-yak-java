package app.skill.impl.game.blackjack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Game{

    private static Random RANDOM = new Random(System.currentTimeMillis());

    String[] cards;
    int[] selected;

    public Set<Integer> valueForPlayer(){ return value(1); }
    public Set<Integer> valueForBot(){ return value(-1); }

    private Set<Integer> value(int player){
        Set<Integer> vsA = new HashSet<>();
        Set<Integer> vsB = new HashSet<>();
        for (int i = 0; i < cards.length; i++) {
            if(selected[i] == player){
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

    public Game(){
        cards = new String[]{"♠A","♠K","♠Q","♠J","♠10","♠9","♠8","♠7","♠6","♠5","♠4","♠3","♠2",
                "♥A","♥K","♥Q","♥J","♥10","♥9","♥8","♥7","♥6","♥5","♥4","♥3","♥2",
                "♦A","♦K","♦Q","♦J","♦10","♦9","♦8","♦7","♦6","♦5","♦4","♦3","♦2",
                "♣A","♣K","♣Q","♣J","♣10","♣9","♣8","♣7","♣6","♣5","♣4","♣3","♣2"
        };
        selected = new int[52];

        // draw initial cards
        drawCardForPlayer(1);
        drawCardForPlayer(1);
        drawCardForPlayer(-1);
        drawCardForPlayer(-1);
    }

    public void drawCardForPlayer(int player){
        while(true){
            int j = RANDOM.nextInt(52);
            if(selected[j] != 0)
                continue;
            selected[j] = player;
            break;
        }
    }
}
