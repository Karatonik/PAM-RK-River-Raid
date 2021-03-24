package pl.r.river_raid_mobile.enums;

public enum RenderedObjectEnum {
    helicopter("helicopter"),boat("boat"),bridge("bridge"),fuel("fuel"),exp("exp");


    private final String name;


    RenderedObjectEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
