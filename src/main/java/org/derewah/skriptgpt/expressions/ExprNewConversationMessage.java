package org.derewah.skriptgpt.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skriptgpt.types.ConversationMessage;


public class ExprNewConversationMessage extends SimpleExpression<ConversationMessage> {

    static {
        Skript.registerExpression(ExprNewConversationMessage.class, ConversationMessage.class, ExpressionType.SIMPLE, "[a] new conversation message [with role user]",
                "[a] new conversation message with role assistant",
                "[a] new conversation message with role system");
    }

    private String role;

    @Override
    protected ConversationMessage[] get(Event e){
        ConversationMessage cv = new ConversationMessage();
        cv.role = role;
        cv.content = null;
        return new ConversationMessage[] { cv };
    }

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0){
            role = "user";
        }else if (matchedPattern == 1){
            role = "assistant";
        }else if (matchedPattern == 2){
            role = "system";
        }
        return true;
    }

    @Override
    public boolean isSingle(){
        return true;
    }

    @Override
    public Class<? extends ConversationMessage> getReturnType(){
        return ConversationMessage.class;
    }


    @Override
    public String toString(Event e, boolean debug){
        return "new conversation message";
    }

}
