package app.skill.impl.internet.cocktail;

import app.skill.DefaultSkillImpl;

public class CocktailSkill extends DefaultSkillImpl {

    public CocktailSkill(){
        addRequestHandler(new CocktailRequestHandler());
    }
}
