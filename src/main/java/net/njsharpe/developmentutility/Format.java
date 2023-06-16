package net.njsharpe.developmentutility;

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
        return Arrays.stream(value.split(regex)).collect(Collectors.toList());
    }

    /**
     * Transforms the input {@link String} to capitalize on the delimiters {@code `}, {@code '}, {@code -},
     * {@code /}, {@code _} and {@code \s} whitespace characters
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
