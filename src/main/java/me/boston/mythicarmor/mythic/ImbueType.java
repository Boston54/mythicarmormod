package me.boston.mythicarmor.mythic;

import me.boston.mythicarmor.component.ModDataComponentTypes;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicArmor;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicItem;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicTool;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicWeapon;
import me.boston.mythicarmor.util.RGB;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public enum ImbueType {
    MAGMA("magma", "Magma", new RGB(255, 170, 0), '6',
            new LinkedHashMap<>() {{
                put(ImbuableEquipment.ARMOR, List.of(
                        new ImbueEffect("+%s%% chance to light attackers on fire", ImbuementEffectsHandler.MAGMA_LIGHT_ATTACKERS_CHANCE),
                        new ImbueEffect.PercentageEffect("permanent fire resistance", 0, ImbuementEffectsHandler.MAGMA_FIRE_RESISTANCE_PERCENTAGE)
                ));
                put(ImbuableEquipment.WEAPON, List.of(
                        new ImbueEffect("+%s%% outgoing damage to non-fire resistant enemies", ImbuementEffectsHandler.MAGMA_OUTGOING_INCREASE)
                ));
                put(ImbuableEquipment.TOOL, List.of(
                        new ImbueEffect("%s%% chance to auto smelt mined blocks", ImbuementEffectsHandler.MAGMA_AUTO_SMELT_CHANCE)
                ));
                put(ImbuableEquipment.PICKAXE, List.of(
                        new ImbueEffect("+%s%% obsidian mining speed", ImbuementEffectsHandler.MAGMA_BREAK_OBSIDIAN_SPEED_INCREASE)
                ));
            }}),
    END("end", "End", new RGB(170, 0, 170), '5',
            new LinkedHashMap<>() {{
                put(ImbuableEquipment.ARMOR, List.of(
                        new ImbueEffect("-%s%% incoming damage from enemies with more than 100 max hp", ImbuementEffectsHandler.ENDER_INCOMING_DECREASE),
                        new ImbueEffect("+%s%% chance to get an ender pearl back after teleporting", ImbuementEffectsHandler.ENDER_PEARL_GIVE)
                ));
                put(ImbuableEquipment.WEAPON, List.of(
                        new ImbueEffect("+%s%% outgoing damage to enemies with more than 100 max hp", ImbuementEffectsHandler.ENDER_OUTGOING_INCREASE)
                ));
            }}),
    PROSPERITY("prosperity", "Prosperity", new RGB(85, 255, 85), 'a',
            new LinkedHashMap<>() {{
                put(ImbuableEquipment.ARMOR, List.of(
                        new ImbueEffect("+%s%% chance to dodge incoming damage", ImbuementEffectsHandler.PROSPERITY_DODGE_CHANCE)
                ));
                put(ImbuableEquipment.WEAPON, List.of(
                        new ImbueEffect("+%s%% chance to one-shot any enemy with less than 100 max hp", ImbuementEffectsHandler.PROSPERITY_ONESHOT_CHANCE),
                        new ImbueEffect.PercentageEffect("+%s looting level(s)", 1, ImbuementEffectsHandler.PROSPERITY_LOOTING_PERCENTAGES)
                ));
                put(ImbuableEquipment.TOOL, List.of(
                        new ImbueEffect.PercentageEffect("+%s fortune level(s)", 1, ImbuementEffectsHandler.PROSPERITY_FORTUNE_PERCENTAGES)
                ));
            }}),
    AMETHYST("amethyst", "Amethyst", new RGB(255, 85, 255), 'd',
            new LinkedHashMap<>() {{
                put(ImbuableEquipment.ARMOR, List.of(
                        new ImbueEffect("+%s max hp", ImbuementEffectsHandler.AMETHYST_MAX_HP),
                        new ImbueEffect("-%s%% movement speed", ImbuementEffectsHandler.AMETHYST_MOVE_SPEED)
                ));
                put(ImbuableEquipment.WEAPON, List.of(
                        new ImbueEffect("+%s%% outgoing damage", ImbuementEffectsHandler.AMETHYST_OUTGOING_INCREASE)
                ));
                put(ImbuableEquipment.TOOL, List.of(
                        new ImbueEffect("+%s%% chance to repair 1 durability when breaking a block", ImbuementEffectsHandler.AMETHYST_REPAIR_CHANCE)
                ));
                put(ImbuableEquipment.ALL, List.of(
                        new ImbueEffect.PercentageEffect("unbreakable", 0, ImbuementEffectsHandler.AMETHYST_UNBREAKABLE_PERCENTAGE)
                ));
            }}),
    AGILITY("agility", "Agility", new RGB(255, 255, 85), 'e',
            new LinkedHashMap<>() {{
                put(ImbuableEquipment.ARMOR, List.of(
                        new ImbueEffect("+%s%% movement speed", ImbuementEffectsHandler.AGILITY_MOVE_SPEED)
                ));
                put(ImbuableEquipment.WEAPON, List.of(
                        new ImbueEffect("+%s%% attack speed", ImbuementEffectsHandler.AGILITY_ATTACK_SPEED)
                ));
                put(ImbuableEquipment.TOOL, List.of(
                        new ImbueEffect("+%s%% mining speed", ImbuementEffectsHandler.AGILITY_BREAK_SPEED_INCREASE)
                ));
            }}),
    ANCIENT("ancient", "Ancient", new RGB(68, 0, 255), '1',
            new LinkedHashMap<>() {{
                put(ImbuableEquipment.ARMOR, List.of(
                        new ImbueEffect("+%s%% experience from entities and blocks", ImbuementEffectsHandler.ANCIENT_ARMOR_XP_GAIN)
                ));
                put(ImbuableEquipment.WEAPON, List.of(
                        new ImbueEffect("+%s%% experience from entities", ImbuementEffectsHandler.ANCIENT_WEAPON_XP_GAIN)
                ));
                put(ImbuableEquipment.TOOL, List.of(
                        new ImbueEffect("+%s%% experience from blocks", ImbuementEffectsHandler.ANCIENT_TOOL_XP_GAIN)
                ));
            }}),
    SEA("sea", "Sea", new RGB(0, 183, 255), 'b',
            new LinkedHashMap<>() {{
                put(ImbuableEquipment.ARMOR, List.of(
                        new ImbueEffect("+%s%% increased swimming speed", ImbuementEffectsHandler.SEA_SWIM_SPEED),
                        new ImbueEffect.PercentageEffect("permanent water breathing", 0, ImbuementEffectsHandler.SEA_WATER_BREATHING_PERCENTAGE)
                ));
                put(ImbuableEquipment.WEAPON, List.of(
                        new ImbueEffect("+%s%% outgoing damage to fire resistant enemies", ImbuementEffectsHandler.SEA_OUTGOING_INCREASE)
                ));
                put(ImbuableEquipment.TOOL, List.of(
                        new ImbueEffect("+%s%% underwater mining speed", ImbuementEffectsHandler.SEA_UNDERWATER_MINING_SPEED)
                ));
            }});

    private final String id;
    private final String displayName;
    private final RGB textureColor;
    private final char textColorCode;
    private final LinkedHashMap<ImbuableEquipment, List<ImbueEffect>> effectDescriptions;

    ImbueType(String id, String displayName, RGB textureColor, char textColorCode,
              LinkedHashMap<ImbuableEquipment, List<ImbueEffect>> effectDescriptions) {
        this.id = "mythicarmor.imbue." + id;
        this.displayName = displayName;
        this.textureColor = textureColor;
        this.textColorCode = textColorCode;
        this.effectDescriptions = effectDescriptions;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public char getTextColorCode() {
        return textColorCode;
    }

    public RGB getColor() {
        return textureColor;
    }

    public DataComponentType<Integer> getDataComponentType() {
        return ModDataComponentTypes.IMBUEMENT_COMPONENT_TYPES.get(this).get();
    }

    public String getFormattedDisplayPercentage(int level) {
        return "§e" + level + "%§r§" + textColorCode + " " + displayName;
    }

    public LinkedHashMap<ImbuableEquipment, List<ImbueEffect>> getEffectDescriptions() {
        return effectDescriptions;
    }

    // works by checking if any of the keys from the effectDescriptions hashmap are for this item type
    public boolean canImbueItem(ItemStack stack) {
        Item item = stack.getItem();
        return effectDescriptions.keySet().stream().anyMatch(i -> i.is(item));
    }


    public enum ImbuableEquipment {
        ARMOR("Armor"), // includes elytra
        WEAPON("Weapons"),
        TOOL("Tools"),
        ELYTRA("Elytra"),
        PICKAXE("Pickaxes"),
        ALL("All");

        private String displayName;

        ImbuableEquipment(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean is(Item item) {
            return switch (this) {
                case ARMOR -> item instanceof IMythicArmor;
                case WEAPON -> item instanceof IMythicWeapon;
                case TOOL -> item instanceof IMythicTool;
                case ELYTRA -> item instanceof IMythicArmor && item instanceof ElytraItem;
                case PICKAXE -> item instanceof IMythicTool && item instanceof PickaxeItem;
                case ALL -> item instanceof IMythicItem;
            };
        }
    }

    public static class ImbueEffect {
        protected final String description;
        protected final float influence;

        public ImbueEffect(String description, float influence) {
            this.description = description;
            this.influence = influence;
        }

        public String getUnformattedDescription() {
            return description;
        }

        public String getFormattedDescriptionForLevel(int level) {
            if (influence == 0)
                return description;
            return String.format(description, formatFloat(getInfluenceForLevel(level)));
        }

        public List<String> getFormattedDescriptionForEssenceItem() {
            return List.of(getFormattedDescriptionForLevel(1));
        }

        public float getInfluenceForLevel(int level) {
            return influence * level;
        }

        protected String formatFloat(float value) {
            return String.format("%.4f", value).replaceAll("\\.?0*$", "");
        }

        public static class PercentageEffect extends ImbueEffect {
            private final int[] percentages;

            public PercentageEffect(String description, int influence, int... percentages) {
                super(description, influence);
                this.percentages = percentages;
            }

            @Override
            @Nullable
            public String getFormattedDescriptionForLevel(int level) {
                float total_influence = 0;
                for (int percentage : percentages) {
                    if (level >= percentage)
                        total_influence += 1;
                }
                if (total_influence == 0)
                    return null;
                return String.format(description, formatFloat(total_influence * influence));
            }

            @Override
            public List<String> getFormattedDescriptionForEssenceItem() {
                List<String> descriptions = new ArrayList<>();
                String modifiedDescription = "(at %s%%) " + String.format(description, formatFloat(influence));
                for (int percentage : percentages) {
                    descriptions.add(String.format(modifiedDescription, percentage));
                }
                return descriptions;
            }

            public int[] getPercentages() {
                return percentages;
            }
        }

        public static class DifferentDescription extends ImbueEffect {
            private String essenceDescription;

            public DifferentDescription(String itemDescription, String essenceDescription, float influence) {
                super(itemDescription, influence);
                this.essenceDescription = essenceDescription;
            }

            @Override
            public List<String> getFormattedDescriptionForEssenceItem() {
                return List.of(String.format(essenceDescription, formatFloat(influence)));
            }
        }
    }
}