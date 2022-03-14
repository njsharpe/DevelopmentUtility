package net.njsharpe.developmentutility;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Format {

    /**
     * Transforms the input {@link String} to use {@code &} for color code translation
     *
     * @param value Nullable {@link String}
     * @return The transformed {@link String}, or {@code null} if the input is null
     */
    @Contract("!null -> !null; null -> null")
    @Nullable
    public static String colorize(@Nullable String value) {
        if(value == null) return null;
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    /**
     * Transforms and formats the input {@link String} to use {@code &} for color code translation
     *
     * @param value Nullable {@link String}
     * @param args A set of {@link Object} to format the input {@link String}
     * @return The transformed and formatted {@link String}, or {@code null} if {@code value} is null
     */
    @Contract("!null,_ -> !null; null,_ -> null")
    @Nullable
    public static String colorize(@Nullable String value, Object... args) {
        if(value == null) return null;
        return colorize(String.format(value, args));
    }

    /**
     * Transforms the input {@link List} of {@link String} to use {@code &} for color code translation
     *
     * @param values Nullable {@link List} of {@link String}
     * @return The transformed {@link List} of {@link String}, or {@code null} if the input is null
     */
    @Contract("!null -> !null; null -> null")
    @Nullable
    public static List<String> colorize(@Nullable List<String> values) {
        if(values == null) return null;
        return values.stream().map(Format::colorize).collect(Collectors.toList());
    }

    /**
     * Transforms the input {@link String} to remove all instances of {@code U+00A7}
     *
     * @param value Nullable {@link String}
     * @return The transformed {@link String}, or {@code null} if the input is null
     */
    @Contract("!null -> !null; null -> null")
    @Nullable
    public static String strip(@Nullable String value) {
        if(value == null) return null;
        return ChatColor.stripColor(value);
    }

    /**
     * Transforms the input {@link List} of {@link String} to remove all instances of {@code U+00A7}
     *
     * @param values Nullable {@link List} of {@link String}
     * @return The transformed {@link List} of {@link String}, or {@code null} if the input is null
     */
    @Contract("!null -> !null; null -> null")
    @Nullable
    public static List<String> strip(@Nullable List<String> values) {
        if(values == null) return null;
        return values.stream().map(Format::strip).collect(Collectors.toList());
    }

    /**
     * Transforms the input {@link String} to replace all instanceof of {@code U+00A7} with {@code &}
     *
     * @param value Nullable {@link String}
     * @return The transformed {@link String}, or {@code null} if the input is null
     */
    @Contract("!null -> !null; null -> null")
    @Nullable
    public static String raw(@Nullable String value) {
        if(value == null) return null;
        return value.replaceAll("\u00A7", "&");
    }

    /**
     * Creates a {@link List} of {@link String}, splitting on the {@code regex} specified
     *
     * @param regex Regex pattern to split on
     * @param value Nullable {@link String}
     * @return The final {@link List} of {@link String}, or {@code null} if {@code value} is null
     */
    @Contract("_,!null -> !null; _,null -> null")
    @Nullable
    public static List<String> toList(@NotNull String regex, @Nullable String value) {
        if(value == null) return null;
        return Arrays.asList(value.split(regex));
    }

    /**
     * Transforms the input {@link String} to capitalize on the delimiters {@code `}, {@code '}, {@code -},
     * {@code /}, {@code _} and {@code \s} whitespace characters.
     *
     * @param value Nullable {@link String}
     * @return The transformed {@link String}, or {@code null} if the input is null
     */
    @Contract("!null -> !null; null -> null")
    @Nullable
    public static String toTitleCase(@Nullable String value) {
        if(value == null) return null;
        final Pattern pattern = Pattern.compile("[`'\\-/_\\s]");
        StringBuilder builder = new StringBuilder();
        boolean capitalize = true;
        for(char c : value.toCharArray()) {
            Matcher matcher = pattern.matcher(String.valueOf(c));
            c = (capitalize) ? Character.toUpperCase(c) : Character.toLowerCase(c);
            builder.append(c);
            capitalize = matcher.find();
        }
        return builder.toString();
    }

}
