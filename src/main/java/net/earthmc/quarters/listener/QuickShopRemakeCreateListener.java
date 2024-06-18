package net.earthmc.quarters.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import net.earthmc.quarters.object.Quarter;
import net.earthmc.quarters.object.QuarterType;
import net.earthmc.quarters.util.QuarterUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.maxgamer.quickshop.api.event.ShopCreateEvent;

import java.util.Objects;

public class QuickShopRemakeCreateListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onShopCreate(ShopCreateEvent event) {
        if (!event.isCancelled())
            return;

        Quarter quarter = QuarterUtil.getQuarter(event.getShop().getLocation());
        if (quarter == null)
            return;

        if (quarter.getType() != QuarterType.SHOP)
            return;

        Player player = Bukkit.getPlayer(event.getCreator());
        if (player == null)
            return;

        Resident resident = TownyAPI.getInstance().getResident(player);
        if (resident == null)
            return;

        if (Objects.equals(quarter.getOwnerResident(), resident) || quarter.getTrustedResidents().contains(resident))
            event.setCancelled(false);
    }
}
