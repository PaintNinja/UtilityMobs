package toast.utilityMobs.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerWorkbench;

public class ContainerWorkbenchGolem extends ContainerWorkbench
{
    /// The golem being crafted on.
    public final EntityContainerGolem golem;

    public ContainerWorkbenchGolem(PlayerInventory inventory, EntityContainerGolem workbench) {
        super(inventory, workbench.worldObj, (int)workbench.posX, (int)workbench.posY, (int)workbench.posZ);
        this.golem = workbench;
        this.golem.openInventory();
    }

    /// Callback for when the crafting gui is closed.
    @Override
    public void onContainerClosed(PlayerInventory player) {
        super.onContainerClosed(player);
        this.golem.closeInventory();
    }

    /// Returns true if this container can be opened by the player.
    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return this.golem.isUseableByPlayer(player);
    }
}