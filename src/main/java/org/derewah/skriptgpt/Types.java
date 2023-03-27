package org.derewah.skriptgpt;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.derewah.skriptgpt.types.ConversationMessage;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(ConversationMessage.class, "conversation message")
                .defaultExpression(new EventValueExpression<>(ConversationMessage.class))
                .user("conversation message")
                .name("Conversation Message")
                .description("Represents one message of a conversation. Holds a role and a content value.")
                .parser(new Parser<ConversationMessage>(){

                    @Override
                    public ConversationMessage parse(String id, ParseContext arg1){
                        ConversationMessage newmess = new ConversationMessage();
                        newmess.role = "user";
                        newmess.content = id;
                        return newmess;
                    }

                    @Override
                    public String toString(ConversationMessage conv, int arg1){
                        return conv.toString();
                    }


                    @Override
                    public String toVariableNameString(ConversationMessage conv){
                        return conv.toString();
                    }

                    @Override
                    public String getVariableNamePattern(){
                        return "r";
                    }


                })
        );
    }
}
