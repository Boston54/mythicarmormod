package me.boston.mythicarmor.block.entity;

import me.boston.mythicarmor.gui.ImbuingStationMenu;
import me.boston.mythicarmor.mythic.ImbueType;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.mythic.items.EssenceItem;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImbuingStationBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;
    private int progress = 0;
    private final int maxProgress = 10;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public ImbuingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.IMBUING_STATION.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> ImbuingStationBlockEntity.this.progress;
                    case 1 -> ImbuingStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> ImbuingStationBlockEntity.this.progress = pValue;
                    case 1 -> ImbuingStationBlockEntity.this.progress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Imbuing Station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ImbuingStationMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.put("inventory", itemHandler.serializeNBT(registries));
        nbt.putInt("imbuing_station.progress", this.progress);
        super.saveAdditional(nbt, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("imbuing_station.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ImbuingStationBlockEntity pEntity) {
        if (level.isClientSide())
            return;

        if (hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, blockPos, blockState);

            if (pEntity.progress >= pEntity.maxProgress) {
                imbueItem(pEntity);
                pEntity.itemHandler.extractItem(0, 1, false);
                pEntity.resetProgress();
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, blockPos, blockState);
        }
    }

    private static boolean hasRecipe(ImbuingStationBlockEntity pEntity) {
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        // Is there essence in the entity
        Item essenceItem = inventory.getItem(0).getItem();
        boolean hasEssence = essenceItem instanceof EssenceItem;
        if (!hasEssence) return false;
        ImbueType imbueType = ((EssenceItem)essenceItem).getImbueType();

        // Is there a mythic item in the input slot
        ItemStack mythicItem = inventory.getItem(1);
        boolean hasMythic = mythicItem.getItem() instanceof IMythicItem;
        if (!hasMythic) return false;

        // Can the armor be imbued
        boolean canBeImbued = MythicItemHandler.canImbueItem(inventory.getItem(1), 1) && imbueType.canImbueItem(mythicItem);

        return canBeImbued;
    }

    private static void imbueItem(ImbuingStationBlockEntity pEntity) {
        @NotNull Item essence = pEntity.itemHandler.getStackInSlot(0).getItem();
        @NotNull ItemStack mythicItem = pEntity.itemHandler.getStackInSlot(1);
        MythicItemHandler.imbueItem(mythicItem, ((EssenceItem)essence).getImbueType());

//        if (mythicItem.getItem() instanceof IMythicTool) {
//            int prosperity = MythicItemHandler.getImbuePercentage(mythicItem, ImbueType.PROSPERITY);
//            if (prosperity > 0) {
//                ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(mythicItem);
//                // See if it needs its fortune level increased
//                if (Arrays.stream(ImbuementEffectsHandler.PROSPERITY_FORTUNE_PERCENTAGES).anyMatch(p -> p == prosperity)) {
//                    var lookup = pEntity.getLevel().holderLookup(Registries.ENCHANTMENT);
//                    int fortune = EnchantmentHelper.getItemEnchantmentLevel(lookup.getOrThrow(Enchantments.FORTUNE), mythicItem);
//                    enchantments.
//                }
//                EnchantmentHelper.setEnchantments(mythicItem, enchantments);
//            }
//        }
    }
}
