package adris.rinforzando.tasks.entity;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasks.movement.GetToEntityTask;
import adris.rinforzando.tasks.movement.PickupDroppedItemTask;
import adris.rinforzando.tasks.movement.TimeoutWanderTask;
import adris.rinforzando.tasks.resources.KillAndLootTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.helpers.ItemHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;

import java.util.Optional;

public class HeroTask extends Task {
    @Override
    protected void onStart() {

    }

    @Override
    protected Task onTick() {
        Rinforzando mod = Rinforzando.getInstance();

        if (mod.getFoodChain().needsToEat()) {
            setDebugState("Eat first.");
            return null;
        }
        Optional<Entity> experienceOrb = mod.getEntityTracker().getClosestEntity(ExperienceOrbEntity.class);
        if (experienceOrb.isPresent()) {
            setDebugState("Getting experience.");
            return new GetToEntityTask(experienceOrb.get());
        }
        assert MinecraftClient.getInstance().world != null;
        Iterable<Entity> hostiles = MinecraftClient.getInstance().world.getEntities();
        if (hostiles != null) {
            for (Entity hostile : hostiles) {
                if (hostile instanceof HostileEntity || hostile instanceof SlimeEntity) {
                    Optional<Entity> closestHostile = mod.getEntityTracker().getClosestEntity(hostile.getClass());
                    if (closestHostile.isPresent()) {
                        setDebugState("Killing hostiles or picking hostile drops.");
                        return new KillAndLootTask(hostile.getClass(), new ItemTarget(ItemHelper.HOSTILE_MOB_DROPS));
                    }
                }
            }
        }
        if (mod.getEntityTracker().itemDropped(ItemHelper.HOSTILE_MOB_DROPS)) {
            setDebugState("Picking hostile drops.");
            return new PickupDroppedItemTask(new ItemTarget(ItemHelper.HOSTILE_MOB_DROPS), true);
        }
        setDebugState("Searching for hostile mobs.");
        return new TimeoutWanderTask();
    }

    @Override
    protected void onStop(Task interruptTask) {

    }

    @Override
    protected boolean isEqual(Task other) {
        return other instanceof HeroTask;
    }

    @Override
    protected String toDebugString() {
        return "Killing all hostile mobs.";
    }
}
