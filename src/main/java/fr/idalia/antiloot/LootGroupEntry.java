package fr.idalia.antiloot;

public final class LootGroupEntry {

    private final String group;
    private final LootGroupData data;

    public LootGroupEntry(String group, LootGroupData data) {
        this.group = group;
        this.data = data;
    }

    public String group() {
        return this.group;
    }

    public LootGroupData data() {
        return this.data;
    }
}
