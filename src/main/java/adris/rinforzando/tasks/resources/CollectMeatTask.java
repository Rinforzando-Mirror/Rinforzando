package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.multiversion.item.ItemVer;
import adris.rinforzando.tasks.container.SmeltInSmokerTask;
import adris.rinforzando.tasks.movement.PickupDroppedItemTask;
import adris.rinforzando.tasks.movement.TimeoutWanderTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.SmeltTarget;
import adris.rinforzando.util.helpers.ItemHelper;
import adris.rinforzando.util.helpers.StorageHelper;
import adris.rinforzando.util.slots.SmokerSlot;
import adris.rinforzando.util.time.TimerGame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SmokerScreenHandler;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class CollectMeatTask extends Task {
    public static final CookableFoodTarget[] COOKABLE_FOODS = new CookableFoodTarget[]{
            new CookableFoodTarget("beef", CowEntity.class),
            new CookableFoodTarget("porkchop", PigEntity.class),
            new CookableFoodTarget("chicken", ChickenEntity.class),
            new CookableFoodTarget("mutton", SheepEntity.class),
            new CookableFoodTarget("rabbit", RabbitEntity.class)
    };
    private final double unitsNeeded;
    private final TimerGame checkNewOptionsTimer = new TimerGame(10);
    private SmeltInSmokerTask smeltTask = null;
    private Task currentResourceTask = null;

    public CollectMeatTask(double unitsNeeded) {
        this.unitsNeeded = unitsNeeded;
    }

    private static double getFoodPotential(ItemStack food) {
        if (food == null) return 0;
        int count = food.getCount();
        if (count <= 0) return 0;
        for (CookableFoodTarget cookable : COOKABLE_FOODS) {
            if (food.getItem() == cookable.getRaw()) {
                assert ItemVer.getFoodComponent(cookable.getCooked()) != null;
                return count * ItemVer.getFoodComponent(cookable.getCooked()).getHunger();
            }
        }
        return 0;
    }

    private static double calculateFoodPotential(Rinforzando mod) {
        double potentialFood = 0;
        for (ItemStack food : mod.getItemStorage().getItemStacksPlayerInventory(true)) {
            potentialFood += getFoodPotential(food);
        }
        // Check smelting
        ScreenHandler screen = mod.getPlayer().currentScreenHandler;
        if (screen instanceof SmokerScreenHandler) {
            potentialFood += getFoodPotential(StorageHelper.getItemStackInSlot(SmokerSlot.INPUT_SLOT_MATERIALS));
            potentialFood += getFoodPotential(StorageHelper.getItemStackInSlot(SmokerSlot.OUTPUT_SLOT));
        }
        return potentialFood;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected Task onTick() {
        Rinforzando mod = Rinforzando.getInstance();

        CollectFoodTask.blackListChickenJockeys(mod);
        // If we were previously smelting, keep on smelting.
        if (smeltTask != null && smeltTask.isActive() && !smeltTask.isFinished()) {
            setDebugState("Cooking...");
            return smeltTask;
        } else {
            smeltTask = null;
        }
        if (checkNewOptionsTimer.elapsed()) {
            // Try a new resource task
            checkNewOptionsTimer.reset();
            currentResourceTask = null;
        }
        if (currentResourceTask != null && currentResourceTask.isActive() && !currentResourceTask.isFinished() && !currentResourceTask.thisOrChildAreTimedOut()) {
            return currentResourceTask;
        }
        // Calculate potential
        double potentialFood = calculateFoodPotential(mod);
        if (potentialFood >= unitsNeeded) {
            // Convert our raw foods
            // PLAN:
            // - If we have raw foods, smelt all of them
            // Convert raw foods -> cooked foods
            for (CookableFoodTarget cookable : COOKABLE_FOODS) {
                int rawCount = mod.getItemStorage().getItemCount(cookable.getRaw());
                if (rawCount > 0) {
                    //Debug.logMessage("STARTING COOK OF " + cookable.getRaw().getTranslationKey());
                    int toSmelt = rawCount + mod.getItemStorage().getItemCount(cookable.getCooked());
                    smeltTask = new SmeltInSmokerTask(new SmeltTarget(new ItemTarget(cookable.cookedFood, toSmelt), new ItemTarget(cookable.rawFood, rawCount)));
                    smeltTask.ignoreMaterials();
                    return smeltTask;
                }
            }
        } else {
            // Pick up raw/cooked foods on ground
            for (CookableFoodTarget cookable : COOKABLE_FOODS) {
                Task t = this.pickupTaskOrNull(mod, cookable.getRaw(), 20);
                if (t == null) t = this.pickupTaskOrNull(mod, cookable.getCooked(), 40);
                if (t != null) {
                    setDebugState("Picking up Cookable food");
                    currentResourceTask = t;
                    return currentResourceTask;
                }
            }
            // Cooked foods
            double bestScore = 0;
            Entity bestEntity = null;
            Item bestRawFood = null;
            for (CookableFoodTarget cookable : COOKABLE_FOODS) {
                if (!mod.getEntityTracker().entityFound(cookable.mobToKill)) continue;
                Optional<Entity> nearest = mod.getEntityTracker().getClosestEntity(mod.getPlayer().getPos(), cookable.mobToKill);
                if (nearest.isEmpty()) continue; // ?? This crashed once?
                int hungerPerformance = cookable.getCookedUnits();
                double sqDistance = nearest.get().squaredDistanceTo(mod.getPlayer());
                double score = (double) 100 * hungerPerformance / (sqDistance);
                if (score > bestScore) {
                    bestScore = score;
                    bestEntity = nearest.get();
                    bestRawFood = cookable.getRaw();
                }
            }
            if (bestEntity != null) {
                setDebugState("Killing " + bestEntity.getType().getTranslationKey());
                Predicate<Entity> notBaby = entity -> entity instanceof LivingEntity livingEntity && !livingEntity.isBaby();
                currentResourceTask = killTaskOrNull(bestEntity, notBaby, bestRawFood);
                return currentResourceTask;
            }
        }
        for (Item raw : ItemHelper.RAW_FOODS) {
            if (mod.getItemStorage().hasItem(raw)) {
                Optional<Item> cooked = ItemHelper.getCookedFood(raw);
                if (cooked.isPresent()) {
                    int targetCount = mod.getItemStorage().getItemCount(cooked.get()) + mod.getItemStorage().getItemCount(raw);
                    smeltTask = new SmeltInSmokerTask(new SmeltTarget(new ItemTarget(cooked.get(), targetCount), new ItemTarget(raw, targetCount)));
                    return smeltTask;
                }
            }
        }
        // Look for food.
        setDebugState("Searching...");
        return new TimeoutWanderTask();
    }

    private Task killTaskOrNull(Entity entity, Predicate<Entity> entityPredicate, Item itemToGrab) {
        return new KillAndLootTask(entity.getClass(), entityPredicate, new ItemTarget(itemToGrab, 1));
    }

    private Task pickupTaskOrNull(Rinforzando mod, Item itemToGrab, double maxRange) {
        Optional<ItemEntity> nearestDrop = Optional.empty();
        if (mod.getEntityTracker().itemDropped(itemToGrab)) {
            nearestDrop = mod.getEntityTracker().getClosestItemDrop(mod.getPlayer().getPos(), itemToGrab);
        }
        if (nearestDrop.isPresent()) {
            if (nearestDrop.get().isInRange(mod.getPlayer(), maxRange)) {
                return new PickupDroppedItemTask(new ItemTarget(itemToGrab), true);
            }
            //return new GetToBlockTask(nearestDrop.getBlockPos(), false);
        }
        return null;
    }

    private Task pickupTaskOrNull(Rinforzando mod, Item itemToGrab) {
        return pickupTaskOrNull(mod, itemToGrab, Double.POSITIVE_INFINITY);
    }

    @Override
    protected void onStop(Task interruptTask) {

    }

    @Override
    public boolean isFinished() {
        return StorageHelper.calculateInventoryFoodScore() >= unitsNeeded && smeltTask == null;
    }

    @Override
    protected boolean isEqual(Task other) {
        if (other instanceof CollectMeatTask task) {
            return task.unitsNeeded == unitsNeeded;
        }
        return false;
    }

    @Override
    protected String toDebugString() {
        return "Collect " + unitsNeeded + " units of meat.";
    }

    public static class CookableFoodTarget {
        public String rawFood;
        public String cookedFood;
        public Class<?> mobToKill;

        public CookableFoodTarget(String rawFood, String cookedFood, Class<?> mobToKill) {
            this.rawFood = rawFood;
            this.cookedFood = cookedFood;
            this.mobToKill = mobToKill;
        }

        public CookableFoodTarget(String rawFood, Class<?> mobToKill) {
            this(rawFood, "cooked_" + rawFood, mobToKill);
        }

        public Item getRaw() {
            return Objects.requireNonNull(TaskCatalogue.getItemMatches(rawFood))[0];
        }

        public Item getCooked() {
            return Objects.requireNonNull(TaskCatalogue.getItemMatches(cookedFood))[0];
        }

        public int getCookedUnits() {
            assert ItemVer.getFoodComponent(getCooked()) != null;
            return ItemVer.getFoodComponent(getCooked()).getHunger();
        }
    }
}
