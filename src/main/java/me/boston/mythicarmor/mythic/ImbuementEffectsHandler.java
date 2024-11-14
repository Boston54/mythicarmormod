package me.boston.mythicarmor.mythic;

import com.google.common.collect.Streams;
import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicArmor;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicTool;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicWeapon;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ImbuementEffectsHandler {
    public static final float MAGMA_LIGHT_ATTACKERS_CHANCE = 0.25f;
    public static final int MAGMA_LIGHT_ATTACKERS_DURATION = 80;
    public static final int MAGMA_FIRE_RESISTANCE_PERCENTAGE = 100;
    public static final float MAGMA_OUTGOING_INCREASE = 0.65f;
    public static final float MAGMA_AUTO_SMELT_CHANCE = 1f;
    public static final float MAGMA_BREAK_OBSIDIAN_SPEED_INCREASE = 3f;

    public static final float ENDER_INCOMING_DECREASE = 0.2f;
    public static final float ENDER_PEARL_GIVE = 1f;
    public static final float ENDER_OUTGOING_INCREASE = 1f;

    public static final float PROSPERITY_DODGE_CHANCE = 0.125f;
    public static final float PROSPERITY_ONESHOT_CHANCE = 0.2f;
    public static final int[] PROSPERITY_LOOTING_PERCENTAGES = new int[] {50, 100};
    public static final int[] PROSPERITY_FORTUNE_PERCENTAGES = new int[] {50, 100};

    public static final float AMETHYST_MAX_HP = 0.1f;
    public static final float AMETHYST_MOVE_SPEED = 0.2f;
    public static final float AMETHYST_OUTGOING_INCREASE = 0.5f;
    public static final float AMETHYST_REPAIR_CHANCE = 0.25f;
    public static final int AMETHYST_UNBREAKABLE_PERCENTAGE = 100;

    public static final float AGILITY_MOVE_SPEED = 0.4f;
    public static final float AGILITY_ATTACK_SPEED = 0.5f;
    public static final float AGILITY_BREAK_SPEED_INCREASE = 1f;

    public static final float ANCIENT_ARMOR_XP_GAIN = 0.5f;
    public static final float ANCIENT_WEAPON_XP_GAIN = 1;
    public static final float ANCIENT_TOOL_XP_GAIN = 2;

    public static final float SEA_SWIM_SPEED = 1f;
    public static final int SEA_WATER_BREATHING_PERCENTAGE = 100;
    public static final float SEA_OUTGOING_INCREASE = 0.8f;
    public static final float SEA_UNDERWATER_MINING_SPEED = 2f;

    private static void armorTick(ItemStack itemStack, Level level, Player player, int slot, boolean selected) {
        if (itemStack.getItem() instanceof IMythicArmor) {
            // Magma
            int magma = getImbuePercentage(itemStack, ImbueType.MAGMA);
            if (magma >= MAGMA_FIRE_RESISTANCE_PERCENTAGE) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            }
            // Sea
            int sea = getImbuePercentage(itemStack, ImbueType.SEA);
            if (sea >= SEA_WATER_BREATHING_PERCENTAGE) {
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20, 0, false, false));
            }
        }
    }

    private static void mainHandTick(ItemStack itemStack, Level level, Player player, int slot, boolean selected) {

    }

    @SubscribeEvent
    public static void onEntityTakeDamage(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        float damageAmount = event.getAmount();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        ItemStack attackerWeapon = damageSource.getWeaponItem();

        // Create stuff for if the entity is living
        boolean attackerIsLiving = attacker instanceof LivingEntity;
        @Nullable LivingEntity livingAttacker = attackerIsLiving ? (LivingEntity) attacker : null;

        // Check the damage was dealt using a mythic weapon
        boolean attackerUsedMythicWeapon = attackerWeapon != null && attackerWeapon.getItem() instanceof IMythicWeapon;
        // Get the mythic items the target has equipped
        List<ItemStack> targetEquippedMythicArmor = getEquippedMythicArmorItems(target);

        float totalDamageIncrease = 0;

        //
        // ATTACKER WEAPON EFFECTS
        //

        if (attackerUsedMythicWeapon) {
            // Magma
            int attackerMagma = getImbuePercentage(attackerWeapon, ImbueType.MAGMA);
            if (attackerMagma > 0) {
                if (!target.fireImmune()) {
                    totalDamageIncrease += (damageAmount * attackerMagma * MAGMA_OUTGOING_INCREASE) / 100f;
                }
            }

            // End
            int attackerEnd = getImbuePercentage(attackerWeapon, ImbueType.END);
            if (attackerEnd > 0) {
                if (target.getMaxHealth() > 100) {
                    totalDamageIncrease += (damageAmount * attackerEnd * ENDER_OUTGOING_INCREASE / 100f);
                }
            }

            // Prosperity
            int attackerProsperity = getImbuePercentage(attackerWeapon, ImbueType.PROSPERITY);
            if (attackerProsperity > 0) {
                if (getProc(attackerProsperity * PROSPERITY_ONESHOT_CHANCE)) {
                    totalDamageIncrease += 1000000;
                    if (attacker instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("§a§l**ONESHOT**")));
                        player.playNotifySound(SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1f, 1.3f);
                    }
                }
            }

            // Amethyst
            int attackerAmethyst = getImbuePercentage(attackerWeapon, ImbueType.AMETHYST);
            if (attackerAmethyst > 0) {
                totalDamageIncrease += (damageAmount * attackerAmethyst * AMETHYST_OUTGOING_INCREASE) / 100f;
            }

            // Sea
            int attackerSea = getImbuePercentage(attackerWeapon, ImbueType.SEA);
            if (attackerSea > 0) {
                if (target.fireImmune()) {
                    totalDamageIncrease += (damageAmount * attackerSea * SEA_OUTGOING_INCREASE) / 100f;
                }
            }
        }

        //
        // TARGET ARMOR EFFECTS
        //

        if (attacker != null) {
            // Magma
            int targetMagma = getImbuePercentage(targetEquippedMythicArmor, ImbueType.MAGMA);
            if (targetMagma > 0) {
                if (!attacker.fireImmune() && getProc(targetMagma * MAGMA_LIGHT_ATTACKERS_CHANCE)) {
                    attacker.setRemainingFireTicks(MAGMA_LIGHT_ATTACKERS_DURATION);
                }
            }

            // End
            int targetEnd = getImbuePercentage(targetEquippedMythicArmor, ImbueType.END);
            if (targetEnd > 0) {
                if (attackerIsLiving && livingAttacker.getMaxHealth() > 100) {
                    totalDamageIncrease -= (damageAmount * targetEnd * ENDER_INCOMING_DECREASE / 100f);
                }
            }
        }

        // Prosperity
        int targetProsperity = getImbuePercentage(targetEquippedMythicArmor, ImbueType.PROSPERITY);
        if (targetProsperity > 0) {
            if (getProc(targetProsperity * PROSPERITY_DODGE_CHANCE)) {
                if (target instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("§a§l**DODGED**")));
                    player.playNotifySound(SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.7f, 1.5f);
                }
                event.setCanceled(true);
            }
        }

        // Save the new information about this event
        event.setAmount(Math.max(0, damageAmount + totalDamageIncrease));
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        LivingEntity killer = entity.getKillCredit();
        if (!(killer instanceof ServerPlayer player))
            return;

        ItemStack weapon = event.getSource().getWeaponItem();

        List<ItemStack> killerMythicArmorItems = getEquippedMythicArmorItems(killer);
        if ((weapon != null && weapon.getItem() instanceof IMythicWeapon) || !killerMythicArmorItems.isEmpty()) {
            // Ancient
            int weaponAncient = getImbuePercentage(weapon, ImbueType.ANCIENT);
            int armorAncient = getImbuePercentage(killerMythicArmorItems, ImbueType.ANCIENT);
            if (weaponAncient + armorAncient > 0) {
                ServerLevel serverLevel = player.serverLevel();
                int originalXp = entity.getExperienceReward(serverLevel, killer);
                int extraXpWeapon = Math.round(originalXp * (weaponAncient * ANCIENT_WEAPON_XP_GAIN / 100f));
                int extraXpArmor = Math.round(originalXp * (armorAncient * ANCIENT_ARMOR_XP_GAIN / 100f));
                ExperienceOrb xpOrb = new ExperienceOrb(serverLevel, entity.getX(), entity.getY(), entity.getZ(), extraXpWeapon + extraXpArmor);
                serverLevel.addFreshEntity(xpOrb);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getItemInHand(InteractionHand.MAIN_HAND);
        List<ItemStack> mythicArmorItems = getEquippedMythicArmorItems(player);

        if (tool.getItem() instanceof IMythicTool) {
            // Amethyst
            int amethyst = getImbuePercentage(tool, ImbueType.AMETHYST);
            if (amethyst > 0) {
                if (getProc(amethyst * AMETHYST_REPAIR_CHANCE)) {
                    tool.setDamageValue(Math.max(tool.getDamageValue() - 1, 0));
                }
            }
        }

        if (tool.getItem() instanceof IMythicTool || !mythicArmorItems.isEmpty()) {
            // Ancient
            int toolAncient = getImbuePercentage(tool, ImbueType.ANCIENT);
            int armorAncient = getImbuePercentage(mythicArmorItems, ImbueType.ANCIENT);
            if (toolAncient + armorAncient > 0) {
                float extraXp = (toolAncient * ANCIENT_TOOL_XP_GAIN / 100f) + (armorAncient * ANCIENT_ARMOR_XP_GAIN / 100f);
                int newXp = Math.round(event.getExpToDrop() * (1 + extraXp));
                event.setExpToDrop(newXp);
            }
        }
    }

    @SubscribeEvent
    public static void playerBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack heldItem = event.getEntity().getMainHandItem();
        if (!(heldItem.getItem() instanceof IMythicTool))
            return;

        float totalSpeedIncreasePercent = 0;

        // Magma
        int magma = getImbuePercentage(heldItem, ImbueType.MAGMA);
        if (magma > 0) {
            // obsidian breaking speed
            if (event.getState().getBlock() == Blocks.OBSIDIAN) {
                totalSpeedIncreasePercent += (magma * MAGMA_BREAK_OBSIDIAN_SPEED_INCREASE) / 100f;
            }
        }

        // Agility
        int agility = getImbuePercentage(heldItem, ImbueType.AGILITY);
        if (agility > 0) {
            totalSpeedIncreasePercent += (agility * AGILITY_BREAK_SPEED_INCREASE) / 100f;
        }

        // Sea
        if (event.getEntity().isUnderWater()) {
            int sea = getImbuePercentage(heldItem, ImbueType.SEA);
            if (sea > 0) {
                totalSpeedIncreasePercent += (sea * SEA_UNDERWATER_MINING_SPEED) / 100f;
            }
        }

        event.setNewSpeed(event.getOriginalSpeed() * (1 + totalSpeedIncreasePercent));
    }

    @SubscribeEvent
    public static void lootingLevelEvent(LootingLevelEvent event) {
        DamageSource damageSource = event.getDamageSource();
        if (damageSource == null)
            return;

        ItemStack weapon = damageSource.getWeaponItem();
        if (weapon == null)
            return;

        if (weapon.getItem() instanceof IMythicWeapon) {
            int prosperity = getImbuePercentage(weapon, ImbueType.PROSPERITY);
            if (prosperity > 0) {
                int totalLootingIncrease = 0;
                for (int percentage : PROSPERITY_LOOTING_PERCENTAGES) {
                    if (prosperity >= percentage)
                        totalLootingIncrease++;
                }
                event.setLootingLevel(event.getLootingLevel() + totalLootingIncrease);
            }
        }
    }

    @SubscribeEvent
    public static void onPearlTeleport(EntityTeleportEvent.EnderPearl event) {
        ServerPlayer player = event.getPlayer();
        int end = getImbuePercentage(getEquippedMythicArmorItems(player), ImbueType.END);
        if (end > 0) {
            if (getProc(end * ENDER_PEARL_GIVE)) {
                player.getInventory().add(new ItemStack(Items.ENDER_PEARL));
            }
        }
    }

    public static void onImbue(ItemStack stack, ImbueType imbueType) {
        // Amethyst
        if (imbueType == ImbueType.AMETHYST) {
            int amethyst = getImbuePercentage(stack, ImbueType.AMETHYST);
            if (amethyst >= AMETHYST_UNBREAKABLE_PERCENTAGE) {
                stack.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
            }
        }
    }

    private static final ResourceLocation AMETHYST_HEALTH_RESOURCE = ResourceLocation.fromNamespaceAndPath(MythicArmor.MODID, "amethyst_health");
    private static final ResourceLocation AMETHYST_SPEED_RESOURCE = ResourceLocation.fromNamespaceAndPath(MythicArmor.MODID, "amethyst_speed");
    private static final ResourceLocation AGILITY_SPEED_RESOURCE = ResourceLocation.fromNamespaceAndPath(MythicArmor.MODID, "agility_speed");
    public static final ResourceLocation AGILITY_ATTACK_SPEED_RESOURCE = ResourceLocation.fromNamespaceAndPath(MythicArmor.MODID, "agility_attackspeed");

    public static ItemAttributeModifiers getDefaultAttributeModifiers(ItemAttributeModifiers attributeModifiers, ItemStack itemStack) {
        EquipmentSlotGroup equipmentSlotGroup = getEquipmentSlotGroup(itemStack.getItem());

        int amethyst = getImbuePercentage(itemStack, ImbueType.AMETHYST);
        if (amethyst > 0) {
            if (itemStack.getItem() instanceof IMythicArmor) {
                attributeModifiers = attributeModifiers
                        .withModifierAdded(
                                Attributes.MAX_HEALTH,
                                new AttributeModifier(AMETHYST_HEALTH_RESOURCE, AMETHYST_MAX_HP * amethyst, AttributeModifier.Operation.ADD_VALUE),
                                equipmentSlotGroup)
                        .withModifierAdded(
                                Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(AMETHYST_SPEED_RESOURCE, -AMETHYST_MOVE_SPEED * amethyst / 100f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                equipmentSlotGroup);
            }
        }

        int agility = getImbuePercentage(itemStack, ImbueType.AGILITY);
        if (agility > 0) {
            if (itemStack.getItem() instanceof IMythicArmor) {
                attributeModifiers = attributeModifiers
                        .withModifierAdded(
                                Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(AGILITY_SPEED_RESOURCE, AGILITY_MOVE_SPEED * agility / 100f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                equipmentSlotGroup
                        );
            }

            if (itemStack.getItem() instanceof IMythicWeapon) { // todo for some reason this method isnt even being called on anything except armor so this doesnt work
                attributeModifiers = attributeModifiers
                        .withModifierAdded(
                                Attributes.ATTACK_SPEED,
                                new AttributeModifier(AGILITY_ATTACK_SPEED_RESOURCE, AGILITY_ATTACK_SPEED * agility, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                equipmentSlotGroup
                        );
            }
        }

        return attributeModifiers;
    }

    private static EquipmentSlotGroup getEquipmentSlotGroup(Item item) {
        if (item instanceof ArmorItem armorItem) {
            return EquipmentSlotGroup.bySlot(armorItem.getEquipmentSlot());
        }
        if (item instanceof ElytraItem) {
            return EquipmentSlotGroup.CHEST;
        }
        return EquipmentSlotGroup.MAINHAND;
    }

    public static List<ItemStack> getEquippedMythicArmorItems(LivingEntity entity) {
        return Streams.stream(entity.getArmorSlots().iterator()).filter(stack -> stack.getItem() instanceof IMythicArmor).toList();
    }

    public static void attemptArmorTick (ItemStack itemStack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player && isBeingWorn(itemStack, player)) {
            armorTick(itemStack, level, player, slot, selected);
        }
    }

    public static boolean hasSilkTouch(ItemStack itemStack, Level level) {
        return EnchantmentHelper.getEnchantmentsForCrafting(itemStack).keySet().stream().anyMatch(e -> e.get() == Enchantments.SILK_TOUCH.getOrThrow(level).get());
    }

    public static void attemptMainHandTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player && isSelected(itemStack, player)) {
            mainHandTick(itemStack, level, player, slot, selected);
        }
    }

    private static boolean isBeingWorn(ItemStack itemStack, Player player) {
        return itemStack.getItem() instanceof ArmorItem armorItem && player.getItemBySlot(armorItem.getEquipmentSlot()) == itemStack;
    }

    private static boolean isSelected(ItemStack itemStack, Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND) == itemStack;
    }

    private static int getImbuePercentage(ItemStack itemStack, ImbueType imbueType) {
        return MythicItemHandler.getImbuePercentage(itemStack, imbueType);
    }

    public static int getImbuePercentage(List<ItemStack> itemStacks, ImbueType imbueType) {
        return itemStacks.stream().mapToInt(stack -> getImbuePercentage(stack, imbueType)).sum();
    }

    public static boolean getProc(float percentage) {
        Random random = new Random();
        float rand = random.nextFloat();
        return rand <= percentage / 100f;
    }
}
