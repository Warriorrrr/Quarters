package au.lupine.quarters.command.quartersadmin.method.set;

import au.lupine.quarters.api.QuartersMessaging;
import au.lupine.quarters.object.base.CommandMethod;
import au.lupine.quarters.object.entity.Quarter;
import au.lupine.quarters.object.exception.CommandMethodException;
import au.lupine.quarters.object.wrapper.StringConstants;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminSetOwnerMethod extends CommandMethod {

    public AdminSetOwnerMethod(CommandSender sender, String[] args) {
        super(sender, args, "quarters.command.quartersadmin.set.owner");
    }

    @Override
    public void execute() {
        Player player = getSenderAsPlayerOrThrow();
        Quarter quarter = getQuarterAtPlayerOrThrow(player);

        Resident resident = TownyAPI.getInstance().getResident(getArgOrThrow(0, StringConstants.A_REQUIRED_ARGUMENT_WAS_NOT_PROVIDED));
        if (resident == null || resident.isNPC()) throw new CommandMethodException(StringConstants.SPECIFIED_PLAYER_DOES_NOT_EXIST);

        if (!quarter.isEmbassy() && !quarter.getTown().hasResident(resident)) throw new CommandMethodException(StringConstants.SPECIFIED_PLAYER_COULD_NOT_BE_SET_AS_OWNER);

        quarter.setOwner(resident.getUUID());
        quarter.save();

        QuartersMessaging.sendSuccessMessage(player, "Successfully set this quarter's owner to " + resident.getName());
    }
}
