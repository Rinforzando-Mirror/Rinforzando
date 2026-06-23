package adris.rinforzando.multiversion.item;

import net.minecraft.item.MiningToolItem;

public class ItemHelper {


    //#if MC <= 11605
    //$$ public static boolean isSuitableFor(Item item, BlockState state){
    //$$     if (item instanceof PickaxeItem pickaxe) {
    //$$         return pickaxe.isSuitableFor(state);
    //$$     }
    //$$
    //$$     if (item instanceof MiningToolItem) {
    //$$         boolean isInEffectiveBlocks = ((MiningToolItemAccessor)item).getEffectiveBlocks().contains(state.getBlock());
    //$$
    //$$         if (item instanceof AxeItem) {
    //$$             return isInEffectiveBlocks || ((AxeItemAccessor)item).getEffectiveMaterials().contains(state.getMaterial());
    //$$         }
    //$$         return isInEffectiveBlocks;
    //$$     }
    //$$
    //$$     return item.isSuitableFor(state);
    //$$ }
    //$$
    //#endif

}
