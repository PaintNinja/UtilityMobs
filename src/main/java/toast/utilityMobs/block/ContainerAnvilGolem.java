package toast.utilityMobs.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAnvilGolem extends RepairContainer
{
    // The golem being crafted on.
    public final EntityAnvilGolem golem;
    // The two slots where you put the items in that you want to merge and/or rename.
    public IInventory inputSlots;
    // Determined by damage of input item and stackSize of repair materials.
    public int stackSizeToBeUsedInRepair;

    public ContainerAnvilGolem(PlayerInventory inventory, EntityAnvilGolem anvil, PlayerEntity player) {
        super(inventory, anvil.worldObj, -1, -1, -1, player);
        this.golem = anvil;
        this.golem.openInventory();
        this.inputSlots = ((Slot)this.inventorySlots.get(0)).inventory;
        Slot oldSlot = (Slot)this.inventorySlots.get(2);
        Slot newSlot = new ContainerAnvilGolemSlot(this, oldSlot.inventory, oldSlot.getSlotIndex(), oldSlot.xDisplayPosition, oldSlot.yDisplayPosition);
        newSlot.slotNumber = oldSlot.slotNumber;
        this.inventorySlots.set(2, newSlot);
    }

    // Callback for when the crafting gui is closed.
    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        this.golem.closeInventory();
    }

    // Returns true if this container can be opened by the player.
    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return this.golem.isUseableByPlayer(player);
    }

    // Called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot.
    @Override
    public void updateRepairOutput() {
        super.updateRepairOutput();
        // Update the needlessly private field, stackSizeToBeUsedInRepair.
        ItemStack itemStack = this.inputSlots.getStackInSlot(0);
        if (itemStack != null) {
            ItemStack itemStackTmp = itemStack.copy();
            ItemStack itemStackToBeUsed = this.inputSlots.getStackInSlot(1);
            this.stackSizeToBeUsedInRepair = 0;
            if (itemStackToBeUsed != null) {
                if (itemStackTmp.isItemStackDamageable() && itemStackTmp.getItem().getIsRepairable(itemStack, itemStackToBeUsed)) {
                    int itemDamage = Math.min(itemStackTmp.getItemDamageForDisplay(), itemStackTmp.getMaxDamage() / 4);
                    if (itemDamage <= 0)
                        return;
                    int stackSizeToBeUsed;
                    for (stackSizeToBeUsed = 0; itemDamage > 0 && stackSizeToBeUsed < itemStackToBeUsed.stackSize; stackSizeToBeUsed++) {
                        itemStackTmp.setItemDamage(itemStackTmp.getItemDamageForDisplay() - itemDamage);
                        itemDamage = Math.min(itemStackTmp.getItemDamageForDisplay(), itemStackTmp.getMaxDamage() / 4);
                    }
                    this.stackSizeToBeUsedInRepair = stackSizeToBeUsed;
                }
            }
        }
    }
}