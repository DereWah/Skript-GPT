package org.derewah.skriptgpt.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;

import ch.njol.util.Kleenean;

import org.bukkit.event.Event;

import org.derewah.skriptgpt.expressions.ExprGeneratedText;

import org.derewah.skriptgpt.util.HttpRequest;


import static ch.njol.skript.Skript.registerEffect;


@Name("ChatGPT Completion Request")
@Description({"Generate a completion of an input string using the OpenAI ChatGPT API, based on an input prompt."})
@Examples({
        "command /completion:",
        "\ttrigger:",
        "\t\tgenerate a chatgpt completion with prompt \"On July 9, 2006, Italy\"",
        "\t\tset {_completion} to generated prompt",
        "\t\treplace all \"%nl%%nl%\" in {_name} with \"\" #ChatGPT sometime add a double newline at the beginning of the completion.",
        "\t\tsend message {_completion}"
})

@Since("1.0")



public class EffCompletionRequest extends Effect {

    static  {
        registerEffect(EffCompletionRequest.class,
                "(generate|make) [a] [gpt] completion with (prompt|input) %string% [and model %-string%] [and max tokens %-number%] [and temperature %-number%]",
                "(generate|make) [a] [gpt] completion with (prompt|input) %string% [and model %-string%] [and max tokens %-number%] [and temperature %-number%] without echo"
        );
    }

    private Expression<String> prompt;
    private Expression<String> model;
    private Expression<Number> temperature;
    private Expression<Number> max_tokens;

    private Boolean echo;






    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        prompt = (Expression<String>) expr[0];
        model = (Expression<String>) expr[1];
        max_tokens = (Expression<Number>) expr[2];
        temperature = (Expression<Number>) expr[3];
        echo = matchedPattern == 1 ? false : true;
        return true;
    }


    @Override
    protected void execute(Event e){
        String text = prompt.getSingle(e);
        String s_model = model != null ? model.getSingle(e) : "text-davinci-003";
        Number i_temperature = temperature != null ? temperature.getSingle(e) : 1;
        Number i_max_tokens = max_tokens != null ? max_tokens.getSingle(e) : 160;

        if (i_temperature.intValue() < 0 && i_temperature.intValue() > 2) {
            i_temperature = 1;
        }

        Number finalI_temperature = i_temperature;
            try {
                ExprGeneratedText.conv.content =  HttpRequest.main(false, echo , text, i_max_tokens.intValue(), s_model, finalI_temperature);
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
        return "generate gpt completion with prompt " + prompt.toString(e, debug)
                + (model != null ? " and model " + model.toString(e, debug) : "")
                + (max_tokens != null ? " and max tokens " + max_tokens.toString(e, debug) : "")
                + (temperature != null ? " and temperature " + temperature.toString(e, debug) : "")
                + (echo == true ? "with echo" : "without echo");
    }

}
