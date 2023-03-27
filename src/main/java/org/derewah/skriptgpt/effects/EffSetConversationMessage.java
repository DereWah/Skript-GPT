package org.derewah.skriptgpt.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

import static ch.njol.skript.Skript.registerEffect;

public class EffSetConversationMessage extends Effect {


    static  {
        registerEffect(EffCompletionRequest.class,
                "set [the] role of [the] conversation message %conversation message% to (assistant|system|user)",
                "set [the] content of [the] conversation message %conversation message% to %string%"
        );
    }

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult){

    }

}
