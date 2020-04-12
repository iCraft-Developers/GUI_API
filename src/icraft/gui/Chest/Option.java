package icraft.gui.Chest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Option {
    private ItemStack itemStack;
    private HashMap<ClickType, String> functions;

    public enum ClickType {
        CONTROL_DROP,
        DOUBLE_CLICK,
        DROP,
        LEFT,
        MIDDLE,
        NUMBER_KEY,
        RIGHT,
        SHIFT_LEFT,
        SHIFT_RIGHT,
        WINDOW_BORDER_LEFT,
        WINDOW_BORDER_RIGHT;
    }



    public Option(ItemStack itemStack, HashMap<Option.ClickType, String> functions) {
        if(functions != null) {
            this.functions = new HashMap<>();
            this.functions.putAll(functions);
        }
        this.itemStack = itemStack;
    }


    void runFunction(ClickType clickType, Player p) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Functions.class.getDeclaredMethod(functions.get(clickType), Player.class);
        method.invoke(null, p);
        //p.sendMessage(function);
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    public HashMap<ClickType, String> getFunctions(){
        return functions;
    }

    public String getFunction(ClickType clickType){
        return functions.get(clickType);
    }
}
