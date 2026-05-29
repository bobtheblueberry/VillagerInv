package com.bobtheblueberry.villager_inv.service;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class serves as a centralized messaging / text service that supports i18n.
 *
 * @author Whitescan
 * @since 1.0.0
 */
public final class MessageService {

    private final @NotNull MiniMessage miniMessage;

    @Getter
    @Setter
    private @NotNull Locale locale;

    private @NotNull ResourceBundle bundle;

    public MessageService(@NotNull Locale locale) {
        this.miniMessage = MiniMessage.miniMessage();
        this.locale = locale;
        reload();
    }

    public void reload() {
        this.bundle = ResourceBundle.getBundle("messages", getLocale());
    }

    public @NotNull Component get(@NotNull String key, @NotNull TagResolver... resolvers) {
        return miniMessage.deserialize(getRaw(key), TagResolver.resolver(resolvers));
    }

    public @NotNull Component get(@NotNull String key) {
        return get(key, TagResolver.empty());
    }

    public @NotNull String getRaw(@NotNull String key) {
        return bundle.containsKey(key) ? bundle.getString(key) : "Missing translation: " + key;
    }

    public @NotNull List<Component> getList(@NotNull String key, @NotNull TagResolver... resolvers) {

        final String raw = getRaw(key);

        final List<Component> components = new ArrayList<>();
        for (String line : raw.split("<newline>"))
            components.add(miniMessage.deserialize(line, resolvers));

        return components;

    }

    public @NotNull List<Component> getList(@NotNull String key) {
        return getList(key, TagResolver.empty());
    }

}
