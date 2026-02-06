package com.projectkorra.projectkorra.region;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.CoreAbility;

import net.william278.huskclaims.api.BukkitHuskClaimsAPI;
import net.william278.huskclaims.claim.Claim;
import net.william278.huskclaims.libraries.cloplib.operation.OperationType;
import net.william278.huskclaims.position.Position;
import net.william278.huskclaims.user.OnlineUser;

public class HuskClaims extends RegionProtectionBase {

    protected BukkitHuskClaimsAPI huskClaimsAPI;

    protected HuskClaims() {
        super("HuskClaims");
        this.huskClaimsAPI = BukkitHuskClaimsAPI.getInstance();
    }

    @Override
    public boolean isRegionProtectedReal(Player player, Location location, CoreAbility ability, boolean igniteAbility, boolean explosiveAbility) {
        final OnlineUser user = huskClaimsAPI.getOnlineUser(player);
        final Position position = huskClaimsAPI.getPosition(location);

        final Optional<Claim> claimOpt = huskClaimsAPI.getClaimAt(position);
        if (claimOpt.isEmpty()) {
            return false; // No claim (wilderness) - allow bending
        }

        final Claim claim = claimOpt.get();
        if (claim.getOwner().filter(uuid -> uuid.equals(player.getUniqueId())).isPresent()) {
            return false; // Player is claim owner - allow bending
        }

        // Block bending unless player has build/trust permissions
        return !huskClaimsAPI.isOperationAllowed(user, OperationType.BLOCK_BREAK, position);
    }
}
