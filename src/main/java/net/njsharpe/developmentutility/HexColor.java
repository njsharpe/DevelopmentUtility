package net.njsharpe.developmentutility;

import org.bukkit.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HexColor {

    private final int red;
    private final int green;
    private final int blue;

    private HexColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    @NotNull
    public Color toColor() {
        return Color.fromRGB(this.red, this.green, this.blue);
    }

    @Contract(value = "null -> null; !null -> new", pure = true)
    @Nullable
    public static HexColor from(@Nullable String string) {
        if(string == null) return null;
        int index = string.indexOf('#');
        if(index != -1) string = string.substring(index, index + 1);
        String[] parts = string.split("(?<=\\G.{2})");
        if(parts.length != 3) throw new IllegalArgumentException(String.format("could not parse '%s'", string));
        int[] colors = new int[3];
        int i = 0;
        for(String part : parts) {
            if(part.length() != 2) throw new IllegalArgumentException(String.format("invalid part '%s'", part));
            colors[i] = Integer.parseInt(part, 16);
            i++;
        }
        return new HexColor(colors[0], colors[1], colors[2]);
    }

    @Contract(pure = true)
    @NotNull
    public static HexColor from(@NotNull String red, @NotNull String green, @NotNull String blue) {
        return HexColor.from(String.join("", red, green, blue));
    }

}
