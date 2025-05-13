package irisia.interfaces;

public interface Toggleable {

    void setToggled(final boolean toggled);

    boolean isToggled();

    void onEnable();

    void onDisable();

    void toggle();

}
