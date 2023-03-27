package org.derewah.skriptgpt.types;

public class ConversationMessage {

    public String role;
    public String content;



    public String toString(){
        return "{\"role\": \""+role+"\", \"content\": \""+content+"\"}";
    }


}
