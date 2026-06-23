package adris.rinforzando.tasks.resources;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.Debug;
import adris.rinforzando.TaskCatalogue;
import adris.rinforzando.tasks.ResourceTask;
import adris.rinforzando.tasks.entity.AbstractDoToEntityTask;
import adris.rinforzando.tasks.movement.TimeoutWanderTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.ItemTarget;
import adris.rinforzando.util.helpers.EntityHelper;
import adris.rinforzando.util.time.TimerGame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.HashSet;
import java.util.Optional;

public class TradeWithPiglinsTask extends ResourceTask {

    // TODO: Settings? Custom parameter?
    private static final boolean AVOID_HOGLINS = true;
    private static final double HOGLIN_AVOID_TRADE_RADIUS = 64;
    private final int goldBuffer;
    private final Task tradeTask = new PerformTradeWithPiglin();
    private Task goldTask = null;

    public TradeWithPiglinsTask(int goldBuffer, ItemTarget[] itemTargets) {
        super(itemTargets);
        this.goldBuffer = goldBuffer;
    }

    public TradeWithPiglinsTask(int goldBuffer, ItemTarget target) {
        super(target);
        this.goldBuffer = goldBuffer;
    }

    public TradeWithPiglinsTask(int goldBuffer, Item item, int targetCount) {
        super(item, targetCount);
        this.goldBuffer = goldBuffer;
    }

    @Override
    protected boolean shouldAvoidPickingUp(Rinforzando mod) {
        return false;
    }

    @Override
    protected void onResourceStart(Rinforzando mod) {

    }

    @Override
    protected Task onResourceTick(Rinforzando mod) {
        // Collect gold if we don't have it.
        if (goldTask != null && goldTask.isActive() && !goldTask.isFinished()) {
            setDebugState("Collecting gold");
            return goldTask;
        }
        if (!mod.getItemStorage().hasItem(Items.GOLD_INGOT)) {
            if (goldTask == null) goldTask = TaskCatalogue.getItemTask(Items.GOLD_INGOT, goldBuffer);
            return goldTask;
        }

        // If we have no piglin nearby, explore until we find piglin.
        if (!mod.getEntityTracker().entityFound(PiglinEntity.class)) {
            setDebugState("Wandering");
            return new TimeoutWanderTask(false);
        }

        // If we have a trading piglin that's too far away, get closer to it.

        // Find gold and trade with a piglin
        setDebugState("Trading with Piglin");
        return tradeTask;
    }

    @Override
    protected void onResourceStop(Rinforzando mod, Task interruptTask) {

    }

    @Override
    protected boolean isEqualResource(ResourceTask other) {
        return other instanceof TradeWithPiglinsTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Trading with Piglins";
    }

    static class PerformTradeWithPiglin extends AbstractDoToEntityTask {

        private static final double PIGLIN_NEARBY_RADIUS = 10;
        private final TimerGame _barterTimeout = new TimerGame(2);
        private final TimerGame _intervalTimeout = new TimerGame(10);
        private final HashSet<Entity> _blacklisted = new HashSet<>();
        private Entity _currentlyBartering = null;

        public PerformTradeWithPiglin() {
            super(3);
        }

        @Override
        protected void onStart() {
            super.onStart();
            Rinforzando mod = Rinforzando.getInstance();

            mod.getBehaviour().push();

            // Don't throw away our gold lol
            mod.getBehaviour().addProtectedItems(Items.GOLD_INGOT);

            // Don't attack piglins unless we've blacklisted them.
            mod.getBehaviour().addForceFieldExclusion(entity -> {
                if (entity instanceof PiglinEntity) {
                    return !_blacklisted.contains(entity);
                }
                return false;
            });
            //_blacklisted.clear();
        }

        @Override
        protected void onStop(Task interruptTask) {
            super.onStop(interruptTask);
            Rinforzando.getInstance().getBehaviour().pop();
        }

        @Override
        protected boolean isSubEqual(AbstractDoToEntityTask other) {
            return other instanceof PerformTradeWithPiglin;
        }

        @Override
        protected Task onEntityInteract(Rinforzando mod, Entity entity) {

            // If we didn't run this in a while, we can retry bartering.
            if (_intervalTimeout.elapsed()) {
                // We didn't interact for a while, continue bartering as usual.
                _barterTimeout.reset();
                _intervalTimeout.reset();
            }

            // We're trading so reset the barter timeout
            if (EntityHelper.isTradingPiglin(_currentlyBartering)) {
                _barterTimeout.reset();
            }

            // We're bartering a new entity.
            if (!entity.equals(_currentlyBartering)) {
                _currentlyBartering = entity;
                _barterTimeout.reset();
            }

            if (_barterTimeout.elapsed()) {
                // We failed bartering.
                Debug.logMessage("Failed bartering with current piglin, blacklisting.");
                _blacklisted.add(_currentlyBartering);
                _barterTimeout.reset();
                _currentlyBartering = null;
                return null;
            }

            if (AVOID_HOGLINS && _currentlyBartering != null && !EntityHelper.isTradingPiglin(_currentlyBartering)) {
                Optional<Entity> closestHoglin = mod.getEntityTracker().getClosestEntity(_currentlyBartering.getPos(), HoglinEntity.class);
                if (closestHoglin.isPresent() && closestHoglin.get().isInRange(entity, HOGLIN_AVOID_TRADE_RADIUS)) {
                    Debug.logMessage("Aborting further trading because a hoglin showed up");
                    _blacklisted.add(_currentlyBartering);
                    _barterTimeout.reset();
                    _currentlyBartering = null;
                }
            }

            setDebugState("Trading with piglin");

            if (mod.getSlotHandler().forceEquipItem(Items.GOLD_INGOT)) {
                mod.getController().interactEntity(mod.getPlayer(), entity, Hand.MAIN_HAND);
                _intervalTimeout.reset();
            }
            return null;
        }

        @Override
        protected Optional<Entity> getEntityTarget(Rinforzando mod) {
            // Ignore trading piglins
            Optional<Entity> found = mod.getEntityTracker().getClosestEntity(mod.getPlayer().getPos(),
                    entity -> {
                        if (_blacklisted.contains(entity)
                                || EntityHelper.isTradingPiglin(entity)
                                || (entity instanceof LivingEntity && ((LivingEntity) entity).isBaby())
                                || (_currentlyBartering != null && !entity.isInRange(_currentlyBartering, PIGLIN_NEARBY_RADIUS))) {
                            return false;
                        }

                        if (AVOID_HOGLINS) {
                            // Avoid trading if hoglin is anywhere remotely nearby.
                            Optional<Entity> closestHoglin = mod.getEntityTracker().getClosestEntity(entity.getPos(), HoglinEntity.class);
                            return closestHoglin.isEmpty() || !closestHoglin.get().isInRange(entity, HOGLIN_AVOID_TRADE_RADIUS);
                        }
                        return true;
                    }, PiglinEntity.class
            );
            if (found.isEmpty()) {
                if (_currentlyBartering != null && (_blacklisted.contains(_currentlyBartering) || !_currentlyBartering.isAlive())) {
                    _currentlyBartering = null;
                }
                found = Optional.ofNullable(_currentlyBartering);
            }
            return found;
        }

        @Override
        protected String toDebugString() {
            return "Trading with piglin";
        }
    }

}
