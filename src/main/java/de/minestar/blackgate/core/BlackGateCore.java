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

package de.minestar.blackgate.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.PluginManager;

import de.minestar.blackgate.listener.LoginListener;
import de.minestar.core.units.MinestarGroup;
import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class BlackGateCore extends AbstractCore {

    public final static String NAME = "BlackGate";

    private static List<MinestarGroup> allowedGroups;

    public BlackGateCore() {
        super(NAME);
    }

    @Override
    protected boolean loadingConfigs(File dataFolder) {

        File groupWhiteListFile = new File(dataFolder, "GroupWhiteList.txt");
        if (!groupWhiteListFile.exists())
            generateDefaultWhiteList(groupWhiteListFile);

        allowedGroups = loadGroupsFromFile(groupWhiteListFile);

        return true;
    }

    private void generateDefaultWhiteList(File groupWhiteListFile) {
        ConsoleUtils.printInfo(NAME, "Generating default whitelist for groups allowing all groups..");

        // Write all minestar group names in one file and allow everyone
        MinestarGroup[] groups = MinestarGroup.values();
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(groupWhiteListFile))) {

            for (int i = 0; i < groups.length; i++) {
                MinestarGroup minestarGroup = groups[i];
                bWriter.write(minestarGroup.name());
                bWriter.newLine();
            }
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Writing default whitelist file");
        }
    }

    private List<MinestarGroup> loadGroupsFromFile(File listFile) {
        List<MinestarGroup> allowedGroups = new ArrayList<MinestarGroup>();

        try (BufferedReader bReader = new BufferedReader(new FileReader(listFile))) {
            String line = null;
            while ((line = bReader.readLine()) != null) {
                try {
                    allowedGroups.add(MinestarGroup.valueOf(line.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    ConsoleUtils.printError(NAME, "Unknown group: " + line);
                }
            }
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Loading group from file");
        }

        return allowedGroups;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {

        pm.registerEvents(new LoginListener(), this);

        return true;
    }

    public static boolean isAllowedToJoin(MinestarGroup group) {
        return allowedGroups.contains(group);
    }
}
