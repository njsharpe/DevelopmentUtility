package net.njsharpe.developmentutility.helper;

import net.njsharpe.developmentutility.enchantment.DummyEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class Enchantments {

    public static DummyEnchantment DUMMY = Enchantments.register(new DummyEnchantment());
    public static <T extends Enchantment> T register(T t) {
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);

            if(Enchantment.getByKey(t.getKey()) == null) Enchantment.registerEnchantment(t);
            return t;
        } catch (Exception ex) {
            throw new RuntimeException("Could not register enchantment", ex);
        }
    }

    public static <T extends Enchantment> T register(Supplier<T> supplier) {
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);

            T t = supplier.get();
            if(Enchantment.getByKey(t.getKey()) == null) Enchantment.registerEnchantment(t);
            return t;
        } catch (Exception ex) {
            throw new RuntimeException("Could not register enchantment", ex);
        }
    }

}
