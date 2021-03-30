package pl.r.river_raid_mobile.enums;

public enum RenderedObjectEnum {
    helicopterL("helicopterL"),helicopterP("helicopterP"),boat("boat"),fuel("fuel"),exp("exp");


    private final String name;


    RenderedObjectEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
