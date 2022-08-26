package net.njsharpe.developmentutility.helper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class StringHelper {

    /***
     * Transforms the input {@link String} by removing the first and last characters
     *
     * @param value Nullable {@link String}
     * @return The transformed {@link String}, or {@code null} if the input is null
     */
    @Contract("!null -> !null; null -> null")
    @Nullable
    public static String trimEnds(@Nullable String value) {
        if(value == null) return null;
        return value.substring(1).substring(0, value.length() - 2);
    }

}
