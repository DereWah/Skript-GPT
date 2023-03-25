package org.derewah.skriptgpt.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;


public class ExprGeneratedText extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGeneratedText.class, String.class, ExpressionType.SIMPLE, "[the] [last] generated prompt");
    }

    public static String content;

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResut) {
        return true;
    }

    @Override
    protected String[] get(Event e){
        return new String[] { content };
    }

    @Override

    public boolean isSingle(){
        return true;
    }

    public Class<? extends String> getReturnType(){
        return String.class;
    }

    public String toString(Event e, boolean debug){
        return "Received generated text";
    }

}
