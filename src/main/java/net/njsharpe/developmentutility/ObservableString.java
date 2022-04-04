package net.njsharpe.developmentutility;

public class ObservableString {

    private static final char LINE_CHAR = '\u001D';
    private static final char ENTRY_CHAR = '\u001E';

    private String string;
    private Object[] values;

    public ObservableString(String string, Object... values) {
        this.string = this.transform(string);
        this.values = values;
    }

    private String transform(String string) {
        return String.format("%s%s", LINE_CHAR, string);
    }

    public String getString() {
        return this.string;
    }

    public Object[] getValues() {
        return this.values;
    }

    public void setValues(Object... values) {
        if(values.length != this.values.length)
            throw new IllegalArgumentException("values length mismatch");
        this.values = values;
    }

    public ObservableString setValue(int index, Object value) {
        if(index < 0 || index >= this.values.length)
            throw new RuntimeException(String.format("index out of bounds: %s", index));
        if(value == null)
            throw new IllegalArgumentException("value cannot be null");
        this.values[index] = value;
        return this;
    }

    public ObservableString append(String string, Object... values) {
        if(values.length > 0) {
            Object[] objects = new Object[this.values.length + values.length];
            for(int i = 0; i < objects.length; i++) {
                objects[i] = (i < this.values.length) ? this.values[i] : values[i - this.values.length];
            }
            this.values = objects;
        }
        this.string = String.format("%s%s", this.string, string);
        return this;
    }

    public ObservableString prepend(String string, Object... values) {
        if(values.length > 0) {
            Object[] objects = new Object[this.values.length + values.length];
            for(int i = 0; i < objects.length; i++) {
                objects[i] = (i < values.length) ? values[i] : this.values[i - this.values.length];
            }
            this.values = objects;
        }
        this.string = this.transform(String.format("%s%s", string, this.string.substring(1)));
        return this;
    }

    @Override
    public String toString() {
        String copy = this.string;
        for(Object value : values) {
            copy = copy.replaceFirst("\\{[^}]*}", String.format("%s%s%s", ENTRY_CHAR,
                    value == null ? "null" : value.toString(), ENTRY_CHAR));
        }
        return copy;
    }

    public static boolean isObservableString(String string) {
        return string.startsWith(String.valueOf(LINE_CHAR));
    }

    public static ObservableString from(String string) {
        if(!ObservableString.isObservableString(string)) return null;
        StringBuilder builder = new StringBuilder();
        char[] array = string.substring(1).toCharArray();
        boolean flag = false;
        int index = 0;
        for(char c : array) {
            if(c == ENTRY_CHAR) {
                flag = !flag;
                if(flag) {
                    builder.append("{").append(index).append("}");
                }
                index++;
                continue;
            }
            if(!flag) {
                builder.append(c);
            }
        }
        return new ObservableString(builder.toString(), new Object[index]);
    }

}
