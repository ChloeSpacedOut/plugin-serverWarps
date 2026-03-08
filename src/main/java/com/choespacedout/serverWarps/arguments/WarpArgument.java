package com.choespacedout.serverWarps.arguments;

import com.choespacedout.serverWarps.WarpCache;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class WarpArgument implements CustomArgumentType<String, String> {

    static WarpCache warpCache;
    public WarpArgument(WarpCache newWarpCache) {
        warpCache = newWarpCache;
    }

    private static final DynamicCommandExceptionType ERROR_NO_WARP = new DynamicCommandExceptionType(name -> {
        return MessageComponentSerializer.message().serialize(Component.text("Valid Warps: " + warpCache.cacheToList()));
    });

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        final Set<String> warps = warpCache.getCache();
        final String warpName = getNativeType().parse(reader).toString();

        if (!warps.contains(warpName)) {
            throw ERROR_NO_WARP.create(warpName);
        }

        return warpName;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> ctx, final SuggestionsBuilder builder) {
        final Set<String> warps;

        warps = warpCache.getCache();

        for (int i = 0; i < warps.size(); i++) {
            final String warpName = warps.stream().toList().get(i);

            if (warpName.toLowerCase().startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(warpName);
            }
        }

        return builder.buildFuture();
    }
}