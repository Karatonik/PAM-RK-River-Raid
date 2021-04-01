package pl.r.river_raid_mobile.enums;

public enum RenderedObjectEnum {
    helicopterL("helicopterL"),helicopterP("helicopterP"),boat("boat")
    ,shipP("shipP"),shipL("shipL"),fuel("fuel"),exp("exp"),
    bridge("bridge");


    private final String name;


    RenderedObjectEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
