package org.derewah.skriptgpt.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import ch.njol.skript.classes.Changer.ChangeMode;
import org.derewah.skriptgpt.types.ConversationMessage;
import org.jetbrains.annotations.Nullable;

public class ExprRoleConversationMessage extends SimplePropertyExpression<ConversationMessage, String> {

    static {
        register(ExprRoleConversationMessage.class, String.class, "gpt role", "conversationmessage");
    }

    @Override
    public Class<? extends String> getReturnType(){
        return String.class;
    }

    @Override
    @Nullable
    public String convert(ConversationMessage conv){
        return conv.role;
    }

    @Override
    protected String getPropertyName() {
        return "role";
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode){
        if (mode == ChangeMode.SET) { return CollectionUtils.array(String.class);}
        return null;
    }


    public void change(Event e, Object[] delta, Changer.ChangeMode mode){
        if (delta != null){
            ConversationMessage conv = getExpr().getSingle(e);
            if (mode == ChangeMode.SET){

                conv.role = (String) delta[0];
            } else{
                Skript.error("A role of a conversation message can only be set.");
            }
        }
    }


}
