package net.earthmc.quarters.manager;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import net.earthmc.quarters.object.Cuboid;
import net.earthmc.quarters.object.Selection;
import net.earthmc.quarters.api.QuartersMessaging;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SelectionManager {
    public static Map<Player, Selection> selectionMap = new ConcurrentHashMap<>();
    public static Map<Player, List<Cuboid>> cuboidsMap = new ConcurrentHashMap<>();

    public static void selectPosition(Player player, Location location, boolean isPos1) {
        Selection selection = selectionMap.computeIfAbsent(player, k -> new Selection());

        Town town = TownyAPI.getInstance().getTown(location);
        if (!canSelectPosition(player, town))
            return;

        if (!isPositionInBounds(player, location))
            return;

        if (isPos1)
            selection.setPos1(location);

        if (!isPos1)
            selection.setPos2(location);

        QuartersMessaging.sendComponentWithPrefix(player, getSelectedPositionComponent(isPos1, location));
    }

    private static boolean canSelectPosition(Player player, Town town) {
        Resident resident = TownyAPI.getInstance().getResident(player);
        if (resident == null)
            return false;

        if (town == null || !town.hasResident(player)) {
            QuartersMessaging.sendErrorMessage(player, "Selected area must be part of your town");
            return false;
        }

        return true;
    }

    private static boolean isPositionInBounds(Player player, Location location) {
        if (location.getY() > player.getWorld().getMaxHeight()) {
            QuartersMessaging.sendErrorMessage(player, "Selected position is greater than the world's maximum height");
            return false;
        }

        if (location.getY() < player.getWorld().getMinHeight()) {
            QuartersMessaging.sendErrorMessage(player, "Selected position is less than the world's minimum height");
            return false;
        }

        return true;
    }

    private static Component getSelectedPositionComponent(boolean pos1, Location location) {
        int pos = pos1 ? 1 : 2;

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return Component.text()
                .append(Component.text(String.format("Position %s: ", pos), NamedTextColor.GRAY, TextDecoration.ITALIC))
                .append(Component.text("X=" + x, NamedTextColor.RED)).resetStyle()
                .append(Component.text("/", NamedTextColor.GRAY))
                .append(Component.text("Y=" + y, NamedTextColor.GREEN))
                .append(Component.text("/", NamedTextColor.GRAY))
                .append(Component.text("Z=" + z, NamedTextColor.BLUE))
                .build();
    }
}
