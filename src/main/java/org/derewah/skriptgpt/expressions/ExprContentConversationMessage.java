package org.derewah.skriptgpt.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.derewah.skriptgpt.types.ConversationMessage;
import org.jetbrains.annotations.Nullable;

public class ExprContentConversationMessage extends SimplePropertyExpression<ConversationMessage, String> {

    static {
        register(ExprContentConversationMessage.class, String.class, "gpt content", "conversationmessage");
    }

    @Override
    public Class<? extends String> getReturnType(){
        return String.class;
    }

    @Override
    @Nullable
    public String convert(ConversationMessage conv){
        return conv.content;
    }

    @Override
    protected String getPropertyName() {
        return "content";
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode){
        if (mode == ChangeMode.SET) { return CollectionUtils.array(String.class);}
        return null;
    }


    public void change(Event e, Object[] delta, ChangeMode mode){
        if (delta != null){
            ConversationMessage conv = getExpr().getSingle(e);
            if (mode == ChangeMode.SET){
                conv.content = (String) delta[0];
            } else{
                Skript.error("A content of a conversation message can only be set.");
            }
        }
    }


}
