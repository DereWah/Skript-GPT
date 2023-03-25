package org.derewah.skriptgpt.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.derewah.skriptgpt.SkriptGPT;
import org.derewah.skriptgpt.expressions.ExprGeneratedText;
import org.derewah.skriptgpt.util.HttpRequest;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ch.njol.skript.Skript.registerEffect;


@Name("ChatGPT Chat Completion Request")
@Description({"Generate a response to a prompt string using the OpenAI ChatGPT API, based on an input prompt."})
@Examples({
        "command /swordgpt:",
        "\ttrigger:",
        "\t\tgenerate a chatgpt completion with prompt \"Give me the name of an epic sword. In your response only include the name, and color it with minecraft colors. Do not include anything else. JUST THE NAME. JUST THE NAME IN THE RESPONSE. Example: &dAncient &5Sword or &eSword of &cFire\"",
        "\t\tset {_name} to generated prompt",
        "\t\treplace all \"%nl%%nl%\" in {_name} with \"\" #ChatGPT sometime add a double newline at the beginning of the completion.",
        "\t\tadd 1 diamond sword named {_name} to player's inventory"
})

@Since("1.0")



public class EffChatCompletionRequest extends Effect {

    static  {
        registerEffect(EffChatCompletionRequest.class,
                "(generate|make) [a] chat[gpt] completion with (prompt|input) %string% [and model %-string%] [and max tokens %-number%] [and temperature %-number%]"
        );
    }

    private Expression<String> prompt;
    private Expression<String> model;
    private Expression<Number> temperature;
    private Expression<Number> max_tokens;
    private String token;

    private static final Field DELAYED;

    static {
        Field _DELAYED = null;
        try {
            _DELAYED = Delay.class.getDeclaredField("delayed");
            _DELAYED.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Skript.warning("Skript's 'delayed' method could not be resolved. Some Skript warnings may " +
                    "not be available.");
        }
        DELAYED = _DELAYED;
    }






    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        token = SkriptGPT.config.getString("openai_token");
        prompt = (Expression<String>) expr[0];
        model = (Expression<String>) expr[1];
        max_tokens = (Expression<Number>) expr[2];
        temperature = (Expression<Number>) expr[3];

        return true;
    }



    private static final ExecutorService threadPool =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    protected void execute(Event e){
        String text = prompt.getSingle(e);
        String s_model = model != null ? model.getSingle(e) : "gpt-3.5-turbo";
        Number i_temperature = temperature != null ? temperature.getSingle(e) : 1;
        Number i_max_tokens = max_tokens != null ? max_tokens.getSingle(e) : 160;
        if (i_temperature.intValue() < 0 && i_temperature.intValue() > 2) {
            i_temperature = 1;
        }

        Number finalI_temperature = i_temperature;
        CompletableFuture.supplyAsync(() -> {
            try {
                return HttpRequest.main(true, false, token ,text, i_max_tokens.intValue(), s_model, finalI_temperature);
            } catch (Exception ex) {
                if (ex.getMessage().equals("401")){
                    Skript.warning("Authentication error. Provide a valid API token in config.yml");
                } else if (ex.getMessage().equals("429")) {
                    Skript.warning("Request error: you might have exceeded your current quota, or you might be rate limited.");
                }
                throw new RuntimeException(ex);
            }
        }, threadPool)
                .whenComplete((resp, err) -> {

                    if (err != null) {
                        err.printStackTrace();
                        ExprGeneratedText.content = null;
                        return;
                    }

                    Bukkit.getScheduler().runTask(SkriptGPT.getInstance(), () -> {
                        ExprGeneratedText.content = resp;
                        if (getNext() != null){
                            TriggerItem.walk(getNext(), e);
                        }
                    });
                });
    }


    @Override
    protected TriggerItem walk(Event e) {
        debug(e, true);
        delay(e);
        execute(e);
        return null;
    }


    @SuppressWarnings("unchecked")
    private void delay(Event e) {
        if (DELAYED != null) {
            try {
                ((Set<Event>) DELAYED.get(null)).add(e);
            } catch (IllegalAccessException ignored) {
            }
        }
    }


    public String toString(Event e, boolean debug) {
        return "generate chatgpt completion with prompt " + prompt.toString(e, debug)
                + (model != null ? " and model " + model.toString(e, debug) : "")
                + (max_tokens != null ? " and max tokens " + max_tokens.toString(e, debug) : "")
                + (temperature != null ? " and temperature " + temperature.toString(e, debug) : "");
    }

}
