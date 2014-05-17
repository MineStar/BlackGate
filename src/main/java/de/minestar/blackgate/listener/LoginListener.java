/*
 * Copyright (C) 2014 MineStar.de 
 * 
 * This file is part of BlackGate.
 * 
 * BlackGate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * BlackGate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with BlackGate.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.blackgate.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import de.minestar.blackgate.core.BlackGateCore;
import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarGroup;
import de.minestar.core.units.MinestarPlayer;

public class LoginListener implements Listener {

    @EventHandler(ignoreCancelled = false)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        MinestarPlayer player = MinestarCore.getPlayer(event.getName());
        MinestarGroup group = player.getMinestarGroup();
        if (!BlackGateCore.isAllowedToJoin(group)) {
            event.setLoginResult(Result.KICK_OTHER);
            event.setKickMessage("Your player group is not allowed on this server!");
        }
    }
}
