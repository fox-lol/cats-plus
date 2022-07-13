package xyz.foxkin.catsplus.commonside.access;

public interface CatEntityAccess {

    /**
     * Gets whether the ability for a cat to sit or sleep is not on cooldown.
     *
     * @return Whether the ability for a cat to sit or sleep is not on cooldown.
     */
    boolean catsPlus$canSitOrSleep();

    /**
     * Sets the cooldown for the ability for a cat to sit or sleep.
     *
     * @param sitOrSleepCooldown The cooldown for the ability for a cat to sit or sleep.
     */
    void catsPlus$setSitOrSleepCooldown(int sitOrSleepCooldown);
}
