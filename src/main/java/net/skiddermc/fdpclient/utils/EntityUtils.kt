/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.skiddermc.fdpclient.utils

import net.skiddermc.fdpclient.FDPClient
import net.skiddermc.fdpclient.features.module.modules.client.Target.animalValue
import net.skiddermc.fdpclient.features.module.modules.client.Target.deadValue
import net.skiddermc.fdpclient.features.module.modules.client.Target.invisibleValue
import net.skiddermc.fdpclient.features.module.modules.client.Target.mobValue
import net.skiddermc.fdpclient.features.module.modules.client.Target.playerValue
import net.skiddermc.fdpclient.features.module.modules.misc.AntiBot.isBot
import net.skiddermc.fdpclient.features.module.modules.misc.Teams
import net.skiddermc.fdpclient.utils.render.ColorUtils.stripColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.monster.EntityGhast
import net.minecraft.entity.monster.EntityGolem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.monster.EntitySlime
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityBat
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer

object EntityUtils : MinecraftInstance() {
    fun isSelected(entity: Entity, canAttackCheck: Boolean): Boolean {
        if (entity is EntityLivingBase && (deadValue.get() || entity.isEntityAlive()) && entity !== mc.thePlayer) {
            if (invisibleValue.get() || !entity.isInvisible()) {
                if (playerValue.get() && entity is EntityPlayer) {
                    if (canAttackCheck) {
                        if (isBot(entity)) {
                            return false
                        }

                        if (isFriend(entity)) {
                            return false
                        }

                        if (entity.isSpectator) {
                            return false
                        }

                        if (entity.isPlayerSleeping) {
                            return false
                        }

                        if (!FDPClient.combatManager.isFocusEntity(entity)) {
                            return false
                        }

                        val teams = FDPClient.moduleManager.getModule(Teams::class.java)
                        return !teams!!.state || !teams.isInYourTeam(entity)
                    }

                    return true
                }
                return mobValue.get() && isMob(entity) || animalValue.get() && isAnimal(entity)
            }
        }
        return false
    }

    fun isFriend(entity: Entity): Boolean {
        return entity is EntityPlayer && entity.getName() != null && FDPClient.fileManager.friendsConfig.isFriend(stripColor(entity.getName()))
    }

    fun isFriend(entity: String): Boolean {
        return FDPClient.fileManager.friendsConfig.isFriend(entity)
    }

    fun isAnimal(entity: Entity): Boolean {
        return entity is EntityAnimal || entity is EntitySquid || entity is EntityGolem || entity is EntityVillager || entity is EntityBat
    }

    fun isMob(entity: Entity): Boolean {
        return entity is EntityMob || entity is EntitySlime || entity is EntityGhast || entity is EntityDragon
    }
}