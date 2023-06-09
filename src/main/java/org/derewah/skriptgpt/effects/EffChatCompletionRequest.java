package org.derewah.skriptgpt.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;

import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;

import org.bukkit.event.Event;

import org.derewah.skriptgpt.expressions.ExprGeneratedText;
import org.derewah.skriptgpt.types.ConversationMessage;
import org.derewah.skriptgpt.util.HttpRequest;


import static ch.njol.skript.Skript.registerEffect;


@Name("ChatGPT Chat Completion Request")
@Description({"Generate a response to a prompt string using the OpenAI ChatGPT API, based on an input prompt."})
@Examples({
        "command /swordgpt:",
        "\ttrigger:",
        "\t\tgenerate a chatgpt completion with prompt \"Give me the name of an epic sword. In your response only include the name, and color it with minecraft colors. Do not include anything else. JUST THE NAME. JUST THE NAME IN THE RESPONSE. Example: &dAncient &5Sword or &eSword of &cFire\"",
        "\t\tset {_name} to generated prompt",
        "\t\treplace all \"%nl%%nl%\" in {_name} with \"\" #ChatGPT sometime add a double newline at the beginning of the completion.",
        "\t\tadd 1 diamond sword named {_name} to player's inventory",
        "--------------------------",
        "command /completion [<text>]:\n" +
                "\ttrigger:\n" +
                "\t\tif arg is set:\n" +
                "\t\t\tsend message \"&7DereWah:&f %arg%\"\n" +
                "\t\t\tset {_c} to arg parsed as conversation message\n" +
                "\t\t\tadd {_c} to {gpt::%player%::*}\n" +
                "\t\t\tgenerate a chat completion with conversation {gpt::%player%::*}\n" +
                "\t\t\tset {_p} to last generated prompt\n" +
                "\t\t\tadd {_p} to {gpt::%player%::*}\n" +
                "\t\t\tsend \"&7ChatGPT:&f %{_p}%\"\n" +
                "\t\telse:\n" +
                "\t\t\tsend message \"&cYou cleared your previous conversation.\"\n" +
                "\t\t\tclear {gpt::%player%::*}"
})

@Since("1.0")



public class EffChatCompletionRequest extends AsyncEffect {

    static  {
        registerEffect(EffChatCompletionRequest.class,
                "(generate|make) [a] chat[gpt] completion with (prompt|input) %string% [and model %-string%] [and max tokens %-number%] [and temperature %-number%]",
                "(generate|make) [a] chat[gpt] completion with conversation %conversationmessages% [and model %-string%] [and max tokens %-number%] [and temperature %-number%]"
        );
    }

    private Expression<String> prompt;

    private Expression<ConversationMessage> prompts;
    private Expression<String> model;
    private Expression<Number> temperature;
    private Expression<Number> max_tokens;


    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        getParser().setHasDelayBefore(Kleenean.TRUE);
        if (matchedPattern == 0) {
            prompt = (Expression<String>) expr[0];
        }else{
            prompts = (Expression<ConversationMessage>) expr[0];
        }
        model = (Expression<String>) expr[1];
        max_tokens = (Expression<Number>) expr[2];
        temperature = (Expression<Number>) expr[3];
        return true;
    }

    @Override
    protected void execute(Event e){
        ConversationMessage[] convs;
        if (prompt != null){
            convs = new ConversationMessage[1];
            ConversationMessage conv = new ConversationMessage();
            conv.role = "user";
            conv.content = prompt.getSingle(e);
            convs[0] = conv;
        }else{
            convs = prompts.getArray(e);
        }
        String s_model = model != null ? model.getSingle(e) : "gpt-3.5-turbo";
        Number i_temperature = temperature != null ? temperature.getSingle(e) : 1;
        Number i_max_tokens = max_tokens != null ? max_tokens.getSingle(e) : 160;
        if (i_temperature.intValue() < 0 && i_temperature.intValue() > 2) {
            i_temperature = 1;
        }

        Number finalI_temperature = i_temperature;

            try {
                ExprGeneratedText.conv.content =  HttpRequest.main(true, false, convs, i_max_tokens.intValue(), s_model, finalI_temperature);
            } catch (Exception ex) {
                if (ex.getMessage().equals("401")){
                    Skript.warning("Authentication error. Provide a valid API token in config.yml");
                } else if (ex.getMessage().equals("429")) {
                    Skript.warning("Request error: you might have exceeded your current quota, or you might be rate limited.");
                }
                throw new RuntimeException(ex);
            }
    }



    public String toString(Event e, boolean debug) {
        return "generate chatgpt completion with prompt " + (prompt!= null ? prompt.toString(e, debug):prompts.getSingle(e))
                + (model != null ? " and model " + model.toString(e, debug) : "")
                + (max_tokens != null ? " and max tokens " + max_tokens.toString(e, debug) : "")
                + (temperature != null ? " and temperature " + temperature.toString(e, debug) : "");
    }

}
