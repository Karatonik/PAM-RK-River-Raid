package pl.r.river_raid_mobile.enums;

public enum EnumDir {
    LEFT("LEFT"),RIGHT("RIGHT"),NORMAL("NORMAL");


    private final String name;


    EnumDir(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
