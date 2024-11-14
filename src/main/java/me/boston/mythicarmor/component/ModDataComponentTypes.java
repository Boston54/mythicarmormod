package me.boston.mythicarmor.component;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.mythic.ImbueType;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MythicArmor.MODID);

    public static HashMap<ImbueType, RegistryObject<DataComponentType<Integer>>> IMBUEMENT_COMPONENT_TYPES = new HashMap<>();

    public static RegistryObject<DataComponentType<Integer>> IMBUEMENT_TOTAL = register("imbuement.total",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT));

    private static <T>RegistryObject<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

    static {
        for (ImbueType type : ImbueType.values()) {
            IMBUEMENT_COMPONENT_TYPES.put(type, register("imbuement."+type.getId(),
                    builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT)));
        }
    }
}
