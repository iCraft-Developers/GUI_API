package icraft.gui.Chest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;


public class Menu implements InventoryHolder, Listener {
    private Inventory inv;
    private static HashMap<String, Menu> menus = new HashMap<>();
    private HashMap<Integer, Option> functions = new HashMap<>();


    public Menu(String name, String title, int rows, HashMap<Integer, Option> options, Plugin plugin) {
        this(name, title, rows, plugin);
        for(Integer slot : options.keySet()){
            addOption(slot, options.get(slot));
        }
    }



    public Menu(String name, String title, int rows, Plugin plugin, Option... options) throws Exception {
        this(name, title, rows, plugin);
        addOption(options);
    }




    public Menu(String name, String title, int rows, Plugin plugin) {
        inv = Bukkit.createInventory(this, rows * 9, title);
        menus.put(name, this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }




    public Inventory getInventory() {
        return inv;
    }




    // You can call this whenever you want to put the items in
    public void addOption(int slot, Option option) {
        //"Example Sword", "works", "§aFirst line of the lore", "§bSecond line of the lore"
        inv.setItem(slot, option.getItemStack());
        if(option.getFunctions() != null) {
            functions.put(slot, option);
        }
        //inv.addItem(new ItemStack(Material.IRON_HELMET,1, "§bExample Helmet", "§aFirst line of the lore", "§bSecond line of the lore"));
    }



    public void addOption(@Nonnull Option...options) throws Exception {
        int slot = 0;
        for(Option option : options) {
            if(slot < inv.getSize()) {
                while (slot < inv.getSize() && inv.getItem(slot) != null) {
                    slot++;
                }
                if (inv.getItem(slot) == null) {
                    inv.setItem(slot, option.getItemStack());
                    if(option.getFunctions() != null) {
                        functions.put(slot, option);
                    };
                }
                slot++;
            } else {
                throw new Exception("Not enough space in inventory to add new option!");
            }
        }
    }



    // You can open the inventory with this
    public void show(Player p) {
        p.openInventory(inv);
    }


    public static Menu get(String name) {
        return menus.get(name);
    }

    // Check for clicks on items
    @EventHandler
    public void GUIOptionClick(InventoryClickEvent e) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (e.getInventory().getHolder() != this) {
            return;
        }



        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        int clickedSlot = e.getRawSlot();

        if (clickedItem == null) return;

        ClickType clickType = e.getClick();

        if(functions.containsKey(clickedSlot)) {
            Option option = functions.get(clickedSlot);

            HashMap<Option.ClickType, String> fs = option.getFunctions();

            if (fs != null) {
                for (Option.ClickType ct : fs.keySet()) {
                    if (clickType.name().equals(ct.name())) {
                        option.runFunction(ct, p);
                        break;
                    }
                }
            }
        }

        e.setCancelled(true);
        /*
        try {
            clickedItem.runFunction(p);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
         */
    }



}
