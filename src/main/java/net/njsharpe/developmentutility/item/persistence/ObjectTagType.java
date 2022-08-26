package net.njsharpe.developmentutility.item.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class ObjectTagType implements PersistentDataType<byte[], Serializable> {

    @Override
    @NotNull
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    @NotNull
    public Class<Serializable> getComplexType() {
        return Serializable.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Serializable complex, @NotNull PersistentDataAdapterContext context) {
        try(ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(stream)) {
            output.writeObject(complex);
            return stream.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("could not serialize object", ex);
        }
    }

    @Override
    @NotNull
    public Serializable fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        try(ByteArrayInputStream stream = new ByteArrayInputStream(primitive);
            ObjectInputStream input = new ObjectInputStream(stream)) {
            return (Serializable) input.readObject();
        } catch (Exception ex) {
            throw new RuntimeException("could not deserialize byte array", ex);
        }
    }

}
