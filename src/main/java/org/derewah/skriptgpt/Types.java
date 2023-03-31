package org.derewah.skriptgpt;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import org.derewah.skriptgpt.types.ConversationMessage;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(ConversationMessage.class, "conversationmessage")
                        .user("conversation message")
                .defaultExpression(new EventValueExpression<>(ConversationMessage.class))
                .name("conversation message")
                .description("Represents one message of a conversation. Holds a role and a content value.")
                .parser(new Parser<ConversationMessage>(){

//                    @Override
//                    public ConversationMessage parse(final String id, ParseContext arg1){
//                        ConversationMessage newmess = new ConversationMessage();
//                        newmess.role = "user";
//                        newmess.content = id;
//                        return newmess;
//                    }


                    @Override
                    public boolean canParse(ParseContext parseContext){
                        return false;
                    }


                    @Override
                    public String toString(ConversationMessage conv, int arg1){
                        return conv.content;
                    }

                    @Override
                    public String toVariableNameString(ConversationMessage conv){
                        return conv.toString();
                    }



                })
                        .serializer(new Serializer<ConversationMessage>() {
                            @Override
                            public Fields serialize(ConversationMessage conversationMessage) throws NotSerializableException {
                                Fields fields = new Fields();
                                fields.putObject("content", conversationMessage.content != null ? conversationMessage.content : "");
                                fields.putObject("role", conversationMessage.role != null ? conversationMessage.role : "user");
                                return fields;
                            }

                            @Override
                            public void deserialize(ConversationMessage conversationMessage, Fields fields) throws StreamCorruptedException, NotSerializableException {
                                assert false;
                            }

                            @SuppressWarnings("NullableProblems")
                            @Override
                            public ConversationMessage deserialize(Fields fields) throws StreamCorruptedException{
                                String content = fields.getObject("content", String.class);
                                String role = fields.getObject("role", String.class);
                                System.out.println("role: "+role);
                                System.out.println("content: "+content);
                                if ((content != null) && (role != null)){
                                    ConversationMessage conv = new ConversationMessage();
                                    conv.role = role;
                                    conv.content = content;
                                    return conv;
                                }
                                return null;
                            }

                            @Override
                            public boolean mustSyncDeserialization() {
                                return false;
                            }

                            @Override
                            protected boolean canBeInstantiated() {
                                return false;
                            }
                        })
        );
    }
}
